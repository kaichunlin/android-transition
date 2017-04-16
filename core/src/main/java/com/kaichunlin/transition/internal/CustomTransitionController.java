package com.kaichunlin.transition.internal;

import android.support.annotation.CheckResult;

import com.kaichunlin.transition.transformer.ViewTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregates a set of {@link ViewTransformer}s.
 */
public class CustomTransitionController extends TransitionController<CustomTransitionController> implements Cloneable {
    private List<ViewTransformer> mViewTransformerList = new ArrayList<>();

    public CustomTransitionController() {
        super(null);
        updateProgressWidth();
    }

    public void addViewTransformer(ViewTransformer mViewTransformer) {
        mViewTransformerList.add(mViewTransformer);
    }

    @Override
    public void updateProgress(float progress) {
        for (int i = 0, count = mViewTransformerList.size(); i < count; ++i) {
            mViewTransformerList.get(i).updateView(this, getTarget(), progress);
        }
    }

    @CheckResult
    @Override
    public CustomTransitionController clone() {
        CustomTransitionController newCopy = (CustomTransitionController) super.clone();
        newCopy.mViewTransformerList = new ArrayList<>(mViewTransformerList.size());
        newCopy.mViewTransformerList.addAll(mViewTransformerList);
        return newCopy;
    }

    protected CustomTransitionController self() {
        return this;
    }
}
