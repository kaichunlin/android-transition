package com.kaichunlin.transition.animation;

import android.support.annotation.IntRange;
import android.support.annotation.UiThread;

/**
 */
@UiThread
public interface Animation {

    /**
     *
     * @param animationListener
     */
    void addAnimationListener(AnimationListener animationListener);

    /**
     *
     * @param animationListener
     */
    void removeAnimationListener(AnimationListener animationListener);

    void setDuration(@IntRange(from = 0) int duration);
    int getDuration();

     void setReverseAnimation(boolean reverse);

    /**
     *
     * @return
     */
    boolean isReverseAnimation();

    /**
     * Starts the animation with the default duration (300 ms)
     */
    void startAnimation();

    /**
     * Starts the animation with the specified duration
     *
     * @param duration
     */
    void startAnimation(@IntRange(from = 0) int duration);

    /**
     *
     * @param delay
     */
    void startAnimationDelayed(@IntRange(from = 0) int delay);

    /**
     *
     * @param duration
     * @param delay
     */
    void startAnimationDelayed(@IntRange(from = 0) int duration, @IntRange(from = 0) int delay);

    boolean isAnimating();

    /**
     * Cancels the animation, i.e. the affected Views will retain their last states. A canceled animation cannot be resumed.
     */
    void cancelAnimation();

    /**
     * Pauses the animation, i.e. the affected Views will retain their last states. A canceled animation can be resumed.
     */
    void pauseAnimation();

    /**
     * Resumes the animation, i.e. the affected Views will continue its animation
     */
    void resumeAnimation();

    /**
     * Ends the animation, i.e. the affected Views will be assigned their final states
     */
    void endAnimation();

    /**
     *  Forces the animation's end state to be applied immediately even if the animation has not started. If the animation has started, it will end immediately.
     */
    void forceEndState();

    /**
     * Stops the animation, i.e. the affected Views will be reverted to their original states
     */
    void resetAnimation();
}
