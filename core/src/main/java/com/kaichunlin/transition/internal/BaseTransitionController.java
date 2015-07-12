package com.kaichunlin.transition.internal;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Interpolator;

import com.kaichunlin.transition.R;
import com.kaichunlin.transition.util.TransitionStateHolder;

/**
 * Base implementation for ITransitionController
 * <p>
 * Created by Kai-Chun Lin on 2015/4/28.
 */
public abstract class BaseTransitionController implements ITransitionController {
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

    /**
     * @param target the view this object should manipulate
     */
    public BaseTransitionController(@Nullable View target) {
        this.mTarget = target;
    }

    @Override
    public void start() {
        mLastTime = -1;
        mSetup = true;
    }

    @Override
    public void setId(@NonNull String id) {
        mId = id;
    }

    @Override
    public String getId() {
        if (mId == null) {
            mId = toString();
        }
        return mId;
    }

    @Override
    public void end() {
        mStarted = false;
    }

    protected void updateProgressWidth() {
        mProgressWidth = Math.abs(mEnd - mStart);
    }

    @Override
    public ITransitionController setRange(float start, float end) {
        this.mStart = start;
        this.mEnd = end;
        updateProgressWidth();
        return this;
    }

    @Override
    public float getStart() {
        return mStart;
    }

    @Override
    public ITransitionController setStart(float mStart) {
        this.mStart = mStart;
        updateProgressWidth();
        return this;
    }

    @Override
    public float getEnd() {
        return mEnd;
    }

    @Override
    public ITransitionController setEnd(float end) {
        this.mEnd = end;
        updateProgressWidth();
        return this;
    }

    @Override
    public View getTarget() {
        return mTarget;
    }

    @Override
    public ITransitionController reverse() {
        String id = getId();
        String REVERSE = "_REVERSE";
        if (id.endsWith(REVERSE)) {
            setId(id.substring(0, id.length() - REVERSE.length()));
        }

        float start = mStart;
        float end = mEnd;
        mStart = end;
        mEnd = start;
        return this;
    }

    @Override
    public ITransitionController setTarget(@Nullable View target) {
        this.mTarget = target;
        return this;
    }

    @Override
    public void setInterpolator(@Nullable Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    @Override
    public void setUpdateStateAfterUpdateProgress(boolean updateStateAfterUpdateProgress) {
        mUpdateStateAfterUpdateProgress = updateStateAfterUpdateProgress;
    }

    @Override
    public void setEnable(boolean enable) {
        mEnable = enable;
    }

    //TODO hack to make ViewPager work
    @Override
    public boolean isEnable() {
        return mEnable;
    }

    @CheckResult
    @Override
    public BaseTransitionController clone() {
        try {
            return (BaseTransitionController) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected TransitionStateHolder getTransitionStateHolder() {
        return (TransitionStateHolder) getTarget().getTag(R.id.debug_id);
    }
}
