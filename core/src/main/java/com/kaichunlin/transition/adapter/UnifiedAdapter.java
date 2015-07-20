package com.kaichunlin.transition.adapter;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kaichunlin.transition.AbstractTransitionBuilder;
import com.kaichunlin.transition.animation.Animation;
import com.kaichunlin.transition.animation.AnimationListener;
import com.kaichunlin.transition.animation.AnimationManager;
import com.kaichunlin.transition.Transition;
import com.kaichunlin.transition.TransitionListener;
import com.kaichunlin.transition.TransitionManager;

import java.util.List;

/**
 * Created by Kai on 2015/7/10.
 */
public class UnifiedAdapter extends AbstractAdapter implements Animation, TransitionListener {
    private final TransitionAdapter mAdapter;
    private final AnimationManager mAnimationManager;
    private boolean mUpdateProgressAdapter = true;
    private boolean mUpdateAnimationAdapter = true;

    public UnifiedAdapter(@Nullable TransitionAdapter adapter) {
        this(adapter, new AnimationManager());
    }

    /**
     * @param adapter          its methods should not be called elsewhere
     * @param animationManager its methods should not be called elsewhere
     */
    public UnifiedAdapter(@NonNull TransitionAdapter adapter, @NonNull AnimationManager animationManager) {
        super(adapter == null ? new AdapterState() : adapter.getAdapterState());

        mAdapter = adapter;
        getAdapter().addTransitionListener(this);

        mAnimationManager = animationManager;
    }

    @Nullable
    protected TransitionAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void addTransition(@NonNull AbstractTransitionBuilder transitionBuilder) {
        invalidateTransitions();

        super.addTransition(transitionBuilder);
    }

    @Override
    public void addTransition(@NonNull Transition transition) {
        invalidateTransitions();

        super.addTransition(transition);
    }

    @Override
    public void addAllTransitions(@NonNull List<Transition> transitionsList) {
        invalidateTransitions();

        super.addAllTransitions(transitionsList);
    }

    @Override
    public boolean removeTransition(@NonNull Transition transition) {
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
        cancelAnimation();
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
    public void stopTransition() {
        if (mAdapter == null) {
            super.stopTransition();
        } else {
            mAdapter.stopTransition();
        }
    }

    public void addTransitionListener(TransitionListener transitionListener) {
        super.addTransitionListener(transitionListener);

        if (mAdapter != null) {
            mAdapter.addTransitionListener(this);
        }
    }

    public void removeTransitionListener(TransitionListener transitionListener) {
        super.removeTransitionListener(transitionListener);

        if (mAdapter != null) {
            mAdapter.removeTransitionListener(this);
        }
    }

    @Override
    public void onTransitionStart(TransitionManager transitionManager) {
        if (mUpdateProgressAdapter) {
            getAdapter().removeAllTransitions();
            getAdapter().addAllTransitions(getTransitions());
            mUpdateProgressAdapter = false;
        }
        notifyTransitionStart();
    }

    @Override
    public void onTransitionEnd(TransitionManager transitionManager) {
        notifyTransitionEnd();
    }

    @Override
    public void addAnimationListener(AnimationListener animationListener) {
        mAnimationManager.addAnimationListener(animationListener);
    }

    @Override
    public void removeAnimationListener(AnimationListener animationListener) {
        mAnimationManager.removeAnimationListener(animationListener);
    }

    @Override
    public void setDuration(@IntRange(from = 0) int duration) {
        mAnimationManager.setDuration(duration);
    }

    @Override
    public int getDuration() {
        return mAnimationManager.getDuration();
    }

    @Override
    public void setReverseAnimation(boolean reverse) {
        mAnimationManager.setReverseAnimation(reverse);
    }

    @Override
    public boolean isReverseAnimation() {
        return mAnimationManager.isReverseAnimation();
    }

    private void startAnimation(boolean setDuration, int duration) {
        cancelAnimation();
        if (mUpdateAnimationAdapter) {
            mAnimationManager.removeAllAnimations();
            mAnimationManager.addAllTransitions(getTransitions());
            mUpdateAnimationAdapter = false;
        }
        if(setDuration) {
            mAnimationManager.startAnimation(duration);
        } else {
            mAnimationManager.startAnimation();
        }
    }

    @Override
    public void startAnimation() {
        startAnimation(false, -1);
    }

    @Override
    public void startAnimation(@IntRange(from = 0) int duration) {
        startAnimation(true, duration);
    }

    public void startAnimationDelayed(@IntRange(from = 0) int delay) {
        mAnimationManager.startAnimationDelayed(delay);
    }

    public void startAnimationDelayed(@IntRange(from = 0) final int duration, @IntRange(from = 0) int delay) {
        mAnimationManager.startAnimationDelayed(duration, delay);
    }

    public boolean isAnimating() {
        return mAnimationManager.isAnimating();
    }

    @Override
    public void cancelAnimation() {
        mAnimationManager.cancelAnimation();
    }

    @Override
    public void pauseAnimation() {
        mAnimationManager.pauseAnimation();
    }

    @Override
    public void resumeAnimation() {
        mAnimationManager.resumeAnimation();
    }

    @Override
    public void endAnimation() {
        mAnimationManager.endAnimation();
    }

    @Override
    public void resetAnimation() {
        mAnimationManager.resetAnimation();
    }
}
