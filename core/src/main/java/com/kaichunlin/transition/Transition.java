package com.kaichunlin.transition;

import android.support.annotation.UiThread;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * Represent the operations that should be performed with the given progress, usually performed on a view.
 * <p>
 * Created by Kai-Chun Lin on 2015/4/18.
 */
public interface Transition<T extends AbstractTransition.Setup> extends TransitionOperation, Cloneable {

    /**
     * Sets an ID the transition, used internally for debugging purpose
     *
     * @param id
     * @return
     */
    AbstractTransition setId(String id);

    /**
     *
     * @return ID assigned for the transition, used internally for debugging purpose
     */
    String getId();

    /**
     * Sets the transition progress
     *
     * @param progress
     */
    @UiThread
    void setProgress(float progress);

    /**
     * Reverses the transition
     *
     * @return itself
     */
    Transition reverse();

    /**
     *
     * @param setup
     * @return itself
     */
    Transition setSetup(T setup);

    /**
     *
     * @return a carbon clone of this object that can be used independently from the object it's cloned from
     */
    Transition clone();

    /**
     *
     * @param interpolator the Interpolator that should be used
     * @return itself
     */
    Transition setInterpolator(Interpolator interpolator);

    /**
     *
     * @param target the target this transition would modify
     */
    void setTarget(View target);

    /**
     *
     * @return the target this transition would modify
     */
    View getTarget();

    /**
     *
     * @param updateStateAfterUpdateProgress whether or not to update a controller's enable state after each {@link #updateProgress(float)} call
     * @return itself
     */
    Transition setUpdateStateAfterUpdateProgress(boolean updateStateAfterUpdateProgress);
}
