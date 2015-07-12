package com.kaichunlin.transition.internal;

import com.kaichunlin.transition.ITransitionHandler;

/**
 * Allows customized transition behavior by implementing {@link #updateProgress(float)}
 * <p>
 * Created by Kai-Chun Lin on 2015/4/28.
 */
public class CustomTransitionController extends BaseTransitionController implements Cloneable {
private final ITransitionHandler mCustomTransitionHandler;
    public CustomTransitionController(ITransitionHandler customTransitionHandler) {
        super(null);
        updateProgressWidth();
        mCustomTransitionHandler=customTransitionHandler;
    }

    @Override
    public void updateProgress(float progress) {
        mCustomTransitionHandler.onUpdateProgress(this, getTarget(), progress);
    }
}
