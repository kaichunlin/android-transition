package com.kaichunlin.transition;

import android.view.View;

import com.kaichunlin.transition.internal.TransitionController;

/**
 */
public interface TransitionHandler {
    void onUpdateProgress(TransitionController controller, View target, float progress);
}
