package com.kaichunlin.transition.animation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.animation.LinearInterpolator;

import com.kaichunlin.transition.TransitionOperation;

/**
 * Created by Kai on 2015/7/12.
 */
public class TransitionAnimation extends AbstractAnimation {
    private final AnimatorStateListener mAnimatorStateListener = new AnimatorStateListener();
    private final TransitionOperation mTransition;
    private ValueAnimator mValueAnimator;
    private boolean mReverse;
    private int mDuration = 300;
    private boolean mReset;

    public TransitionAnimation(@NonNull TransitionOperation transition) {
        mTransition = transition;
    }

    protected TransitionOperation getTransition() {
        return mTransition;
    }

    @Override
    public void setDuration(@IntRange(from = 0) int duration) {
        mDuration = duration;
    }

    @Override
    public int getDuration() {
        return mDuration;
    }

    @Override
    public void setReverseAnimation(boolean reverse) {
        mReverse = reverse;
    }

    @Override
    public boolean isReverseAnimation() {
        return mReverse;
    }

    @Override
    public void startAnimation() {
        startAnimation(new ValueAnimator(), mDuration);
        mValueAnimator.start();
    }

    protected void startAnimation(@NonNull ValueAnimator sharedAnimator) {
        startAnimation(sharedAnimator, -1);
    }

    @Override
    public void startAnimation(@IntRange(from = 0) int duration) {
        startAnimation(new ValueAnimator(), duration);
        mValueAnimator.start();
    }

    protected void startAnimation(@NonNull ValueAnimator sharedAnimator, @IntRange(from = 0) int duration) {
        if (isAnimating()) {
            return;
        }
        setAnimating(true);
        getTransition().startTransition(mReverse ? 1 : 0);

        mValueAnimator = sharedAnimator;
        if(duration!=-1) {
            mValueAnimator.setDuration(duration);
        }
        mValueAnimator.setInterpolator(new LinearInterpolator());
        if (mReverse) {
            mValueAnimator.setFloatValues(1, 0);
        } else {
            mValueAnimator.setFloatValues(0, 1);
        }
        mValueAnimator.addUpdateListener(mAnimatorStateListener);
        mValueAnimator.addListener(mAnimatorStateListener);

        mReset = false;
    }

    public void startAnimationDelayed(@IntRange(from = 0) int delay) {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.postDelayed(mStartAnimation, delay);
    }

    public void startAnimationDelayed(@IntRange(from = 0) final int duration, @IntRange(from = 0) int delay) {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation(duration);
            }
        }, delay);
    }

    @Override
    public void cancelAnimation() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
    }

    @TargetApi(19)
    @Override
    public void pauseAnimation() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        if (mValueAnimator != null) {
            mValueAnimator.pause();
        }
    }

    @TargetApi(19)
    @Override
    public void resumeAnimation() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        if (mValueAnimator != null) {
            mValueAnimator.resume();
        }
    }

    @Override
    public void endAnimation() {
        if (mValueAnimator != null) {
            mValueAnimator.end();
            mValueAnimator = null;
        }
    }

    @Override
    public void resetAnimation() {
        mReset = true;
        notifyAnimationReset();
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
        //TODO optimize
        getTransition().startTransition();
        getTransition().updateProgress(mReverse ? 1 : 0);
        getTransition().stopTransition();
    }

    private class AnimatorStateListener implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            getTransition().updateProgress((Float) animation.getAnimatedValue());
        }

        @Override
        public void onAnimationStart(Animator animation) {
            notifyAnimationStart();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (mReset) {
                return;
            }
            setAnimating(false);
            notifyAnimationEnd();
            getTransition().stopTransition();
            mValueAnimator = null;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            if (mReset) {
                notifyAnimationReset();
            } else {
                notifyAnimationCancel();
            }
            setAnimating(false);
            getTransition().stopTransition();
            mValueAnimator = null;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }
}