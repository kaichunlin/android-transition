package com.kaichunlin.transition;

import java.util.List;

/**
 * Allows the adaption of different UI interactive components (e.g. Drawer) to support transition effects
 * <p>
 * Created by Kai-Chun Lin on 2015/5/20.
 */
public interface ITransitionManager extends IBaseTransition {

    /**
     * Same as calling addTransition(transitionBuilder.build())
     *
     * @param transitionBuilder
     */
    void addTransition(BaseTransitionBuilder transitionBuilder);

    /**
     * Adds a transition
     *
     * @param transition
     */
    void addTransition(ITransition transition);

    void addAllTransitions(List<ITransition> transitionsList);

    /**
     * Removes a transition, should not be called while transition is in progress (isTransitioning() returns true)
     *
     * @param transition
     * @return true if a transition is removed, false otherwise
     */
    boolean removeTransition(ITransition transition);

    List<ITransition> getTransitions();

    /**
     * Stops and clears all transitions
     */
    void removeAllTransitions();
}