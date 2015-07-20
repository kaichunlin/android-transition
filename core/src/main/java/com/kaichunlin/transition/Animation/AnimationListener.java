package com.kaichunlin.transition.animation;

/**
 * Created by Kai on 2015/7/18.
 */
public interface AnimationListener {
    void onAnimationStart(Animation animationManager);

    /**
     * Notify the animation's ended, the affected Views have been assigned their final states
     */
    void onAnimationEnd(Animation animationManager);

    /**
     * Notify the animation's canceled, the affected Views retain their last states
     */
    void onAnimationCancel(Animation animationManager);

    /**
     * Notify the animation's been reset, the affected Views have been reverted to their original states
     */
    void onAnimationReset(Animation animationManager);
}