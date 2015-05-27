package com.kaichunlin.transition;

import android.view.View;

/**
 * Allows customized transition behavior by implementing {@link #updateProgress(float)}
 * <p/>
 * Created by Kai-Chun Lin on 2015/4/28.
 */
public abstract class CustomTransitionController extends BaseTransitionController implements Cloneable {

    public CustomTransitionController() {
        super(null);
        updateProgressWidth();
    }

    @Override
    public void updateProgress(float progress) {
        updateProgress(getTarget(), progress);
    }

    protected abstract void updateProgress(View target, float progress);

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public float getStart() {
        return mStart;
    }

    @Override
    public float getEnd() {
        return mEnd;
    }
}
