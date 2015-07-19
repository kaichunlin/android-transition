package com.kaichunlin.transition;

import android.view.View;

import com.kaichunlin.transition.internal.TransitionController;

/**
 * Created by Kai on 2015/7/12.
 */
public interface TransitionHandler {
    void onUpdateProgress(TransitionController controller, View target, float progress);
}
