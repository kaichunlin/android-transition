package com.kaichunlin.transition.adapter;

import com.kaichunlin.transition.TransitionManager;

/**
 */
public interface TransitionAdapter extends TransitionManager {
    AdapterState getAdapterState();

    void setTransitionManager(TransitionManager transitionManager);

    TransitionManager getTransitionManager();
}
