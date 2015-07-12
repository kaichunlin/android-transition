package com.kaichunlin.transition;

import android.view.View;

import com.kaichunlin.transition.internal.ITransitionController;

/**
 * Created by Kai on 2015/7/12.
 */
public interface ITransitionHandler {
    void onUpdateProgress(ITransitionController controller, View target, float progress);
}
