package com.kaichunlin.transition.transformer;

import android.view.View;

import com.kaichunlin.transition.internal.TransitionController;

/**
 * The progress valued passed to @link {@link #updateView(TransitionController, View, float)} has taken the start and end range
 * into consideration. The method will only be called when the progress is within range, and the value passed has been scaled to be
 * always between 0f to 1f.
 */
public abstract class ScaledTransformer implements ViewTransformer {
    private final boolean updateOnceOutsideRange;
    private boolean updateMinProgress = true;
    private boolean updateMaxProgress = true;

    public ScaledTransformer() {
        this(false);
    }

    public ScaledTransformer(boolean updateOnceOutsideRange) {
        this.updateOnceOutsideRange = updateOnceOutsideRange;
    }

    @Override
    public final void updateView(TransitionController controller, View target, float progress) {
        final float start = controller.getStart();
        final float end = controller.getEnd();
        if (progress < start) {
            if (updateOnceOutsideRange && updateMinProgress) {
                updateViewScaled(controller, target, 0);
            }
            updateMinProgress = false;
            return;
        }
        updateMinProgress = true;
        if (progress > end) {
            if (updateOnceOutsideRange && updateMaxProgress) {
                updateViewScaled(controller, target, 1);
            }
            updateMaxProgress = false;
            return;
        }
        updateMaxProgress = true;
        updateViewScaled(controller, target, (progress - start) / (end - start));
    }

    /**
     * @param controller
     * @param target
     * @param scaledProgress
     */
    protected abstract void updateViewScaled(TransitionController controller, View target, float scaledProgress);
}
