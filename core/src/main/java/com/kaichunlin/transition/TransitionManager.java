package com.kaichunlin.transition;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages a collection of {@link ITransition}
 *
 * Created by Kai on 2015/7/14.
 */
public class TransitionManager implements ITransitionManager {
    protected final Map<String, ITransition> mTransitionList = new HashMap<>();

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
        mTransitionList.clear();
    }

    @Override
    public List<ITransition> getTransitions() {
        return new ArrayList<>(mTransitionList.values());
    }

    @Override
    public boolean startTransition() {
        for (ITransition trans : mTransitionList.values()) {
            trans.startTransition();
        }
        return true;
    }

    @Override
    public boolean startTransition(float progress) {
        for (ITransition trans : mTransitionList.values()) {
            trans.startTransition(progress);
        }
        return true;
    }

    /**
     * Updates the transition progress
     *
     * @param value
     */
    @Override
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
        for (ITransition trans : mTransitionList.values()) {
            trans.stopTransition();
        }
    }
}
