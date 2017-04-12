package com.kaichunlin.transition.internal.debug;

import android.util.Log;

import com.kaichunlin.transition.TransitionManager;
import com.kaichunlin.transition.TransitionConfig;
import com.kaichunlin.transition.TransitionManagerListener;

/**
 * Uses Log.i to output the start and end of a transition.
 */
public class TraceTransitionManagerListener implements TransitionManagerListener {
    private long mStart;

    @Override
    public void onTransitionStart(TransitionManager manager) {
        mStart = System.currentTimeMillis();
        if (TransitionConfig.isDebug()) {
            Log.i(getClass().getSimpleName(), "onTransitionStart: " + manager);
        }
    }

    @Override
    public void onTransitionEnd(TransitionManager manager) {
        if (TransitionConfig.isDebug()) {
            Log.i(getClass().getSimpleName(), "onTransitionEnd, duration=" + (System.currentTimeMillis() - mStart) + ": " + manager);
        }
    }
}
