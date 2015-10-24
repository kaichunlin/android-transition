package com.kaichunlin.transition.animation;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kai on 2015/8/7.
 */
class AnimatorController extends ValueAnimator implements StateController, ValueAnimator.AnimatorUpdateListener, android.animation.Animator.AnimatorListener {
    private final List<AbstractAnimation> mAnimationList = new ArrayList<>();
    private boolean mReset;
    private boolean mCancel;

    AnimatorController(boolean reverse) {
        setInterpolator(new LinearInterpolator());
        if (reverse) {
            setFloatValues(1, 0);
        } else {
            setFloatValues(0, 1);
        }
        addUpdateListener(this);
        addListener(this);
    }

    @Override
    public void addAnimation(AbstractAnimation animatorStateListener) {
        mAnimationList.add(animatorStateListener);
    }

    @Override
    public void setAnimationDuration(long duration) {
        setDuration(duration);
    }

    @Override
    public void startController() {
        start();
        mReset = false;
        mCancel = false;
    }

    @TargetApi(19)
    @Override
    public void pauseController() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        pause();
    }

    @TargetApi(19)
    @Override
    public void resumeController() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        resume();
    }

    @Override
    public void endController() {
        end();
    }

    @Override
    public void resetController() {
        mReset = true;
        AbstractAnimation ani;
        for (int i = 0, size = mAnimationList.size(); i < size; i++) {
            ani = mAnimationList.get(i);
            ani.notifyAnimationReset();
        }
        cancel();

        //TODO optimize
        for (int i = 0, size = mAnimationList.size(); i < size; i++) {
            ani = mAnimationList.get(i);
            ani.getTransition().startTransition();
            ani.getTransition().updateProgress(ani.isReverseAnimation() ? 1 : 0);
            ani.getTransition().stopTransition();
        }
    }

    @Override
    public void cancelController() {
        cancel();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        AbstractAnimation ani;
        float progress = (Float) animation.getAnimatedValue();
        for (int i = 0, size = mAnimationList.size(); i < size; i++) {
            ani = mAnimationList.get(i);
            ani.getTransition().updateProgress(progress);
        }
    }

    @Override
    public void onAnimationStart(android.animation.Animator animation) {
        for (int i = 0, size = mAnimationList.size(); i < size; i++) {
            mAnimationList.get(i).notifyAnimationStart();
        }
    }

    @Override
    public void onAnimationEnd(android.animation.Animator animation) {
        if (mReset || mCancel) {
            return;
        }

        AbstractAnimation ani;
        for (int i = 0, size = mAnimationList.size(); i < size; i++) {
            ani = mAnimationList.get(i);
            ani.setAnimating(false);
            ani.notifyAnimationEnd();
            ani.getTransition().stopTransition();
        }
    }

    @Override
    public void onAnimationCancel(android.animation.Animator animation) {
        AbstractAnimation ani;
        for (int i = 0, size = mAnimationList.size(); i < size; i++) {
            ani = mAnimationList.get(i);
            if (mReset) {
                ani.notifyAnimationReset();
            } else {
                ani.notifyAnimationCancel();
            }
            ani.setAnimating(false);
            ani.getTransition().stopTransition();
        }
        mCancel = true;
    }

    @Override
    public void onAnimationRepeat(android.animation.Animator animation) {
    }
}
