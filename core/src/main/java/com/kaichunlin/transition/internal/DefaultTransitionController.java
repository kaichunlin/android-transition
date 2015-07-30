package com.kaichunlin.transition.internal;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.kaichunlin.transition.TransitionConfig;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;

/**
 * NineOldAndroids' ObjectAnimator is used to provide required transition behavior.
 * <p>
 * TODO may be possible to switch to use NineOldDroid's ViewPropertyAnimator for better performance
 * <p>
 * Created by Kai-Chun Lin on 2015/4/16.
 */
public class DefaultTransitionController extends TransitionController<DefaultTransitionController> implements Cloneable {
    protected AnimatorSet mAnimSet;
    private int mUpdateCount;

    /**
     * Wraps an Animator as a DefaultTransitionController
     *
     * @param anim
     * @return
     */
    public static DefaultTransitionController wrapAnimator(@NonNull Animator anim) {
        AnimatorSet set = new AnimatorSet();
        set.play(anim);
        return new DefaultTransitionController(set);
    }

    /**
     * Wraps an AnimatorSet as a DefaultTransitionController
     *
     * @param animSet
     * @return
     */
    public static DefaultTransitionController wrapAnimatorSet(@NonNull AnimatorSet animSet) {
        return new DefaultTransitionController(animSet);
    }

    public DefaultTransitionController(@NonNull AnimatorSet mAnimSet) {
        this(null, mAnimSet);
    }

    /**
     * @param target   the View that should be transitioned
     * @param mAnimSet
     */
    public DefaultTransitionController(@Nullable View target, @NonNull AnimatorSet mAnimSet) {
        super(target);
        this.mAnimSet = mAnimSet;
        mStartDelay = mAnimSet.getStartDelay();

        ArrayList<Animator> animators = mAnimSet.getChildAnimations();
        final int size = animators.size();
        Animator animator;
        for (int i = 0; i < size; i++) {
            animator = animators.get(i);
            if (!(animator instanceof ValueAnimator)) {
                throw new UnsupportedOperationException("Only ValueAnimator and its subclasses are supported: " + animator);
            }
        }
        mDuration = mAnimSet.getDuration();
        if (mAnimSet.getDuration() >= 0) {
            long duration = mAnimSet.getDuration();
            for (int i = 0; i < size; i++) {
                animators.get(i).setDuration(duration);
            }
        } else {
            for (int i = 0; i < size; i++) {
                animator = animators.get(i);
                long endTime = animator.getStartDelay() + animator.getDuration();
                if (mDuration < endTime) {
                    mDuration = endTime;
                }
            }
        }
        mTotalDuration = mStartDelay + mDuration;
        updateProgressWidth();
    }

    @Override
    public void start() {
        super.start();
        if (TransitionConfig.isDebug()) {
            getTransitionStateHolder().clear();
        }
        if (mTarget == null && mInterpolator == null) {
            return;
        }
        ArrayList<Animator> animators = mAnimSet.getChildAnimations();
        final int size = animators.size();
        Animator animator;
        for (int i = 0; i < size; i++) {
            animator = animators.get(i);
            if (mTarget != null) {
                animator.setTarget(mTarget);
            }
            if (mInterpolator != null) {
                animator.setInterpolator(mInterpolator);
            }
        }
    }

    @Override
    public void updateProgress(float progress) {
        String debug = "";
        final boolean DEBUG = TransitionConfig.isDebug();

        long time = 0;
        if (mStart < mEnd && progress >= mStart && progress <= mEnd || mStart > mEnd && progress >= mEnd && progress <= mStart) {
            //forward progression
            if (mStart < mEnd) {
                time = (long) (mTotalDuration * (progress - mStart) / mProgressWidth);
                //backward
            } else {
                time = (long) (mTotalDuration - mTotalDuration * (progress - mEnd) / mProgressWidth);
            }
            time -= mStartDelay;

            if (time > 0) {
                mStarted = true;
            }
            if (DEBUG) {
                debug = "forward progression: [" + mStart + ".." + mEnd + "], mStarted=" + mStarted;
            }
            mUpdateCount++;
        } else {
            //forward
            if (mStart < mEnd) {
                if (progress < mStart) {
                    time = 0;
                    if (DEBUG) {
                        debug = "forward progression: [" + mStart + ".." + mEnd + "], pre-start, progress=" + progress;
                    }
                } else if (progress > mEnd) {
                    time = mTotalDuration;
                    if (mUpdateCount == 1) {
                        mUpdateCount = -1;
                    }
                    if (DEBUG) {
                        debug = "forward progression: [" + mStart + ".." + mEnd + "], post-finish, progress=" + progress;
                    }
                }
                //backward
            } else if (mStart > mEnd) {
                if (progress > mStart) {
                    time = 0;
                    if (DEBUG) {
                        debug = "forward progression: [" + mStart + ".." + mEnd + "], pre-start, progress=" + progress;
                    }
                } else if (progress < mEnd) {
                    time = mTotalDuration;
                    if (mUpdateCount == 1) {
                        mUpdateCount = -1;
                    }
                    if (DEBUG) {
                        debug = "forward progression: [" + mStart + ".." + mEnd + "], post-finish, progress=" + progress;
                    }
                }
            }
        }

        //TODO hack to make it work for ViewPager, removing mUpdateStateAfterUpdateProgress would break it for everything else
//        if (mSetup && mUpdateStateAfterUpdateProgress) {
        updateState(time);
//        }

        if (DEBUG) {
            appendLog("updateProgress: \t" + debug);
        }
    }

    private void updateState(long time) {
        if ((time == mLastTime || (!mStarted && !mSetup)) && mUpdateCount != -1) {
            return;
        }

        if (TransitionConfig.isDebug()) {
            appendLog("updateState: \t\ttime=" + time);
        }

        mSetup = false;
        mLastTime = time;
        ArrayList<Animator> animators = mAnimSet.getChildAnimations();
        final int size = animators.size();
        for (int i = 0; i < size; i++) {
            ValueAnimator va = (ValueAnimator) animators.get(i);
            long absTime = time - va.getStartDelay();
            if (absTime >= 0) {
                va.setCurrentPlayTime(absTime);
            }
        }
    }

    private void appendLog(String msg) {
        getTransitionStateHolder().append(getId() + "->View" + mTarget.hashCode(), this, msg);
    }

    @CheckResult
    @Override
    public DefaultTransitionController clone() {
        return (DefaultTransitionController) super.clone();
    }

    protected DefaultTransitionController self() {
        return this;
    }
}
