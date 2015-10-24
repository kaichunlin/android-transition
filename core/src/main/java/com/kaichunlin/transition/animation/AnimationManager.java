package com.kaichunlin.transition.animation;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.view.View;

import com.kaichunlin.transition.AbstractTransition;
import com.kaichunlin.transition.AbstractTransitionBuilder;
import com.kaichunlin.transition.Transition;
import com.kaichunlin.transition.TransitionOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of {@link Animation}
 * <p>
 * Created by Kai on 2015/7/15.
 */
public class AnimationManager extends AbstractAnimation {
    private final AnimationListener mAnimationListener = new AnimationListener() {

        @Override
        public void onAnimationStart(Animation animationManager) {
            setAnimating(true);
        }

        @Override
        public void onAnimationEnd(Animation animationManager) {
            setAnimating(false);
            notifyAnimationEnd();
        }

        @Override
        public void onAnimationCancel(Animation animationManager) {
            setAnimating(false);
            notifyAnimationCancel();
        }

        @Override
        public void onAnimationReset(Animation animationManager) {
            setAnimating(false);
            notifyAnimationReset();
        }
    };
    private final List<AbstractAnimation> mAnimationList = new ArrayList<>();
    private boolean mCheckAnimationType;
    private boolean mPassAnimationTypeCheck;
    private StateController mSharedController;

    /**
     * Same as calling addAnimation(transitionBuilder.buildAnimation())
     *
     * @param transitionBuilder
     */
    public void addAnimation(@NonNull AbstractTransitionBuilder transitionBuilder) {
        addAnimation(transitionBuilder.buildAnimation());
    }

    private void processAnimation(AbstractAnimation animation) {
        //attempt to merge an animation
        boolean merged = false;
        //no optimization is taken if the TransitionOption is not an AbstractTransition subclass
        if (animation.getTransition() instanceof AbstractTransition) {
            TransitionOperation to;
            for (int i = 0, size = mAnimationList.size(); i < size; i++) {
                to = mAnimationList.get(i).getTransition();
                if (to instanceof AbstractTransition) {
                    merged = ((AbstractTransition) to).merge((AbstractTransition) animation.getTransition());
                    if(merged) {
                        break;
                    }
                }
            }
        }
        if (!merged) {
            mAnimationList.add(animation);
        }
        mCheckAnimationType = true;
    }

    /**
     * Adds an animation
     *
     * @param animation
     */
    public void addAnimation(@NonNull Animation animation) {
        processAnimation((AbstractAnimation) animation);
    }

    /**
     * @param animationsList
     */
    public void addAllAnimations(@NonNull List<Animation> animationsList) {
        final int size = animationsList.size();
        for (int i = 0; i < size; i++) {
            processAnimation((AbstractAnimation) animationsList.get(i));
        }
    }

    /**
     * Adds an transition
     *
     * @param transition
     */
    public void addTransition(@NonNull Transition transition) {
        processAnimation(new TransitionAnimation(transition));
    }

    /**
     * @param transitionList
     */
    public void addAllTransitions(List<Transition> transitionList) {
        final int size = transitionList.size();
        for (int i = 0; i < size; i++) {
            processAnimation(new TransitionAnimation(transitionList.get(i)));
        }
    }

    /**
     * Stops and clears all transitions
     */
    public void removeAllAnimations() {
        for (int i = 0, size = mAnimationList.size(); i < size; i++) {
            mAnimationList.get(i).removeAnimationListener(mAnimationListener);
        }
        mAnimationList.clear();
    }

    /**
     * @return
     */
    public List<Animation> getAnimations() {
        List<Animation> list = new ArrayList<>(mAnimationList.size());
        list.addAll(mAnimationList);
        return list;
    }

    @Override
    public int getDuration() {
        if (super.getDuration() == -1) {
            int maxDuration = 0;
            int duration;
            for (int i = 0, size = mAnimationList.size(); i < size; i++) {
                duration = mAnimationList.get(i).getDuration();
                if (maxDuration < duration) {
                    maxDuration = duration;
                }
            }
            return maxDuration;
        } else {
            return super.getDuration();
        }
    }

