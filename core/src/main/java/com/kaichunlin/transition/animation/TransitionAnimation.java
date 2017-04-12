package com.kaichunlin.transition.animation;

import android.os.Build;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.view.View;

import com.kaichunlin.transition.AbstractTransition;
import com.kaichunlin.transition.TransitionOperation;

/**
 */
@UiThread
public class TransitionAnimation extends AbstractAnimation {
    private StateController mController;
    private boolean mReverse;
    private int mDuration = 300;

    public TransitionAnimation(@NonNull TransitionOperation transition) {
        super(transition);

        addAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animationManager) {

            }

            @Override
            public void onAnimationEnd(Animation animationManager) {
                getTransition().stopTransition();
            }

            @Override
            public void onAnimationCancel(Animation animationManager) {
                getTransition().stopTransition();
            }

            @Override
            public void onAnimationReset(Animation animationManager) {
                getTransition().stopTransition();
            }
        });
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
        startAnimation(mDuration);
    }

    @Override
    public void startAnimation(@IntRange(from = 0) final int duration) {
        if (getStateControllerType() == CONTROLLER_ANIMATION) {
            View target = ((AbstractTransition) getTransition()).getTarget();
            if (target == null) {
                setStateControllerType(CONTROLLER_ANIMATOR);
                startAnimation(duration);
                return;
            }
            mController = new AnimationController(target, mReverse, getTransition());
        } else if (getStateControllerType() == CONTROLLER_ANIMATOR) {
            mController = new AnimatorController(isReverseAnimation());
        }
        prepareAnimation(mController, duration);
        mController.startController();
    }

    /**
     * Called before {@link StateController#startController()} to configure the animation.
     *
     * @param sharedController A shared {@link StateController} to reduce resource usage.
     * @param duration Duration of the animation, -1 if sharedController's duration should not be overridden.
     */
    protected void prepareAnimation(@NonNull StateController sharedController, @IntRange(from = -1) final int duration) {
        if (isAnimating()) {
            return;
        }
        setAnimating(true);
        getTransition().startTransition(mReverse ? 1 : 0);

        mController = sharedController;
        if (duration != -1) {
            mController.setAnimationDuration(duration);
        }
        mController.addAnimation(this);
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
        if (mController != null) {
            mController.cancelController();
            mController = null;
        }
    }

    @Override
    public void pauseAnimation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (mController != null) {
            mController.pauseController();
        }
    }

    @Override
    public void resumeAnimation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (mController != null) {
            mController.resumeController();
        }
    }

    @Override
    public void endAnimation() {
        if (mController != null) {
            mController.endController();
            mController = null;
        }
    }

    @Override
    public void forceEndState() {
        startAnimation();
        endAnimation();
        notifyAnimationEnd();
    }

    @Override
    public void resetAnimation() {
        notifyAnimationReset();
        if (mController != null) {
            mController.resetController();
            mController = null;
        }
        //TODO optimize
        getTransition().startTransition();
        getTransition().updateProgress(mReverse ? 1 : 0);
        getTransition().stopTransition();
    }
}