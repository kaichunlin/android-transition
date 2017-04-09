package com.kaichunlin.transition;

/**
 */
public interface TransitionListener {
    void onTransitionStart(TransitionManager transitionManager);

    void onTransitionEnd(TransitionManager transitionManager);
}