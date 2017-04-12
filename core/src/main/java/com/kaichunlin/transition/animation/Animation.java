package com.kaichunlin.transition.animation;

import android.support.annotation.IntRange;
import android.support.annotation.UiThread;

/**
 * Interface for an Animation.
 */
@UiThread
public interface Animation {

    /**
     * @param listener
     */
    void addAnimationListener(AnimationListener listener);

    /**
     * @param listener
     */
    void removeAnimationListener(AnimationListener listener);

    /**
     *
     * @param duration Length of the animation.
     */
    void setDuration(@IntRange(from = 0) int duration);

    int getDuration();

    /**
     *
     * @param reverse If true, the animation will be played backward.
     */
    void setReverseAnimation(boolean reverse);

    /**
     * @return true if {@link #setReverseAnimation(boolean)} has been set to true.
     */
    boolean isReverseAnimation();

    /**
     * Starts the animation with the default duration (300 ms).
     */
    void startAnimation();

    /**
     * Starts the animation with the specified duration.
     *
     * @param duration
     */
    void startAnimation(@IntRange(from = 0) int duration);

    /**
     * Same as {@link #startAnimation()}, but with an added delay before the animation is started.

     * @param delay
     */
    void startAnimationDelayed(@IntRange(from = 0) int delay);

    /**
     * Same as {@link #startAnimation(int)}, but with an added delay before the animation is started.
     *
     * @param duration
     * @param delay
     */
    void startAnimationDelayed(@IntRange(from = 0) int duration, @IntRange(from = 0) int delay);

    /**
     *
     * @return Is the Animation in the started state.
     */
    boolean isAnimating();

    /**
     * Cancels the animation, i.e. the affected Views will retain their current states but will no longer
     * advance the animation. A canceled animation cannot be resumed.
     */
    void cancelAnimation();

    /**
     * Pauses the animation, i.e. the affected Views will retain their current states but will no longer
     * advance the animation. {@link #resumeAnimation()} can be called afterwards.
     */
    void pauseAnimation();

    /**
     * Resumes the animation, i.e. the affected Views will continue the animations from the paused states.
     */
    void resumeAnimation();

    /**
     * Ends the animation, i.e. the affected Views will be assigned their final states.
     */
    void endAnimation();

    /**
     * Forces the animation's end state to be applied immediately even if the animation has not started.
     * If the animation has started, it will end immediately, the effect would be the same as calling
     * {@link #endAnimation()}.
     */
    void forceEndState();

    /**
     * Stops the animation, i.e. the affected Views will be reverted to their original states at the
     * beginning of the animation.
     */
    void resetAnimation();
}
