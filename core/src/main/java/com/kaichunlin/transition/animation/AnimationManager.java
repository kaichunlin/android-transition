package com.kaichunlin.transition.animation;

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
    }

    /**
     * @param animationsList
     */
    public void addAllAnimations(@NonNull List<Animation> animationsList) {
        mAnimationList.addAll(animationsList);
    }

    /**
     * @param transitionList
     */
    public void addAllTransitions(List<Transition> transitionList) {
        for (Transition transition : transitionList) {
            mAnimationList.add(new TransitionAnimation(transition));
        }
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
        for (Animation animation : mAnimationList) {
            animation.removeAnimationListener(mAnimationListener);
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
            for (Animation trans : mAnimationList) {
                duration = trans.getDuration();
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
        for (Animation trans : mAnimationList) {
            trans.setReverseAnimation(!trans.isReverseAnimation());
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
        if (mAnimationList.size() == 0) {
            return;
        }
        mAnimationList.get(0).addAnimationListener(mAnimationListener);
        //call listeners so they can perform their actions first, like modifying this adapter's transitions
        notifyAnimationStart();

        for (Animation trans : mAnimationList) {
            trans.startAnimation(duration);
        }
    }

    @UiThread
    @Override
    public void cancelAnimation() {
        if(isAnimating()) {
            for (Animation trans : mAnimationList) {
                trans.cancelAnimation();
            }
        }
    }

    @UiThread
    @Override
    public void pauseAnimation() {
        for (Animation trans : mAnimationList) {
            trans.pauseAnimation();
        }
    }

    @UiThread
    @Override
    public void resumeAnimation() {
        for (Animation trans : mAnimationList) {
            trans.resumeAnimation();
        }
    }

    @UiThread
    @Override
    public void endAnimation() {
        for (Animation trans : mAnimationList) {
            trans.endAnimation();
        }
    }

    @UiThread
    @Override
    public void resetAnimation() {
        for (Animation trans : mAnimationList) {
            trans.resetAnimation();
        }
    }
}
