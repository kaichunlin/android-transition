package com.kaichunlin.transition;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
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
    protected Set<TransitionListener> transitionListenerList = new HashSet<>();
    protected Map<String, Transition> mTransitionMap = new HashMap<>();

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
        for (Transition trans : mTransitionMap.values()) {
            start |= trans.startTransition();
        }
        return start;
    }

    @Override
    public boolean startTransition(float progress) {
        //call listeners so they can perform their actions first, like modifying this adapter's transitions
        notifyTransitionStart();

        boolean start = false;
        for (Transition trans : mTransitionMap.values()) {
            start |= trans.startTransition(progress);
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
        for (Transition trans : mTransitionMap.values()) {
            trans.updateProgress(value);
        }
    }

    /**
     * Stops all transitions
     */
    @Override
    public void stopTransition() {
        //call listeners so they can perform their actions first, like modifying this adapter's transitions
        for (TransitionListener listener:transitionListenerList) {
            listener.onTransitionEnd(this);
        }

        for (Transition trans : mTransitionMap.values()) {
            trans.stopTransition();
        }
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
    public void notifyTransitionStart() {
        for (TransitionListener listener : transitionListenerList) {
            listener.onTransitionStart(this);
        }
    }

    @Override
    public void notifyTransitionEnd() {
        for (TransitionListener listener : transitionListenerList) {
            listener.onTransitionEnd(this);
        }
    }
}
