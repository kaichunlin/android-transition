package com.kaichunlin.transition.adapter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.kaichunlin.transition.BaseTransitionBuilder;
import com.kaichunlin.transition.ITransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides implementation shared by most adapters
 * <p>
 * Created by Kai-Chun Lin on 2015/4/18.
 */
public abstract class BaseAdapter implements ITransitionAdapter {
    protected final Set<TransitionListener> transitionListenerList = new HashSet<>();
    protected final Map<String, ITransition> mTransitionList = new HashMap<>();
    private AdapterState mAdapterState;

    public BaseAdapter() {
        mAdapterState = new AdapterState();
    }

    public BaseAdapter(AdapterState adapterState) {
        mAdapterState = adapterState;
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
        addTransition(transitionBuilder.build());
    }

    @Override
    public void addTransition(@NonNull ITransition transition) {
        mTransitionList.put(transition.getId(), transition);
    }

    @Override
    public void addAllTransitions(@NonNull List<ITransition> transitionsList) {
        for (ITransition transition : transitionsList) {
            mTransitionList.put(transition.getId(), transition);
        }
    }

    @Override
    public boolean removeTransition(@NonNull ITransition transition) {
        if (mTransitionList.remove(transition.getId()) == null) {
            //fallback check
            for (Map.Entry<String, ITransition> entry : mTransitionList.entrySet()) {
                if (entry.getValue() == transition) {
                    Log.w(getClass().getSimpleName(), "removeTransition: transition has its ID changed after being added " + transition.getId());
                    mTransitionList.remove(entry.getKey());
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public void removeAllTransitions() {
        stopTransition();
        mTransitionList.clear();
    }

    @Override
    public List<ITransition> getTransitions() {
        return new ArrayList<>(mTransitionList.values());
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

        for (ITransition trans : mTransitionList.values()) {
            trans.startTransition(progress);
        }
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
        for (ITransition trans : mTransitionList.values()) {
            trans.updateProgress(value);
        }
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

        for (ITransition trans : mTransitionList.values()) {
            trans.stopTransition();
        }
        mAdapterState.setTransiting(false);
    }

    protected void notifyStopTransition() {
        for (TransitionListener listener:transitionListenerList) {
            listener.onStopTransition(this);
        }
    }
}
