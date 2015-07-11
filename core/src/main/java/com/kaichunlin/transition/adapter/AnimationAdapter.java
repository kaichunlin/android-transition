package com.kaichunlin.transition.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.view.animation.LinearInterpolator;

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
public class AnimationAdapter extends BaseAdapter implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener, ITransitionAdapter.TransitionListener {
    final BaseAdapter mAdapter;
    private ValueAnimator mValueAnimator;
    private boolean mReverse;
    private int mDuration = 300;

    public AnimationAdapter() {
        mAdapter = null;
    }

    /**
     * Wraps an existing {@link BaseAdapter) to reuse its onCreateOptionsMenu(...) logic and its transition effects
     *
     * @param adapter
     */
    public AnimationAdapter(@Nullable BaseAdapter adapter) {
        mAdapter = adapter;
    }

    public void setAnimationDuration(@IntRange(from = 0) int duration) {
        mDuration = duration;
    }

    public void setAnimationInReverse(boolean reverse) {
        mReverse = reverse;
    }

    public boolean isAnimationInReverse() {
        return mReverse;
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

    /**
     * Starts the animation with the default duration (300 ms)
     */
    public void startAnimation() {
        startAnimation(mDuration);
    }

    /**
     * Starts the animation with the specified duration
     *
     * @param duration
     */
    public void startAnimation(@IntRange(from = 0) int duration) {
        stopAnimation();

        getAdapter().startTransition(mReverse ? 1 : 0);

        mValueAnimator = new ValueAnimator();
        mValueAnimator.setDuration(duration);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        if (mReverse) {
            mValueAnimator.setFloatValues(1, 0);
        } else {
            mValueAnimator.setFloatValues(0, 1);
        }
        mValueAnimator.addUpdateListener(this);
        mValueAnimator.addListener(this);
        mValueAnimator.start();
    }

    /**
     * Cancels the animation, i.e. the affected Views will retain their last states
     */
    public void cancelAnimation() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
    }

    /**
     * Ends the animation, i.e. the affected Views will jump to their final states
     */
    public void endAnimation() {
        if (mValueAnimator != null) {
            mValueAnimator.end();
            mValueAnimator = null;
        }
    }

    /**
     * Stops the animation, i.e. the affected Views will reverse to their original states
     */
    public void stopAnimation() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            stopTransition();
            mValueAnimator = null;
        }
    }

    /**
     *
     */
    public void resetAnimation() {
        //TODO optimize
        getAdapter().startTransition();
        getAdapter().updateProgress(mReverse ? 1 : 0);
        getAdapter().stopTransition();
    }

    @Override
    public void stopTransition() {
        if (mAdapter == null) {
            super.stopTransition();
        } else {
            mAdapter.stopTransition();
        }
    }

    private ITransitionAdapter getAdapter() {
        return mAdapter == null ? this : mAdapter;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        getAdapter().updateProgress((Float) animation.getAnimatedValue());
    }

    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        stopTransition();
        mValueAnimator = null;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        stopTransition();
        mValueAnimator = null;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }
}
