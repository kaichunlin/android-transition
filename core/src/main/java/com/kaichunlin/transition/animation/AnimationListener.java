package com.kaichunlin.transition.animation;

/**
 * Receives state notifications from an animation.
 */
public interface AnimationListener {
    /**
     * Notifies the start of the animation.
     *
     * @param animation The started animation.
     */
    void onAnimationStart(Animation animation);

    /**
     * Notifies the end of the animation; the affected Views have been assigned their final states.
     *
     * @param animation The animation which reached its end.
     */
    void onAnimationEnd(Animation animation);

    /**
     * Notifies the cancellation of the animation; the affected Views retain the states assigned in the last animated frame.
     *
     * @param animation The animation which was cancelled.
     */
    void onAnimationCancel(Animation animation);

    /**
     * Notifies the animation's been reset; the affected Views have been reverted to the states when the animation was started.
     *
     * @param animation The animation which was reset.
     */
    void onAnimationReset(Animation animation);
}