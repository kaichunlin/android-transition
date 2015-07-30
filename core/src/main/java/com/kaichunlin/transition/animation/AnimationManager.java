package com.kaichunlin.transition.animation;

import android.animation.ValueAnimator;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import com.kaichunlin.transition.AbstractTransitionBuilder;
import com.kaichunlin.transition.Transition;

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
    private final List<Animation> mAnimationList = new ArrayList<>();
    private boolean mCheckAnimationType;
    private boolean mPassAnimationTypeCheck;

    /**
     * Same as calling addAnimation(transitionBuilder.buildAnimation())
     *
     * @param transitionBuilder
     */
    public void addAnimation(@NonNull AbstractTransitionBuilder transitionBuilder) {
        addAnimation(transitionBuilder.buildAnimation());
    }

    /**
     * Adds an animation
     *
     * @param animation
     */
    public void addAnimation(@NonNull Animation animation) {
        mAnimationList.add(animation);
        mCheckAnimationType = true;
    }

    /**
     * @param animationsList
     */
    public void addAllAnimations(@NonNull List<Animation> animationsList) {
        mAnimationList.addAll(animationsList);
        mCheckAnimationType = true;
    }

    /**
     * @param transitionList
     */
    public void addAllTransitions(List<Transition> transitionList) {
        final int size = transitionList.size();
        for (int i = 0; i < size; i++) {
            mAnimationList.add(new TransitionAnimation(transitionList.get(i)));
        }
        mCheckAnimationType = true;
    }

    /**
     * Removes an animation, should not be called while animation is in progress
     *
     * @param animation
     * @return true if an animation is removed, false otherwise
     */
    public boolean removeAnimation(@NonNull Animation animation) {
        if (mAnimationList.remove(animation)) {
            animation.removeAnimationListener(mAnimationListener);
            return true;
        }
        return false;
    }

    /**
     * Stops and clears all transitions
     */
    public void removeAllAnimations() {
        final int size = mAnimationList.size();
        for (int i = 0; i < size; i++) {
            mAnimationList.get(i).removeAnimationListener(mAnimationListener);
        }
        mAnimationList.clear();
    }

    /**
     * @return
     */
    public List<Animation> getAnimations() {
        return new ArrayList<>(mAnimationList);
    }


    @Override
    public void setDuration(@IntRange(from = 0) int duration) {
        super.setDuration(duration);
    }

    @Override
    public int getDuration() {
        if (super.getDuration() == -1) {
            int maxDuration = 0;
            int duration;
            final int size = mAnimationList.size();
            for (int i = 0; i < size; i++) {
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
        final int size = mAnimationList.size();
        Animation animation;
        for (int i = 0; i < size; i++) {
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
            ValueAnimator sharedAnimator = new ValueAnimator();
            for (int i = 0; i < size; i++) {
                ((TransitionAnimation) mAnimationList.get(i)).startAnimation(sharedAnimator, duration);
            }
            sharedAnimator.start();
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
            final int size = mAnimationList.size();
            for (int i = 0; i < size; i++) {
                mAnimationList.get(i).cancelAnimation();
            }
        }
    }

    @UiThread
    @Override
    public void pauseAnimation() {
        final int size = mAnimationList.size();
        for (int i = 0; i < size; i++) {
            mAnimationList.get(i).pauseAnimation();
        }
    }

    @UiThread
    @Override
    public void resumeAnimation() {
        final int size = mAnimationList.size();
        for (int i = 0; i < size; i++) {
            mAnimationList.get(i).resumeAnimation();
        }
    }

    @UiThread
    @Override
    public void endAnimation() {
        final int size = mAnimationList.size();
        for (int i = 0; i < size; i++) {
            mAnimationList.get(i).endAnimation();
        }
    }

    @UiThread
    @Override
    public void resetAnimation() {
        final int size = mAnimationList.size();
        for (int i = 0; i < size; i++) {
            mAnimationList.get(i).resetAnimation();
        }
    }
}
