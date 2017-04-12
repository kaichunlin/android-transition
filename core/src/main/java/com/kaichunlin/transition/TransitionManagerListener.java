package com.kaichunlin.transition;

/**
 * Listens to transition state changes.
 */
public interface TransitionManagerListener {
    void onTransitionStart(TransitionManager manager);

    void onTransitionEnd(TransitionManager manager);
}