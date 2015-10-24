package com.kaichunlin.transition.animation;

import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import com.kaichunlin.transition.TransitionOperation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Kai on 2015/7/12.
 */
public abstract class AbstractAnimation implements Animation {
    @IntDef({CONTROLLER_ANIMATION, CONTROLLER_ANIMATOR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StateControllerType {
    }

    public static final int CONTROLLER_ANIMATION = 0;
    public static final int CONTROLLER_ANIMATOR = 1;

    protected final Runnable mStartAnimation = new Runnable() {
        @Override
        public void run() {
            startAnimation();
        }
    };

    protected Handler mHandler;
    private final Set<AnimationListener> mAnimationListenerSet = new HashSet<>();
    private int mDuration = -1;
    private boolean mReverse;
    private boolean mAnimating;
    private final TransitionOperation mTransition;
    private @StateControllerType int mStateControllerType = CONTROLLER_ANIMATION;

    public AbstractAnimation() {
        mTransition = null;
    }

    public AbstractAnimation(@NonNull TransitionOperation transition) {
        mTransition = transition;
    }

    /**
     * The driver for the animation, can be either {@link #CONTROLLER_ANIMATION} or {@link #CONTROLLER_ANIMATOR},
     * currently the chief difference is that {@link #CONTROLLER_ANIMATION} may be more performant in some situations,
     * where as {@link #CONTROLLER_ANIMATOR} allows pausing/resuming the animation on API level 19 or later.
     * <p>
     * {@link #CONTROLLER_ANIMATION} is the default, unless a valid View cannot be found (for example when animating
     * the menu), then {@link #CONTROLLER_ANIMATOR} is automatically used.
     */
    public void setStateControllerType(@StateControllerType int stateControllerType) {
        mStateControllerType = stateControllerType;
    }

    @StateControllerType
    public int getStateControllerType() {
        return mStateControllerType;
    }

    protected TransitionOperation getTransition() {
        return mTransition;
    }

    /**
     * @param animationListener
     */
    public void addAnimationListener(AnimationListener animationListener) {
        mAnimationListenerSet.add(animationListener);
    }

    /**
     * @param animationListener
     */
    public void removeAnimationListener(AnimationListener animationListener) {
        mAnimationListenerSet.remove(animationListener);
    }

    public void setDuration(@IntRange(from = 0) int duration) {
        mDuration = duration;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setReverseAnimation(boolean reverse) {
        mReverse = reverse;
    }

    /**
     * @return
     */
    public boolean isReverseAnimation() {
        return mReverse;
    }

    /**
     * Starts the animation with the default duration (300 ms)
     */
    @UiThread
    public abstract void startAnimation();

    /**
     * Starts the animation with the specified duration
     *
     * @param duration
     */
    @UiThread
    public abstract void startAnimation(@IntRange(from = 0) int duration);

    @Override
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

    protected void setAnimating(boolean animating) {
        mAnimating = animating;
    }

    public boolean isAnimating() {
        return mAnimating;
    }

    protected void notifyAnimationStart() {
        if (mAnimationListenerSet.size() == 0) {
            return;
        }
        for (AnimationListener listener : mAnimationListenerSet) {
            listener.onAnimationStart(this);
        }
    }

    protected void notifyAnimationEnd() {
        if (mAnimationListenerSet.size() == 0) {
            return;
        }
        for (AnimationListener listener : mAnimationListenerSet) {
            listener.onAnimationEnd(this);
        }
    }

    protected void notifyAnimationCancel() {
        if (mAnimationListenerSet.size() == 0) {
            return;
        }
        for (AnimationListener listener : mAnimationListenerSet) {
            listener.onAnimationCancel(this);
        }
    }

    protected void notifyAnimationReset() {
        if (mAnimationListenerSet.size() == 0) {
            return;
        }
        for (AnimationListener listener : mAnimationListenerSet) {
            listener.onAnimationReset(this);
        }
    }
}
