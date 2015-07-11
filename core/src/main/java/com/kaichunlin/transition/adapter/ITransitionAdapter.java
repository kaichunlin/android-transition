package com.kaichunlin.transition.adapter;

import com.kaichunlin.transition.BaseTransitionBuilder;
import com.kaichunlin.transition.ITransition;

import java.util.List;

/**
 * Allows the adaption of different UI interactive components (e.g. Drawer) to support transition effects
 * <p>
 * Created by Kai-Chun Lin on 2015/5/20.
 */
public interface ITransitionAdapter {
    AdapterState getAdapterState();

    void addTransitionListener(TransitionListener transitionListener);

    void removeTransitionListener(TransitionListener transitionListener);

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
     * Starts the transition,
     */
    void startTransition();

    /**
     * Starts the transition, {@link #getAdapterState()}.isTransitioning() will always returns true after this method is executed.
     *
     * @param progress the starting transition progress, the valid value range depends on the specific Adapter implementation
     * @return true if the call caused the transition to be started, false if the transition is already in the started state
     */
    boolean startTransition(float progress);
    void updateProgress(float value);

    /**
     * Stops all transitions
     */
    void stopTransition();

    /**
     * Stops and clears all transitions
     */
    void removeAllTransitions();

    interface TransitionListener {
        void onStartTransition(ITransitionAdapter adapter);

        void onStopTransition(ITransitionAdapter adapter);
    }
}