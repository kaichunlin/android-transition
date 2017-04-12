package com.kaichunlin.transition.adapter;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.kaichunlin.transition.AbstractTransitionBuilder;
import com.kaichunlin.transition.Transition;
import com.kaichunlin.transition.TransitionManagerListener;
import com.kaichunlin.transition.TransitionManager;
import com.kaichunlin.transition.animation.Animation;
import com.kaichunlin.transition.animation.AnimationListener;
import com.kaichunlin.transition.animation.AnimationManager;

import java.util.List;

/**
 * Allows the combination of both transition (through {@link TransitionAdapter}) and animation (through {@link AnimationManager}).
 */
@UiThread
public class UnifiedAdapter extends AbstractAdapter implements Animation, TransitionManagerListener {
    private final TransitionAdapter mAdapter;
    private final AnimationManager mAnimationManager;
    private boolean mUpdateProgressAdapter = true;
    private boolean mUpdateAnimationAdapter = true;

    public UnifiedAdapter(@Nullable TransitionAdapter adapter) {
        this(adapter, new AnimationManager());
    }

    /**
     * @param adapter          its methods should not be called elsewhere
     * @param manager its methods should not be called elsewhere
     */
    public UnifiedAdapter(@NonNull TransitionAdapter adapter, @NonNull AnimationManager manager) {
        super(adapter == null ? new AdapterState() : adapter.getAdapterState());

        mAdapter = adapter;
        getAdapter().addTransitionListener(this);

        mAnimationManager = manager;
    }

    @Nullable
    protected TransitionAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void addTransition(@NonNull AbstractTransitionBuilder builder) {
        invalidateTransitions();

        super.addTransition(builder);
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

    public void addTransitionListener(TransitionManagerListener listener) {
        super.addTransitionListener(listener);

        if (mAdapter != null) {
            mAdapter.addTransitionListener(this);
        }
    }

    public void removeTransitionListener(TransitionManagerListener listener) {
        super.removeTransitionListener(listener);

        if (mAdapter != null) {
            mAdapter.removeTransitionListener(this);
        }
    }

    @Override
    public void onTransitionStart(TransitionManager manager) {
        if (mUpdateProgressAdapter) {
            getAdapter().removeAllTransitions();
            getAdapter().addAllTransitions(getTransitions());
            mUpdateProgressAdapter = false;
        }
        notifyTransitionStart();
    }

    @Override
    public void onTransitionEnd(TransitionManager manager) {
        notifyTransitionEnd();
    }

    @Override
    public void addAnimationListener(AnimationListener listener) {
        mAnimationManager.addAnimationListener(listener);
    }

    @Override
    public void removeAnimationListener(AnimationListener listener) {
        mAnimationManager.removeAnimationListener(listener);
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
    public void forceEndState() {
        mAnimationManager.forceEndState();
    }

    @Override
    public void resetAnimation() {
        mAnimationManager.resetAnimation();
    }
}
