package com.kaichunlin.transition.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.Menu;
import android.view.animation.LinearInterpolator;

/**
 * This adapter integrates traditional animations using the same methods and logic as the rest of the framework.
 * <p>
 * TODO animation state callback
 * <p>
 * Created by Kai on 2015/6/22.
 */
public class AnimateMenuAdapter extends MenuBaseAdapter implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private MenuOptionHandler mMenuOptionHandler = new MenuOptionHandler(this, new MenuOptionHandler.AdapterState() {
        @Override
        public boolean isOpened(Activity activity) {
            return mOpened;
        }
    });
    private final MenuBaseAdapter mAdapter;
    private ValueAnimator mValueAnimator;
    private boolean mOpened;
    private int mDuration = 300;

    public AnimateMenuAdapter() {
        mAdapter = null;
    }

    /**
     * Wraps an existing MenuBaseAdapter to reuse its onCreateOptionsMenu(...) logic and its transition effects
     *
     * @param adapter
     */
    public AnimateMenuAdapter(MenuBaseAdapter adapter) {
        mAdapter = adapter;
    }

    public void setMenuOptionHandler(MenuOptionHandler menuOptionHandler) {
        mMenuOptionHandler = menuOptionHandler;
    }

    public void setmDuration(int duration) {
        mDuration = duration;
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
    public void startAnimation(int duration) {
        stopAnimation();
        mValueAnimator = new ValueAnimator();
        mValueAnimator.setFloatValues();
        mValueAnimator.setDuration(duration);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        if (mOpened) {
            mValueAnimator.setFloatValues(1, 0);
        } else {
            mValueAnimator.setFloatValues(0, 1);
        }
        mValueAnimator.addUpdateListener(this);
        mValueAnimator.addListener(this);
        mValueAnimator.start();
        if (mAdapter == null) {
            startTransition(mOpened ? 1 : 0);
        } else {
            mAdapter.startTransition(mOpened ? 1 : 0);
        }
        mOpened = !mOpened;
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

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (mAdapter == null) {
            updateProgress((Float) animation.getAnimatedValue());
        } else {
            mAdapter.updateProgress((Float) animation.getAnimatedValue());
        }
    }

    @Override
    protected MenuOptionHandler createMenuHandler() {
        return mMenuOptionHandler;
    }

    @Override
    public void setupOption(Activity activity, MenuOptionConfiguration openConfig) {
        if (mAdapter == null) {
            super.setupOption(activity, openConfig);
        } else {
            mAdapter.setupOption(activity, openConfig);
        }
    }

    @Override
    public void setupOpenOption(Activity activity, MenuOptionConfiguration openConfig) {
        if (mAdapter == null) {
            super.setupOpenOption(activity, openConfig);
        } else {
            mAdapter.setupOpenOption(activity, openConfig);
        }
    }

    @Override
    public void setupCloseOption(Activity activity, MenuOptionConfiguration closeConfig) {
        if (mAdapter == null) {
            super.setupCloseOption(activity, closeConfig);
        } else {
            mAdapter.setupCloseOption(activity, closeConfig);
        }
    }

    @Override
    public void setupOptions(Activity activity, MenuOptionConfiguration openConfig, MenuOptionConfiguration closeConfig) {
        if (mAdapter == null) {
            super.setupOptions(activity, openConfig, closeConfig);
        } else {
            mAdapter.setupOptions(activity, openConfig, closeConfig);
        }
    }

    @Override
    public void onCreateOptionsMenu(Activity activity, Menu menu) {
        if (mAdapter == null) {
            super.onCreateOptionsMenu(activity, menu);
        } else {
            mAdapter.onCreateOptionsMenu(activity, menu, mMenuOptionHandler.getAdapterState());
        }
    }

    @Override
    public void stopTransition() {
        if (mAdapter == null) {
            super.stopTransition();
        } else {
            mAdapter.stopTransition();
        }
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
