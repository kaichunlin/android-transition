package com.kaichunlin.transition.internal.debug;

import com.kaichunlin.transition.animation.Animation;
import com.kaichunlin.transition.animation.AnimationListener;
import com.kaichunlin.transition.TransitionConfig;

/**
 * Created by Kai on 2015/7/18.
 */
public class TraceAnimationListener implements AnimationListener {
    private long mStart;

    @Override
    public void onAnimationStart(Animation animationManager) {
        mStart = System.currentTimeMillis();
        if (TransitionConfig.isDebug()) {
            try {
                throw new IllegalStateException("Tracer: onAnimationStart: " + animationManager);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAnimationEnd(Animation animationManager) {
        if (TransitionConfig.isDebug()) {
            try {
                throw new IllegalStateException("Tracer: onAnimationEnd, duration=" + (System.currentTimeMillis() - mStart) + ": " + animationManager);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAnimationCancel(Animation animationManager) {
        if (TransitionConfig.isDebug()) {
            try {
                throw new IllegalStateException("Tracer: onAnimationCancel, duration=" + (System.currentTimeMillis() - mStart) + ": " + animationManager);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAnimationReset(Animation animationManager) {
        if (TransitionConfig.isDebug()) {
            try {
                throw new IllegalStateException("Tracer: onAnimationReset, duration=" + (System.currentTimeMillis() - mStart) + ": " + animationManager);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }
}
