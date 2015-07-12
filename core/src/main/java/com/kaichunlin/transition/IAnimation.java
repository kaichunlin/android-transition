package com.kaichunlin.transition;

import android.support.annotation.IntRange;

/**
 * Created by Kai on 2015/7/12.
 */
public interface IAnimation {

    void setAnimationDuration(@IntRange(from = 0) int duration);
    int getAnimationDuration();

    void setReverseAnimation(boolean reverse);

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

    void startAnimationDelayed(@IntRange(from = 0) int delay);

    void startAnimationDelayed(@IntRange(from = 0) final int duration, @IntRange(from = 0) int delay);

    /**
     * Cancels the animation, i.e. the affected Views will retain their last states
     */
    void cancelAnimation();

    /**
     * Ends the animation, i.e. the affected Views will be assigned their final states
     */
    void endAnimation();

    /**
     * Stops the animation, i.e. the affected Views will be reverted to their original states
     */
    void stopAnimation();

    /**
     * Similar to {@link #stopAnimation()} but works even when the animation is not started.
     */
    void resetAnimation();
}
