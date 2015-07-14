package com.kaichunlin.transition.adapter;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.kaichunlin.transition.BaseTransitionBuilder;
import com.kaichunlin.transition.ITransition;
import com.kaichunlin.transition.ITransitionManager;

import java.util.List;

/**
 * Created by Kai on 2015/7/10.
 */
public class UnifiedAdapter extends AnimationAdapter {
    private final AnimationAdapter mAnimationAdapter;
    private boolean mUpdateProgressAdapter = true;
    private boolean mUpdateAnimationAdapter = true;

    public UnifiedAdapter(@NonNull ITransitionAdapter progressAdapter) {
        this(progressAdapter, new AnimationAdapter());
    }

    /**
     * @param progressAdapter its methods should not be called elsewhere
     * @param animationAdapter its methods should not be called elsewhere
     */
    public UnifiedAdapter(@NonNull ITransitionAdapter progressAdapter, @NonNull AnimationAdapter animationAdapter) {
        super(progressAdapter);
        getAdapter().addTransitionListener(this);
        mAnimationAdapter = animationAdapter;
        mAnimationAdapter.addTransitionListener(this);
    }

    public void setReverseAnimation(boolean reverse) {
        mAnimationAdapter.setReverseAnimation(reverse);
    }

    public boolean isReverseAnimation() {
        return mAnimationAdapter.isReverseAnimation();
    }

    @Override
    public void onStartTransition(ITransitionManager adapter) {
        if (mUpdateProgressAdapter) {
            getAdapter().removeAllTransitions();
            getAdapter().addAllTransitions(getTransitions());
            mUpdateProgressAdapter = false;
        }
        super.onStartTransition(adapter);
    }

    @Override
    public void addTransition(@NonNull BaseTransitionBuilder transitionBuilder) {
        invalidateTransitions();

        super.addTransition(transitionBuilder);
    }

    @Override
    public void addTransition(@NonNull ITransition transition) {
        invalidateTransitions();

        super.addTransition(transition);
    }

    @Override
    public void addAllTransitions(@NonNull List<ITransition> transitionsList) {
        invalidateTransitions();

        super.addAllTransitions(transitionsList);
    }

    @Override
    public boolean removeTransition(@NonNull ITransition transition) {
        invalidateTransitions();

        return super.removeTransition(transition);
    }

    @Override
    public void removeAllTransitions() {
        invalidateTransitions();

        super.removeAllTransitions();
    }

    private void invalidateTransitions() {
        mUpdateProgressAdapter = true;
        mUpdateAnimationAdapter = true;
    }

    @Override
    public boolean startTransition(float progress) {
        if (mUpdateProgressAdapter) {
            getAdapter().removeAllTransitions();
            getAdapter().addAllTransitions(getTransitions());
            mUpdateProgressAdapter = false;
        }
        return getAdapter().startTransition(progress);
    }

    @Override
    public void updateProgress(float value) {
        getAdapter().updateProgress(value);
    }

    @Override
    public void startAnimation() {
        if (mUpdateAnimationAdapter) {
            mAnimationAdapter.removeAllTransitions();
            mAnimationAdapter.addAllTransitions(getTransitions());
            mUpdateAnimationAdapter = false;
        }
        mAnimationAdapter.startAnimation();
    }

    @Override
    public void startAnimation(@IntRange(from = 0) int duration) {
        mAnimationAdapter.startAnimation(duration);
    }

    @Override
    public void resetAnimation() {
        mAnimationAdapter.resetAnimation();
    }
}
