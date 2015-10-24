package com.kaichunlin.transition.internal;

import android.support.annotation.CheckResult;

import com.kaichunlin.transition.TransitionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows customized transition behavior by implementing {@link #updateProgress(float)}
 * <p>
 * Created by Kai-Chun Lin on 2015/4/28.
 */
public class CustomTransitionController extends TransitionController<CustomTransitionController> implements Cloneable {
    private List<TransitionHandler> mTransitionHandlerList = new ArrayList<>();

    public CustomTransitionController() {
        super(null);
        updateProgressWidth();
    }

    public void addTransitionHandler(TransitionHandler mTransitionHandler) {
        mTransitionHandlerList.add(mTransitionHandler);
    }

    @Override
    public void updateProgress(float progress) {
        for (int i = 0, count = mTransitionHandlerList.size(); i < count; ++i) {
            mTransitionHandlerList.get(i).onUpdateProgress(this, getTarget(), progress);
        }
    }

    @CheckResult
    @Override
    public CustomTransitionController clone() {
        CustomTransitionController newCopy = (CustomTransitionController) super.clone();
        newCopy.mTransitionHandlerList = new ArrayList<>(mTransitionHandlerList.size());
        newCopy.mTransitionHandlerList.addAll(mTransitionHandlerList);
        return newCopy;
    }

    protected CustomTransitionController self() {
        return this;
    }
}
