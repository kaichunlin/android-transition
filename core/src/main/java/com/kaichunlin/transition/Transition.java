package com.kaichunlin.transition;

import android.support.annotation.UiThread;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * Represent the operations that should be performed with the given progress, usually on a View.
 */
public interface Transition<T extends AbstractTransition.Setup> extends TransitionOperation, Cloneable {

    /**
     * Sets an ID the transition, used internally for debugging purpose.
     *
     * @param id
     * @return
     */
    AbstractTransition setId(String id);

    /**
     *
     * @return ID assigned for the transition, used internally for debugging purpose.
     */
    String getId();

    /**
     * Sets the transition progress, many times in the range of 0.0f ~ 1.0f, but not necessarily so.
     *
     * @param progress
     */
    @UiThread
    void setProgress(float progress);

    /**
     * Reverses the transition.
     *
     * @return itself
     */
    Transition reverse();

    /**
     * A {@link AbstractTransition.Setup} is an object that configures how a Transition for a specific effect.
     * <p>
     * For examples see MenuItemTransitionBuilder.setupAnimation(MenuItem, TransitionControllerManager, int, int)
     * and ViewTransitionBuilder#setupAnimation(TransitionControllerManager).
     *
     * @param setup
     * @return itself
     */
    Transition setSetup(T setup);

    /**
     *
     * @return A carbon clone of this object that can be used independently from the object it's cloned from.
     */
    Transition clone();

    /**
     *
     * @param interpolator The Interpolator that should be used.
     * @return itself
     */
    Transition setInterpolator(Interpolator interpolator);

    /**
     *
     * @param target The View this transition would modify.
     */
    void setTarget(View target);

    /**
     *
     * @return The View this transition would modify.
     */
    View getTarget();

    /**
     *
     * @param updateStateAfterUpdateProgress Whether or not to update a controller's enable state after
     *                                       each {@link #updateProgress(float)} call.
     * @return itself
     */
    Transition setUpdateStateAfterUpdateProgress(boolean updateStateAfterUpdateProgress);
}
