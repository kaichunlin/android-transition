package com.kaichunlin.transition.internal;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Interpolator;

import com.kaichunlin.transition.R;
import com.kaichunlin.transition.util.TransitionStateLogger;

/**
 * Created by Kai-Chun Lin on 2015/4/28.
 */
public abstract class TransitionController<T extends TransitionController> {
    public static final float DEFAULT_START = 0f;
    public static final float DEFAULT_END = 1f;

    private String mId;
    View mTarget;
    float mStart = DEFAULT_START;
    float mEnd = DEFAULT_END;
    float mProgressWidth;
    boolean mStarted;
    long mStartDelay;
    long mDuration;
    long mTotalDuration;
    long mLastTime;
    boolean mSetup;
    Interpolator mInterpolator;
    boolean mUpdateStateAfterUpdateProgress;
    boolean mEnable = true;
    boolean mReverse;

    /**
     * @param target the view this object should manipulate
     */
    public TransitionController(@Nullable View target) {
        this.mTarget = target;
    }

    public void setId(@NonNull String id) {
        mId = id;
    }

    public String getId() {
        if (mId == null) {
            mId = toString();
        }
        return mId;
    }

    /**
     * Starts the transition
     */
    public void start() {
        mLastTime = -1;
        mSetup = true;
    }

    /**
     * @param progress
     */
    protected abstract void updateProgress(float progress);

    /**
     * Ends the transition
     */
    public void end() {
        mStarted = false;
    }

    protected void updateProgressWidth() {
        mProgressWidth = Math.abs(mEnd - mStart);
    }

    /**
     * Defaults to [0..1] if not set
     *
     * @param start the start of the applicable transition range
     * @param end   the end of the applicable transition range
     * @return
     */
    public T setRange(float start, float end) {
        this.mStart = start;
        this.mEnd = end;
        updateProgressWidth();
        return self();
    }

    /**
     * @return the start value for the applicable transition range
     */
    public float getStart() {
        return mStart;
    }

    public T setStart(float mStart) {
        this.mStart = mStart;
        updateProgressWidth();
        return self();
    }

    /**
     * @return the end value for the applicable transition range
     */
    public float getEnd() {
        return mEnd;
    }

    public T setEnd(float end) {
        this.mEnd = end;
        updateProgressWidth();
        return self();
    }

    public View getTarget() {
        return mTarget;
    }

    /**
     * Reverse how the transition is applied, such that the transition previously performed when progress=start of range is only performed when progress=end of range
     *
     * @return
     */
    public T reverse() {
        String id = getId();
        String REVERSE = "_REVERSE";
        if (id.endsWith(REVERSE)) {
            setId(id.substring(0, id.length() - REVERSE.length()));
        }

        float start = mStart;
        float end = mEnd;
        mStart = end;
        mEnd = start;
        mReverse = !mReverse;
        return self();
    }

    public boolean isReverse() {
        return mReverse;
    }

    /**
     * @param target the view this controller should manipulate
     * @return
     */
    public T setTarget(@Nullable View target) {
        this.mTarget = target;
        return self();
    }

    /**
     * @param interpolator the Interpolator this controller should be when transiting a view
     */
    public void setInterpolator(@Nullable Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    /**
     * @param updateStateAfterUpdateProgress whether or not to update a controller's enable state after each {@link #updateProgress(float)} call
     */
    public void setUpdateStateAfterUpdateProgress(boolean updateStateAfterUpdateProgress) {
        mUpdateStateAfterUpdateProgress = updateStateAfterUpdateProgress;
    }

    /**
     * Enables or disables the controller
     *
     * @param enable
     */
    public void setEnable(boolean enable) {
        mEnable = enable;
    }

    /**
     * TODO hack to make ViewPager work
     *
     * @return is the controlled enabled
     */
    public boolean isEnable() {
        return mEnable;
    }

    public TransitionStateLogger getTransitionStateHolder() {
        TransitionStateLogger logger = (TransitionStateLogger) getTarget().getTag(R.id.debug_id);
        if (logger == null) {
            logger = new TransitionStateLogger(getId());
            getTarget().setTag(R.id.debug_id, logger);
        }
        return logger;
    }

    @CheckResult
    @Override
    public TransitionController clone() {
        try {
            return (TransitionController) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected abstract T self();
}
