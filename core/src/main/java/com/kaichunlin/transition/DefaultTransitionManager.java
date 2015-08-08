package com.kaichunlin.transition;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of {@link Transition}
 * <p>
 * Created by Kai on 2015/7/14.
 */
public class DefaultTransitionManager implements TransitionManager {
    protected ArrayList<TransitionListener> mTransitionListenerList = new ArrayList<>();
    protected ArrayList<Transition> mTransitionList = new ArrayList<>();

    @Override
    public void addTransition(@NonNull AbstractTransitionBuilder transitionBuilder) {
        addTransition(transitionBuilder.build());
    }

    @Override
    public void addTransition(@NonNull Transition transition) {
        if (!mTransitionList.contains(transition)) {
            mTransitionList.add(transition);
        }
    }

    @Override
    public void addAllTransitions(@NonNull List<Transition> transitionsList) {
        final int size = transitionsList.size();
        for (int i = 0; i < size; i++) {
            addTransition(transitionsList.get(i));
        }
    }

    @Override
    public boolean removeTransition(@NonNull Transition transition) {
        return mTransitionList.remove(transition);
    }

    @Override
    public void removeAllTransitions() {
        mTransitionList.clear();
    }

    @Override
    public List<Transition> getTransitions() {
        return mTransitionList;
    }

    @Override
    public boolean startTransition() {
        //call listeners so they can perform their actions first, like modifying this adapter's transitions
        notifyTransitionStart();

        boolean start = false;
        final int size = mTransitionList.size();
        for (int i = 0; i < size; i++) {
            start |= mTransitionList.get(i).startTransition();
        }
        return start;
    }

    @Override
    public boolean startTransition(float progress) {
        //call listeners so they can perform their actions first, like modifying this adapter's transitions
        notifyTransitionStart();

        boolean start = false;
        final int size = mTransitionList.size();
        for (int i = 0; i < size; i++) {
            start |= mTransitionList.get(i).startTransition(progress);
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
        final int size = mTransitionList.size();
        for (int i = 0; i < size; i++) {
            mTransitionList.get(i).updateProgress(value);
        }
    }

    /**
     * Stops all transitions
     */
    @Override
    public void stopTransition() {
        //call listeners so they can perform their actions first, like modifying this adapter's transitions
        int size = mTransitionListenerList.size();
        for (int i = 0; i < size; i++) {
            mTransitionListenerList.get(i).onTransitionEnd(this);
        }

        size = mTransitionList.size();
        for (int i = 0; i < size; i++) {
            mTransitionList.get(i).stopTransition();
        }
    }

    @Override
    public void addTransitionListener(TransitionListener transitionListener) {
        if(!mTransitionListenerList.contains(transitionListener)) {
            mTransitionListenerList.add(transitionListener);
        }
    }

    @Override
    public void removeTransitionListener(TransitionListener transitionListener) {
        mTransitionListenerList.remove(transitionListener);
    }

    @Override
    public void notifyTransitionStart() {
        final int size = mTransitionListenerList.size();
        for (int i = 0; i < size; i++) {
            mTransitionListenerList.get(i).onTransitionStart(this);
        }
    }

    @Override
    public void notifyTransitionEnd() {
        final int size = mTransitionListenerList.size();
        for (int i = 0; i < size; i++) {
            mTransitionListenerList.get(i).onTransitionEnd(this);
        }
    }
}
