package com.kaichunlin.transition.adapter;

import com.kaichunlin.transition.ITransitionManager;

/**
 * Created by Kai on 2015/7/14.
 */
public interface ITransitionAdapter extends ITransitionManager {
    AdapterState getAdapterState();

    void addTransitionListener(TransitionListener transitionListener);

    void removeTransitionListener(TransitionListener transitionListener);

    interface TransitionListener {
        void onStartTransition(ITransitionManager adapter);

        void onStopTransition(ITransitionManager adapter);
    }
}
