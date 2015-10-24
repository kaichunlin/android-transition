package com.kaichunlin.transition;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manages a collection of {@link Transition}
 * <p>
 * Created by Kai on 2015/7/14.
 */
public class DefaultTransitionManager implements TransitionManager {
    private ArrayList<TransitionListener> mTransitionListenerList = new ArrayList<>();
    private ArrayList<Transition> mTransitionList = new ArrayList<>();
    private Set<Transition> mBackupTransitionList = new HashSet<>();

    @Override
    public void addTransition(@NonNull AbstractTransitionBuilder transitionBuilder) {
        addTransition(transitionBuilder.build());
    }

    @Override
    public void addTransition(@NonNull Transition transition) {
        processAnimation(transition);
    }

    @Override
    public void addAllTransitions(@NonNull List<Transition> transitionsList) {
        for (int i = 0, size = transitionsList.size(); i < size; i++) {
            addTransition(transitionsList.get(i));
        }
    }

    private void processAnimation(Transition transition) {
        //attempt to merge an animation
        boolean merged = false;
        //no optimization is taken if the TransitionOption is not an AbstractTransition subclass
        if (transition instanceof AbstractTransition) {
            TransitionOperation to;
            for (int i = 0, size = mTransitionList.size(); i < size; i++) {
                to = mTransitionList.get(i);
                if (to instanceof AbstractTransition) {
                    AbstractTransition atTo = (AbstractTransition) to;
                    AbstractTransition atTransition = (AbstractTransition) transition;
                    if (atTo.compatible(atTransition)) {
                        //the Transition has already merged another Transition previously
                        if (atTo.hasMultipleSetup()) {
                            atTo.merge(atTransition);
                        } else { //the Transition has not merged another Transition
                            mTransitionList.remove(atTo);
                            atTo = atTo.clone();
                            atTo.merge(atTransition);
                            mTransitionList.add(atTo);
                        }
                        break;
                    }
                }
            }
        }
        if (!merged) {
            mTransitionList.add(transition);
        }
        mBackupTransitionList.add(transition);
    }

    @Override
    public boolean removeTransition(@NonNull Transition transition) {
        //rebuilding mTransitionList
        if (mBackupTransitionList.remove(transition)) {
            List<Transition> temp = new ArrayList<>(mBackupTransitionList.size());
            temp.addAll(mBackupTransitionList);
            mTransitionList.clear();
            mBackupTransitionList.clear();

            for (int i = 0, size = temp.size(); i < size; i++) {
                processAnimation(temp.get(i));
            }

            return true;
        }
        return false;
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
        for (int i = 0, size = mTransitionListenerList.size(); i < size; i++) {
            mTransitionListenerList.get(i).onTransitionEnd(this);
        }

        for (int i = 0, size = mTransitionList.size(); i < size; i++) {
            mTransitionList.get(i).stopTransition();
        }
    }

    @Override
    public void addTransitionListener(TransitionListener transitionListener) {
        if (!mTransitionListenerList.contains(transitionListener)) {
            mTransitionListenerList.add(transitionListener);
        }
    }

    @Override
    public void removeTransitionListener(TransitionListener transitionListener) {
        mTransitionListenerList.remove(transitionListener);
    }

    @Override
    public void notifyTransitionStart() {
        for (int i = 0, size = mTransitionListenerList.size(); i < size; i++) {
            mTransitionListenerList.get(i).onTransitionStart(this);
        }
    }

    @Override
    public void notifyTransitionEnd() {
        for (int i = 0, size = mTransitionListenerList.size(); i < size; i++) {
            mTransitionListenerList.get(i).onTransitionEnd(this);
        }
    }
}
