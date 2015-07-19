package com.kaichunlin.transition.adapter;

import com.kaichunlin.transition.TransitionManager;

/**
 * Created by Kai on 2015/7/14.
 */
public interface TransitionAdapter extends TransitionManager {
    AdapterState getAdapterState();

    void setTransitionManager(TransitionManager transitionManager);

    TransitionManager getTransitionManager();
}
