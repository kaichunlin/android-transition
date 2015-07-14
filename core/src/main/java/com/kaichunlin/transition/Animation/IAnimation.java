package com.kaichunlin.transition.Animation;

import android.support.annotation.IntRange;
import android.support.annotation.UiThread;

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
    @UiThread
    void startAnimation();

    /**
     * Starts the animation with the specified duration
     *
     * @param duration
     */
    @UiThread
    void startAnimation(@IntRange(from = 0) int duration);

    void startAnimationDelayed(@IntRange(from = 0) int delay);

    void startAnimationDelayed(@IntRange(from = 0) final int duration, @IntRange(from = 0) int delay);

    /**
     * Cancels the animation, i.e. the affected Views will retain their last states
     */
    @UiThread
    void cancelAnimation();

    /**
     * Ends the animation, i.e. the affected Views will be assigned their final states
     */
    @UiThread
    void endAnimation();

    /**
     * Stops the animation, i.e. the affected Views will be reverted to their original states
     */
    @UiThread
    void stopAnimation();

    /**
     * Similar to {@link #stopAnimation()} but works even when the animation is not started.
     */
    @UiThread
    void resetAnimation();
}
