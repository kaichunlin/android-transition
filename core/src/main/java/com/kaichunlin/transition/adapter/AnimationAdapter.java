package com.kaichunlin.transition.adapter;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.kaichunlin.transition.TransitionManagerListener;
import com.kaichunlin.transition.TransitionManager;
import com.kaichunlin.transition.animation.Animation;
import com.kaichunlin.transition.animation.AnimationListener;
import com.kaichunlin.transition.animation.TransitionAnimation;

/**
 * This adapter integrates traditional animations using the same methods and logic as the rest of the framework. It can be configured
 * to reuse transitions configured for another {@link AbstractAdapter} so transition effects can be easily shared between progress-based
 * adapters and time-based adapters.
 * <br>
 * However it must be noted that when AnimationAdapter reuses transitions configured for another {@link AbstractAdapter}, any changes made
 * to the other {@link AbstractAdapter} may effect AnimationAdapter and cause strange errors. For more complex case, {@link UnifiedAdapter}
 * should be used instead.
 */
@UiThread
public class AnimationAdapter extends AbstractAdapter implements Animation, TransitionManagerListener {
    private final TransitionAdapter mAdapter;
    private Animation mAnimation;

    public AnimationAdapter() {
        this(null);
    }

    /**
     * Wraps an existing {@link AbstractAdapter} to reuse its onCreateOptionsMenu(...) logic and its transition effects
     *
     * @param adapter
     */
    public AnimationAdapter(@Nullable TransitionAdapter adapter) {
        super(adapter == null ? new AdapterState() : adapter.getAdapterState());
        mAdapter = adapter;
        mAnimation = new TransitionAnimation(mAdapter == null ? this : mAdapter);
    }

    @Override
    public void addTransitionListener(TransitionManagerListener listener) {
        super.addTransitionListener(listener);

        if (mAdapter != null) {
            mAdapter.addTransitionListener(this);
        }
    }

    @Override
    public void removeTransitionListener(TransitionManagerListener listener) {
        super.removeTransitionListener(listener);

        if (mAdapter != null) {
            mAdapter.removeTransitionListener(this);
        }
    }

    @Override
    public void addAnimationListener(AnimationListener animationListener) {

    }

    @Override
    public void removeAnimationListener(AnimationListener animationListener) {

    }

    @Override
    public void onTransitionStart(TransitionManager manager) {
        notifyTransitionStart();
    }

    @Override
    public void onTransitionEnd(TransitionManager manager) {
        notifyTransitionEnd();
    }

    @Override
    public void setDuration(@IntRange(from = 0) int duration) {
        mAnimation.setDuration(duration);
    }

    @Override
    public int getDuration() {
        return mAnimation.getDuration();
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

    public boolean isAnimating() {
        return mAnimation.isAnimating();
    }

    @Override
    public void cancelAnimation() {
        mAnimation.cancelAnimation();
    }

    @Override
    public void pauseAnimation() {
        mAnimation.pauseAnimation();
    }

    @Override
    public void resumeAnimation() {
        mAnimation.resumeAnimation();
    }

    @Override
    public void endAnimation() {
        mAnimation.endAnimation();
    }

    @Override
    public void forceEndState() {
        mAnimation.forceEndState();
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

    @Nullable
    protected TransitionAdapter getAdapter() {
        return mAdapter;
    }
}
