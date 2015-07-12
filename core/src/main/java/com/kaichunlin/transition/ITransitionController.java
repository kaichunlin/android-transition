package com.kaichunlin.transition;

import android.view.View;
import android.view.animation.Interpolator;

/**
 * Implementors would provide the actual implementation for a transition
 * <p>
 * Created by Kai-Chun Lin on 2015/4/28.
 */
public interface ITransitionController extends Cloneable {
    float DEFAULT_START = 0f;
    float DEFAULT_END = 1f;

    void setId(String id);

    String getId();

    /**
     * Starts the transition
     */
    void start();

    /**
     * Ends the transition
     */
    void end();

    /**
     * @param progress
     */
    void updateProgress(float progress);

    /**
     * Defaults to [0..1] if not set
     *
     * @param start the start of the applicable transition range
     * @param end   the end of the applicable transition range
     * @return
     */
    ITransitionController setRange(float start, float end);

    /**
     * @return the start value for the applicable transition range
     */
    float getStart();

    ITransitionController setStart(float mStart);

    /**
     * @return the end value for the applicable transition range
     */
    float getEnd();

    ITransitionController setEnd(float mEnd);

    View getTarget();

    /**
     * Reverse how the transition is applied, such that the transition previously performed when progress=start of range is only performed when progress=end of range
     *
     * @return
     */
    ITransitionController reverse();

    /**
     *
     * @param target the view this controller should manipulate
     * @return
     */
    ITransitionController setTarget(View target);

    /**
     *
     * @param interpolator the Interpolator this controller should be when transiting a view
     */
    void setInterpolator(Interpolator interpolator);

    /**
     *
     * @param updateStateAfterUpdateProgress whether or not to update a controller's enable state after each {@link #updateProgress(float)} call
     */
    void setUpdateStateAfterUpdateProgress(boolean updateStateAfterUpdateProgress);

    /**
     * Enables or disables the controller
     *
     * @param enable
     */
    void setEnable(boolean enable);

    /**
     * @return is the controlled enabled
     */
    boolean isEnable();

    /**
     * @return a clone that can be independently manipulated
     */
    ITransitionController clone();
}
