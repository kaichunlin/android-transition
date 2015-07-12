package com.kaichunlin.transition.internal;

import android.support.annotation.CheckResult;

import com.kaichunlin.transition.ITransitionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows customized transition behavior by implementing {@link #updateProgress(float)}
 * <p>
 * Created by Kai-Chun Lin on 2015/4/28.
 */
public class CustomTransitionController extends BaseTransitionController implements Cloneable {
    private List<ITransitionHandler> mTransitionHandlerList = new ArrayList<>();

    public CustomTransitionController() {
        super(null);
        updateProgressWidth();
    }

    public void addTransitionHandler(ITransitionHandler mTransitionHandler) {
        mTransitionHandlerList.add(mTransitionHandler);
    }

    @Override
    public void updateProgress(float progress) {
        for (ITransitionHandler handler : mTransitionHandlerList) {
            handler.onUpdateProgress(this, getTarget(), progress);
        }
    }

    @CheckResult
    @Override
    public CustomTransitionController clone() {
        CustomTransitionController newCopy = (CustomTransitionController) super.clone();
        newCopy.mTransitionHandlerList = new ArrayList<>();
        newCopy.mTransitionHandlerList.addAll(mTransitionHandlerList);
        return newCopy;
    }
}
