package com.kaichunlin.transition.adapter;

import android.support.annotation.NonNull;

import com.kaichunlin.transition.BaseTransitionBuilder;
import com.kaichunlin.transition.ITransition;
import com.kaichunlin.transition.TransitionManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides implementation shared by most adapters
 * <p>
 * Created by Kai-Chun Lin on 2015/4/18.
 */
public abstract class BaseAdapter implements ITransitionAdapter {
    protected final Set<TransitionListener> transitionListenerList = new HashSet<>();
    private final TransitionManager mTransitionManager = new TransitionManager();
    private AdapterState mAdapterState;

    public BaseAdapter() {
        mAdapterState = new AdapterState();
    }

    public BaseAdapter(AdapterState adapterState) {
        mAdapterState = adapterState;
    }

    protected TransitionManager getTransitionManager() {
        return mTransitionManager;
    }

    @Override
    public AdapterState getAdapterState() {
        return mAdapterState;
    }

    @Override
    public void addTransitionListener(TransitionListener transitionListener) {
        transitionListenerList.add(transitionListener);
    }

    @Override
    public void removeTransitionListener(TransitionListener transitionListener) {
        transitionListenerList.remove(transitionListener);
    }

    @Override
    public void addTransition(@NonNull BaseTransitionBuilder transitionBuilder) {
        mTransitionManager.addTransition(transitionBuilder);
    }

    @Override
    public void addTransition(@NonNull ITransition transition) {
        mTransitionManager.addTransition(transition);
    }

    @Override
    public void addAllTransitions(@NonNull List<ITransition> transitionsList) {
        mTransitionManager.addAllTransitions(transitionsList);
    }

    @Override
    public boolean removeTransition(@NonNull ITransition transition) {
        return mTransitionManager.removeTransition(transition);
    }

    @Override
    public void removeAllTransitions() {
        stopTransition();
        mTransitionManager.removeAllTransitions();
    }

    @Override
    public List<ITransition> getTransitions() {
        return mTransitionManager.getTransitions();
    }

    @Override
    public boolean startTransition() {
        return startTransition(0);
    }

    @Override
    public boolean startTransition(float progress) {
        if (mAdapterState.isTransiting()) {
            return false;
        }

        //call listeners so they can perform their actions first, like modifying this adapter's transitions
        notifyStartTransition();

        mTransitionManager.startTransition(progress);
        mAdapterState.setTransiting(true);
        return true;
    }

    protected void notifyStartTransition() {
        for (TransitionListener listener:transitionListenerList) {
            listener.onStartTransition(this);
        }
    }

    /**
     * Updates the transition progress
     *
     * @param value
     */
    public void updateProgress(float value) {
        mTransitionManager.updateProgress(value);
    }

    /**
     * Stops all transitions
     */
    @Override
    public void stopTransition() {
        if (!mAdapterState.isTransiting()) {
            return;
        }

        notifyStopTransition();
        mTransitionManager.stopTransition();
        mAdapterState.setTransiting(false);
    }

    protected void notifyStopTransition() {
        for (TransitionListener listener:transitionListenerList) {
            listener.onStopTransition(this);
        }
    }
}
