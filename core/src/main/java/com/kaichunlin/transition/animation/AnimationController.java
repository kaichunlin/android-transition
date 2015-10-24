package com.kaichunlin.transition.animation;

import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.kaichunlin.transition.TransitionOperation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * For some reason changing the layout with ValueAnimator is very slow, so this is the default
 * {@link StateController} used by {@link AnimationManager} and {@link TransitionAnimation} to
 * advance animation state.
 * <p>
 * Created by Kai on 2015/8/7.
 */
class AnimationController extends android.view.animation.Animation implements StateController, android.view.animation.Animation.AnimationListener {
    private static final int STOP_END = 0x01;
    private static final int STOP_CANCEL = 0x02;
    private static final int STOP_RESET = 0x04;
    private final List<AbstractAnimation> mAnimationList = new ArrayList<>();
    private WeakReference<View> mTargetRef;
    private boolean mReverse;
    private List<TransitionOperation> mTransitionList;
    private int mStopType;
    private boolean mEnded;

    AnimationController(View target, boolean reverse, final TransitionOperation transition) {
        this(target, reverse, new ArrayList<TransitionOperation>() {{
            add(transition);
        }});
    }

    AnimationController(View target, boolean reverse, List<TransitionOperation> transitionList) {
        mTargetRef = new WeakReference<>(target);
        mReverse = reverse;
        mTransitionList = transitionList;
        setAnimationListener(this);
        setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        if (mStopType != 0) {
            return;
        }
        for (int i = 0; i < mTransitionList.size(); i++) {
            mTransitionList.get(i).updateProgress(mReverse ? 1 - interpolatedTime : interpolatedTime);
        }
    }

    @Override
    public void addAnimation(AbstractAnimation animation) {
        mAnimationList.add(animation);
    }

    @Override
    public void setAnimationDuration(long duration) {
        setDuration(duration);
    }

    @Override
    public void startController() {
        View target = mTargetRef.get();
        if (target == null) {
            return;
        }
        mStopType = 0;
        mEnded = false;
        target.startAnimation(this);
    }

    /**
     * Currently not suppported
     */
    @Override
    public void pauseController() {
        //TODO currently does nothing
    }

    /**
     * Currently not suppported
     */
    @Override
    public void resumeController() {
        //TODO currently does nothing
    }

    @Override
    public void endController() {
        handleStop(STOP_END);
        AbstractAnimation ani;
        for (int i = 0, size = mAnimationList.size(); i < size; i++) {
            ani = mAnimationList.get(i);
            ani.getTransition().updateProgress(ani.isReverseAnimation() ? 0 : 1);
        }
    }

    @Override
    public void resetController() {
        handleStop(STOP_RESET);
        AbstractAnimation ani;
        for (int i = 0, size = mAnimationList.size(); i < size; i++) {
            ani = mAnimationList.get(i);
            ani.getTransition().updateProgress(ani.isReverseAnimation() ? 1 : 0);
        }
    }

    public void cancelController() {
        handleStop(STOP_CANCEL);
    }

    private void handleStop(int stopType) {
        this.mStopType = stopType;
        View target = mTargetRef.get();
        if (target == null) {
            return;
        }
        target.clearAnimation();
    }

    @Override
    public void onAnimationStart(android.view.animation.Animation animation) {
        for (int i = 0, size = mAnimationList.size(); i < size; i++) {
            mAnimationList.get(i).notifyAnimationStart();
        }
    }

    @Override
    public void onAnimationEnd(android.view.animation.Animation animation) {
        if (mEnded) {
            return;
        }
        if (mStopType == 0) {
            mStopType = STOP_END;
        }
        AbstractAnimation ani;
        for (int i = 0, size = mAnimationList.size(); i < size; i++) {
            ani = mAnimationList.get(i);
            ani.setAnimating(false);
            switch (mStopType) {
                case STOP_END:
                    ani.notifyAnimationEnd();
                    break;
                case STOP_CANCEL:
                    ani.notifyAnimationCancel();
                    break;
                case STOP_RESET:
                    ani.notifyAnimationReset();
                    break;
            }
            ani.getTransition().stopTransition();
        }
        mEnded = true;
    }

    @Override
    public void onAnimationRepeat(android.view.animation.Animation animation) {
    }
}
