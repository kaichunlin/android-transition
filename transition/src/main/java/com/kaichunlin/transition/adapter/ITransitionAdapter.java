package com.kaichunlin.transition.adapter;

import com.kaichunlin.transition.BaseTransitionBuilder;
import com.kaichunlin.transition.ITransition;

/**
 * Allows the adaption of different UI interactive components (e.g. Drawer) to support transition effects
 * <p/>
 * Created by Kai-Chun Lin on 2015/5/20.
 */
public interface ITransitionAdapter {
    /**
     * Same as calling addTransition(transitionBuilder.build())
     *
     * @param transitionBuilder
     */
    void addTransition(BaseTransitionBuilder transitionBuilder);

    /**
     * Adds a transition, should be called when
     *
     * @param transition
     */
    void addTransition(ITransition transition);

    /**
     * Removes a transition, should not be called while transition is in progress (isTransitioning() returns true)
     *
     * @param transition
     * @return true if a transition is removed, false otherwise
     */
    boolean removeTransition(ITransition transition);

    /**
     * Stops all transitions
     */
    void stopTransition();

    /**
     * Stops and clears all transitions
     */
    void clearTransition();

    /**
     *
     * @return is transition in progress
     */
    boolean isTransitioning();
}
