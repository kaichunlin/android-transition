package com.kaichunlin.transition;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * NineOldAndroids' ObjectAnimator is used to provide required transition behavior.
 * <p>
 * TODO may be possible to switch to use NineOldDroid's ViewPropertyAnimator for better performance
 * <p>
 * Created by Kai-Chun Lin on 2015/4/16.
 */
public class DefaultTransitionController extends BaseTransitionController implements Cloneable {
    protected AnimatorSet mAnimSet;


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
    public static ITransitionController wrapAnimatorSet(@NonNull AnimatorSet animSet) {
        return new DefaultTransitionController(animSet);
    }

    public DefaultTransitionController(@NonNull AnimatorSet mAnimSet) {
        this(null, mAnimSet);
    }

    /**
     *
     * @param target the View that should be transitioned
     * @param mAnimSet
     */
    public DefaultTransitionController(@Nullable View target, @NonNull AnimatorSet mAnimSet) {
        super(target);
        this.mAnimSet = mAnimSet;
        mStartDelay = mAnimSet.getStartDelay();
        for (Animator animator : mAnimSet.getChildAnimations()) {
            if (!(animator instanceof ValueAnimator)) {
                throw new UnsupportedOperationException("Only ValueAnimator and its subclasses are supported: " + animator);
            }
        }
        mDuration = mAnimSet.getDuration();
        if (mAnimSet.getDuration() >= 0) {
            long duration = mAnimSet.getDuration();
            for (Animator animator : mAnimSet.getChildAnimations()) {
                animator.setDuration(duration);
            }
        } else {
            for (Animator animator : mAnimSet.getChildAnimations()) {
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
        if (mTarget == null && mInterpolator == null) {
            return;
        }
        for (Animator animator : mAnimSet.getChildAnimations()) {
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
        final boolean DEBUG = TransitionConfig._debug;
        long time=0;
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
        } else {
            //forward
            if (mStart < mEnd) {
                if (progress < mStart) {
                    time = 0;
                    if (DEBUG) {
                        debug = "forward progression: [" + mStart + ".." + mEnd + "], before start, progress="+progress;
                    }
                } else if (progress > mEnd) {
                    time = mTotalDuration;
                    if (DEBUG) {
                        debug = "forward progression: [" + mStart + ".." + mEnd + "], after finish"+progress;
                    }
                }
                //backward
            } else if (mStart > mEnd) {
                if (progress > mStart) {
                    time = 0;
                    if (DEBUG) {
                        debug = "forward progression: [" + mStart + ".." + mEnd + "], before start, progress="+progress;
                    }
                } else if (progress < mEnd) {
                    time = mTotalDuration;
                    if (DEBUG) {
                        debug = "forward progression: [" + mStart + ".." + mEnd + "], after finish"+progress;
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
        if (time == mLastTime || (!mStarted && !mSetup)) {
            return;
        }

        if (TransitionConfig.isDebug()) {
            appendLog("updateState: \t\ttime=" + time);
        }

        mSetup = false;
        mLastTime = time;
        for (Animator animator : mAnimSet.getChildAnimations()) {
            ValueAnimator va = (ValueAnimator) animator;
            long absTime = time - va.getStartDelay();
            if (absTime >= 0) {
                va.setCurrentPlayTime(absTime);
            }
        }
    }

    private void appendLog(String msg) {
        getTransitionStateHolder().append(getId(), this, msg);
    }

    @CheckResult
    @Override
    public DefaultTransitionController clone() {
        return (DefaultTransitionController) super.clone();
    }
}
