package com.kaichunlin.transition.adapter;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

import com.kaichunlin.transition.Animation;
import com.kaichunlin.transition.IAnimation;

/**
 * This adapter integrates traditional animations using the same methods and logic as the rest of the framework. It can be configured
 * to reuse transitions configured for another {@link BaseAdapter} so transition effects can be easily shared between progress-based
 * adapters and time-based adapters.
 * <br>
 * However it must be noted that when AnimationAdapter reuses transitions configured for another {@link BaseAdapter}, any changes made
 * to the other {@link BaseAdapter} may effect AnimationAdapter and cause strange errors. For more complex case, {@link UnifiedAdapter}
 * should be used instead.
 * <p>
 * Created by Kai on 2015/7/10.
 */
public class AnimationAdapter extends BaseAdapter implements IAnimation, ITransitionAdapter.TransitionListener {
    private final BaseAdapter mAdapter;
    private IAnimation mAnimation;

    public AnimationAdapter() {
        this(null);
    }

    /**
     * Wraps an existing {@link BaseAdapter) to reuse its onCreateOptionsMenu(...) logic and its transition effects
     *
     * @param adapter
     */
    public AnimationAdapter(@Nullable BaseAdapter adapter) {
        mAdapter = adapter;
        mAnimation=new Animation(getAdapter());
    }

    @Override
    public void addTransitionListener(TransitionListener transitionListener) {
        super.addTransitionListener(transitionListener);

        if (mAdapter != null) {
            mAdapter.addTransitionListener(this);
        }
    }

    @Override
    public void removeTransitionListener(TransitionListener transitionListener) {
        super.removeTransitionListener(transitionListener);

        if (mAdapter != null) {
            mAdapter.removeTransitionListener(this);
        }
    }

    @Override
    public void onStartTransition(ITransitionAdapter adapter) {
        notifyStartTransition();
    }

    @Override
    public void onStopTransition(ITransitionAdapter adapter) {
        notifyStopTransition();
    }

    @Override
    public void setAnimationDuration(@IntRange(from = 0) int duration) {
        mAnimation.setAnimationDuration(duration);
    }

    @Override
    public int getAnimationDuration() {
        return mAnimation.getAnimationDuration();
    }

    @Override
    public void setReverseAnimation(boolean reverse) {
        mAnimation.setReverseAnimation(reverse);
    }

    @Override
    public boolean isReverseAnimation() {
        return mAnimation.isReverseAnimation();
    }

    @Override
    public void startAnimation() {
        mAnimation.startAnimation();
    }

    @Override
    public void startAnimation(@IntRange(from = 0) int duration) {
        mAnimation.startAnimation(duration);
    }

    public void startAnimationDelayed(@IntRange(from = 0) int delay) {
        mAnimation.startAnimationDelayed(delay);
    }

    public void startAnimationDelayed(@IntRange(from = 0) final int duration, @IntRange(from = 0) int delay) {
        mAnimation.startAnimationDelayed(duration, delay);
    }

    @Override
    public void cancelAnimation() {
        mAnimation.cancelAnimation();
    }

    @Override
    public void endAnimation() {
        mAnimation.endAnimation();
    }

    @Override
    public void stopAnimation() {
        mAnimation.stopAnimation();
    }

    @Override
    public void resetAnimation() {
        mAnimation.resetAnimation();
    }

    @Override
    public void stopTransition() {
        if (mAdapter == null) {
            super.stopTransition();
        } else {
            mAdapter.stopTransition();
        }
    }

    protected ITransitionAdapter getAdapter() {
        return mAdapter == null ? this : mAdapter;
    }
}
