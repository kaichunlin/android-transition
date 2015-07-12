package com.kaichunlin.transition.Animation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.animation.LinearInterpolator;

import com.kaichunlin.transition.IBaseTransition;

/**
 * Created by Kai on 2015/7/12.
 */
public class Animation implements IAnimation, ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private final Runnable mStartAnimation = new Runnable() {
        @Override
        public void run() {
            startAnimation();
        }
    };
    private final IBaseTransition mTransition;
    private ValueAnimator mValueAnimator;
    private Handler mHandler;
    private boolean mReverse;
    private int mDuration = 300;

    public Animation(@NonNull IBaseTransition transition) {
        mTransition = transition;
    }

    protected IBaseTransition getTransition() {
        return mTransition;
    }

    @Override
    public void setAnimationDuration(@IntRange(from = 0) int duration) {
        mDuration = duration;
    }

    @Override
    public int getAnimationDuration() {
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
        startAnimation(mDuration);
    }

    @Override
    public void startAnimation(@IntRange(from = 0) int duration) {
        stopAnimation();

        getTransition().startTransition(mReverse ? 1 : 0);

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

    @Override
    public void endAnimation() {
        if (mValueAnimator != null) {
            mValueAnimator.end();
            mValueAnimator = null;
        }
    }

    @Override
    public void stopAnimation() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            getTransition().stopTransition();
            mValueAnimator = null;
        }
    }

    @Override
    public void resetAnimation() {
        //TODO optimize
        getTransition().startTransition();
        getTransition().updateProgress(mReverse ? 1 : 0);
        getTransition().stopTransition();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        getTransition().updateProgress((Float) animation.getAnimatedValue());
    }

    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        getTransition().stopTransition();
        mValueAnimator = null;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        getTransition().stopTransition();
        mValueAnimator = null;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }
}