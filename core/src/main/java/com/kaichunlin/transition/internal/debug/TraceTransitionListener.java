package com.kaichunlin.transition.internal.debug;

import android.util.Log;

import com.kaichunlin.transition.TransitionManager;
import com.kaichunlin.transition.TransitionConfig;
import com.kaichunlin.transition.TransitionListener;

/**
 * Uses Log.i to output the start and end of a transition.
 */
public class TraceTransitionListener implements TransitionListener {
    private long mStart;

    @Override
    public void onTransitionStart(TransitionManager transitionManager) {
        mStart = System.currentTimeMillis();
        if (TransitionConfig.isDebug()) {
            Log.i(getClass().getSimpleName(), "onTransitionStart: " + transitionManager);
        }
    }

    @Override
    public void onTransitionEnd(TransitionManager transitionManager) {
        if (TransitionConfig.isDebug()) {
            Log.i(getClass().getSimpleName(), "onTransitionEnd, duration=" + (System.currentTimeMillis() - mStart) + ": " + transitionManager);
        }
    }
}
