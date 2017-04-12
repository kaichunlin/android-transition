package com.kaichunlin.transition.transformer;

import android.view.View;

import com.kaichunlin.transition.internal.TransitionController;

/**
 * A generic interface providing arbitrary transformation to a View.
 */
public interface ViewTransformer {
    /**
     *
     * @param controller
     * @param target
     * @param progress
     */
    void updateView(TransitionController controller, View target, float progress);
}
