package com.kaichunlin.transition.adapter;

import android.support.annotation.NonNull;

import com.kaichunlin.transition.AbstractTransitionBuilder;
import com.kaichunlin.transition.DefaultTransitionManager;
import com.kaichunlin.transition.Transition;
import com.kaichunlin.transition.TransitionManagerListener;
import com.kaichunlin.transition.TransitionManager;

import java.util.List;

/**
 * Provides implementation shared by most adapters.
 */
public abstract class AbstractAdapter implements TransitionAdapter {
    private TransitionManager mTransitionManager = new DefaultTransitionManager();
    private AdapterState mAdapterState;

    public AbstractAdapter() {
        mAdapterState = new AdapterState();
    }

    public AbstractAdapter(@NonNull AdapterState adapterState) {
        mAdapterState = adapterState;
    }

    public void setTransitionManager(TransitionManager manager) {
        mTransitionManager = manager;
    }

    public TransitionManager getTransitionManager() {
        return mTransitionManager;
    }

    @Override
    public AdapterState getAdapterState() {
        return mAdapterState;
    }

    @Override
    public void addTransition(@NonNull AbstractTransitionBuilder builder) {
        mTransitionManager.addTransition(builder);
    }

    @Override
    public void addTransition(@NonNull Transition transition) {
        mTransitionManager.addTransition(transition);
    }

    @Override
    public void addAllTransitions(@NonNull List<Transition> transitionsList) {
        mTransitionManager.addAllTransitions(transitionsList);
    }

    @Override
    public boolean removeTransition(@NonNull Transition transition) {
        return mTransitionManager.removeTransition(transition);
    }

    @Override
    public void removeAllTransitions() {
        stopTransition();
        mTransitionManager.removeAllTransitions();
    }

    @Override
    public List<Transition> getTransitions() {
        return mTransitionManager.getTransitions();
    }

    @Override
    public boolean startTransition() {
        return startTransition(getAdapterState().isOpen()?1:0);
    }

    @Override
    public boolean startTransition(float progress) {
        if (mAdapterState.isTransiting()) {
            return false;
        }

        mTransitionManager.startTransition(progress);
        mAdapterState.setTransiting(true);
        return true;
    }

    @Override
    public void updateProgress(float value) {
        mTransitionManager.updateProgress(value);
    }

    @Override
    public void stopTransition() {
        if (!mAdapterState.isTransiting()) {
            return;
        }

        mTransitionManager.stopTransition();
        mAdapterState.setTransiting(false);
    }

    @Override
    public void addTransitionListener(TransitionManagerListener listener) {
        mTransitionManager.addTransitionListener(listener);
    }

    @Override
    public void removeTransitionListener(TransitionManagerListener listener) {
        mTransitionManager.removeTransitionListener(listener);
    }

    @Override
    public void notifyTransitionStart() {
        mTransitionManager.notifyTransitionStart();
    }

    @Override
    public void notifyTransitionEnd() {
        mTransitionManager.notifyTransitionEnd();
    }
}
