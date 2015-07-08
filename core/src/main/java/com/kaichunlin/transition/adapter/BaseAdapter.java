package com.kaichunlin.transition.adapter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.kaichunlin.transition.BaseTransitionBuilder;
import com.kaichunlin.transition.ITransition;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides implementation shared by most adapters
 * <p>
 * Created by Kai-Chun Lin on 2015/4/18.
 */
public abstract class BaseAdapter implements ITransitionAdapter {
    protected final Map<String, ITransition> mTransitionList = new HashMap<>();
    boolean mTransitioning;

    @Override
    public void addTransition(@NonNull BaseTransitionBuilder transitionBuilder) {
        addTransition(transitionBuilder.build());
    }

    @Override
    public void addTransition(@NonNull ITransition transition) {
        mTransitionList.put(transition.getId(), transition);
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

    /**
     * Starts the transition,
     */
    protected void startTransition() {
        startTransition(0);
    }

    /**
     * Starts the transition, isTransitioning() will always returns true after this method is executed
     *
     * @param progress the starting transition progress, the valid value range depends on the specific Adapter implementation
     * @return true if the call caused the transition to be started, false if the transition is already in the started state
     */
    protected boolean startTransition(float progress) {
        if (mTransitioning) {
            return false;
        }
        for (ITransition trans : mTransitionList.values()) {
            trans.startTransition();
        }
        updateProgress(progress);
        mTransitioning = true;
        return true;
    }

    /**
     * Updates the transition progress
     *
     * @param value
     */
    protected void updateProgress(float value) {
        for (ITransition trans : mTransitionList.values()) {
            trans.updateProgress(value);
        }
    }

    /**
     * Stops all transitions
     */
    @Override
    public void stopTransition() {
        for (ITransition trans : mTransitionList.values()) {
            trans.stopTransition();
        }
        mTransitioning = false;
    }

    @Override
    public void clearTransition() {
        stopTransition();
        mTransitionList.clear();
    }

    @Override
    public boolean isTransitioning() {
        return mTransitioning;
    }
}
