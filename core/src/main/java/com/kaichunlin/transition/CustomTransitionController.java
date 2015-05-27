package com.kaichunlin.transition;

/**
 * Allows customized transition behavior by implementing {@link #updateProgress(float)}
 * <p>
 * Created by Kai-Chun Lin on 2015/4/28.
 */
public abstract class CustomTransitionController extends BaseTransitionController implements Cloneable {

    public CustomTransitionController() {
        super(null);
        updateProgressWidth();
    }

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
