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

    @Override
    public final void onUpdateProgress(TransitionController controller, View target, float progress) {
        float start = controller.getStart();
        float end = controller.getEnd();
        if (progress < start || progress > end) {
            return;
        }
        float duration = end - start;
        onUpdateScaledProgress(controller, target, (progress - start) / duration);
    }

    protected abstract void onUpdateScaledProgress(TransitionController controller, View target, float modifiedProgress);
}
