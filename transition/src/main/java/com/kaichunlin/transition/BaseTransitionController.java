package com.kaichunlin.transition;

import android.view.View;
import android.view.animation.Interpolator;

import com.kaichunlin.transition.util.TransitionStateHolder;

/**
 * Base implementation for ITransitionController
 * <p>
 * Created by Kai-Chun Lin on 2015/4/28.
 */
public abstract class BaseTransitionController implements ITransitionController {
    String mId;
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
    public BaseTransitionController(View target) {
        this.mTarget = target;
    }

    @Override
    public void start() {
        mLastTime = -1;
        mSetup = true;
    }

    @Override
    public void setId(String id) {
        mId = id;
    }

    @Override
    public String getId() {
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
    public ITransitionController setEnd(float mEnd) {
        this.mEnd = mEnd;
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
    public ITransitionController setTarget(View target) {
        this.mTarget = target;
        return this;
    }

    @Override
    public void setInterpolator(Interpolator interpolator) {
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
