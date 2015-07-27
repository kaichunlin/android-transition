package com.kaichunlin.transition;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Manages a collection of {@link Transition}
 * <p>
 * Created by Kai on 2015/7/14.
 */
public class DefaultTransitionManager implements TransitionManager {
    protected Set<TransitionListener> mTransitionListenerSet = new HashSet<>();
    protected ArrayMap<String, Transition> mTransitionMap = new ArrayMap<>();

    @Override
    public void addTransition(@NonNull AbstractTransitionBuilder transitionBuilder) {
        addTransition(transitionBuilder.build());
    }

    @Override
    public void addTransition(@NonNull Transition transition) {
        mTransitionMap.put(transition.getId(), transition);
    }

    @Override
    public void addAllTransitions(@NonNull List<Transition> transitionsList) {
        for (Transition transition : transitionsList) {
            mTransitionMap.put(transition.getId(), transition);
        }
    }

    @Override
    public boolean removeTransition(@NonNull Transition transition) {
        if (mTransitionMap.remove(transition.getId()) == null) {
            //fallback check
            for (Map.Entry<String, Transition> entry : mTransitionMap.entrySet()) {
                if (entry.getValue() == transition) {
                    Log.w(getClass().getSimpleName(), "removeTransition: transition has its ID changed after being added " + transition.getId());
                    mTransitionMap.remove(entry.getKey());
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public void removeAllTransitions() {
        mTransitionMap.clear();
    }

    @Override
    public List<Transition> getTransitions() {
        return new ArrayList<>(mTransitionMap.values());
    }

    @Override
    public boolean startTransition() {
        //call listeners so they can perform their actions first, like modifying this adapter's transitions
        notifyTransitionStart();

        boolean start = false;
        final int size = mTransitionMap.size();
        for (int i = 0; i < size; i++) {
            start |= mTransitionMap.valueAt(i).startTransition();
        }
        return start;
    }

    @Override
    public boolean startTransition(float progress) {
        //call listeners so they can perform their actions first, like modifying this adapter's transitions
        notifyTransitionStart();

        boolean start = false;
        final int size = mTransitionMap.size();
        for (int i = 0; i < size; i++) {
            start |= mTransitionMap.valueAt(i).startTransition(progress);
        }
        return start;
    }

    /**
     * Updates the transition progress
     *
     * @param value
     */
    @Override
    public void updateProgress(float value) {
        final int size = mTransitionMap.size();
        for (int i = 0; i < size; i++) {
            mTransitionMap.valueAt(i).updateProgress(value);
        }
    }

    /**
     * Stops all transitions
     */
    @Override
    public void stopTransition() {
        //call listeners so they can perform their actions first, like modifying this adapter's transitions
        for (TransitionListener listener : mTransitionListenerSet) {
            listener.onTransitionEnd(this);
        }

        final int size = mTransitionMap.size();
        for (int i = 0; i < size; i++) {
            mTransitionMap.valueAt(i).stopTransition();
        }
    }

    @Override
    public void addTransitionListener(TransitionListener transitionListener) {
        mTransitionListenerSet.add(transitionListener);
    }

    @Override
    public void removeTransitionListener(TransitionListener transitionListener) {
        mTransitionListenerSet.remove(transitionListener);
    }

    @Override
    public void notifyTransitionStart() {
        for (TransitionListener listener : mTransitionListenerSet) {
            listener.onTransitionStart(this);
        }
    }

    @Override
    public void notifyTransitionEnd() {
        for (TransitionListener listener : mTransitionListenerSet) {
            listener.onTransitionEnd(this);
        }
    }
}
