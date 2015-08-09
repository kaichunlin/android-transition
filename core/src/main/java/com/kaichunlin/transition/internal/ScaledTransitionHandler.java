package com.kaichunlin.transition.internal;

import android.view.View;

import com.kaichunlin.transition.TransitionHandler;

/**
 * The progress valued passed to @link {@link #onUpdateScaledProgress(TransitionController, View, float)} has taken the start and
 * end range into consideration. The method will only be called when the progress is within range, and the value passed has been
 * scaled to always be [0..1].
 * <p>
 * Created by Kai on 2015/8/6.
 */
public abstract class ScaledTransitionHandler implements TransitionHandler {
    private float prevProgress = Float.MIN_VALUE;
    private boolean minProgressUpdated;
    private boolean maxProgressUpdated;

    @Override
    public final void onUpdateProgress(TransitionController controller, View target, float progress) {
        int direction = 0; //1 == increasing, -1 == decreasing
        if (prevProgress != Float.MIN_VALUE) {
            direction = progress > prevProgress ? 1 : -1;
        }

        if (direction == -1) {  //decreasing
            if (progress < 0) {
                if (minProgressUpdated) {
                    return;
                }
                minProgressUpdated = true;
                progress = 0;
            }
            maxProgressUpdated = false;
        } else if (direction == 1) { //increasing
            if (progress > 1) {
                if (maxProgressUpdated) {
                    return;
                }
                maxProgressUpdated = true;
                progress = 1;
            }
            minProgressUpdated = false;
        }
        onUpdateScaledProgress(controller, target, progress);
    }

    protected abstract void onUpdateScaledProgress(TransitionController controller, View target, float modifiedProgress);
}