    @Override
    public void setReverseAnimation(boolean reverse) {
        if (isReverseAnimation() == reverse) {
            return;
        }
        super.setReverseAnimation(reverse);
        Animation animation;
        for (int i = 0, size = mAnimationList.size(); i < size; i++) {
            animation = mAnimationList.get(i);
            animation.setReverseAnimation(!animation.isReverseAnimation());
        }
    }

    @UiThread
    @Override
    public void startAnimation() {
        startAnimation(getDuration());
    }

    @UiThread
    @Override
    public void startAnimation(@IntRange(from = 0) int duration) {
        if (isAnimating()) {
            cancelAnimation();
        }
        final int size = mAnimationList.size();
        if (size == 0) {
            return;
        }
        mAnimationList.get(0).addAnimationListener(mAnimationListener);
        //call listeners so they can perform their actions first, like modifying this adapter's transitions
        notifyAnimationStart();

        if (mCheckAnimationType) {
            mPassAnimationTypeCheck = true;
            for (int i = 0; i < size; i++) {
                if (!(mAnimationList.get(i) instanceof TransitionAnimation)) {
                    mPassAnimationTypeCheck = false;
                    break;
                }
            }
            mCheckAnimationType = false;
        }

        if (mPassAnimationTypeCheck) {
            List<TransitionOperation> transitionList = new ArrayList<>();
            TransitionOperation transitionOperation;
            for (int i = 0; i < size; i++) {
                transitionOperation = mAnimationList.get(i).getTransition();
                transitionList.add(transitionOperation);
            }

            //TODO fugly
            boolean forceAnimator = false;
            View view = null;
            for (int i = 0; i < size; i++) {
                view = ((AbstractTransition) (mAnimationList.get(i).getTransition())).getTarget();
                if (view == null) {
                    forceAnimator = true;
                }
            }
            if (getStateControllerType() == CONTROLLER_ANIMATOR || forceAnimator) {
                mSharedController = new AnimatorController(isReverseAnimation());
            } else if (getStateControllerType() == CONTROLLER_ANIMATION) {
                mSharedController = new AnimationController(view, isReverseAnimation(), transitionList);
            }

            mSharedController.setAnimationDuration(duration);
            for (int i = 0; i < size; i++) {
                ((TransitionAnimation) mAnimationList.get(i)).prepareAnimation(mSharedController, -1);
            }
            mSharedController.startController();
        } else {
            for (int i = 0; i < size; i++) {
                mAnimationList.get(i).startAnimation(duration);
            }
        }
    }

    @UiThread
    @Override
    public void cancelAnimation() {
        if (isAnimating()) {
            if (mSharedController == null) {
                for (int i = 0, size = mAnimationList.size(); i < size; i++) {
                    mAnimationList.get(i).cancelAnimation();
                }
            } else {
                mSharedController.cancelController();
            }
        }
    }

    @UiThread
    @Override
    public void pauseAnimation() {
        if (mSharedController == null) {
            for (int i = 0, size = mAnimationList.size(); i < size; i++) {
                mAnimationList.get(i).pauseAnimation();
            }
        } else {
            mSharedController.pauseController();
        }
    }

    @UiThread
    @Override
    public void resumeAnimation() {
        if (mSharedController == null) {
            for (int i = 0, size = mAnimationList.size(); i < size; i++) {
                mAnimationList.get(i).resumeAnimation();
            }
        } else {
            mSharedController.resumeController();
        }
    }

    @UiThread
    @Override
    public void endAnimation() {
        if (mSharedController == null) {
            for (int i = 0, size = mAnimationList.size(); i < size; i++) {
                mAnimationList.get(i).endAnimation();
            }
        } else {
            mSharedController.endController();
        }
    }

    @UiThread
    @Override
    public void resetAnimation() {
        if (mSharedController == null) {
            for (int i = 0, size = mAnimationList.size(); i < size; i++) {
                mAnimationList.get(i).resetAnimation();
            }
        } else {
            mSharedController.resetController();
        }
    }
}