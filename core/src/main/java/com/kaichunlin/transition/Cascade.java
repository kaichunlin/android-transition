package com.kaichunlin.transition;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Defines how a cascading transition should be applied for each child of a ViewGroup, this is used for
 * {@link ViewTransitionBuilder#transitViewGroup(ViewTransitionBuilder.ViewGroupTransition, Cascade)}.
 * There are 3 different type:
 * 1. STAGGERED, where effects would interleave with each other, the length of the effect = getTransitionEnd() - getCascadeEnd():
 * child 0: |------|
 * child 1:     |------|
 * child 2:         |------|
 * 2. RUN_TO_THE_END, where each effect would run to getTransitionEnd(), disregarding when it started:
 * child 0: |--------------|
 * child 1:     |----------|
 * child 2:         |------|
 * 3. SEQUENTIAL, where each effect would run to complete before playing the next effect:
 * Effect would be run as:
 * child 0: |----|
 * child 1:      |----|
 * child 2:           |----|
 *
 * Created by Kai-Chun Lin on 2015/11/24.
 */
public class Cascade implements ViewTransitionBuilder.ViewGroupTransition {
    private static final Interpolator DEFAULT_INTERPOLATOR = new LinearInterpolator();
    public static final int STAGGERED = 0;
    public static final int RUN_TO_THE_END = 1;
    public static final int SEQUENTIAL = 2;

    public final int type;
    public final Interpolator interpolator;
    float mCascadeStart;
    //end of the cascade, i.e. the last child will start the effect at this point
    public final float cascadeEnd;
    //end of transition effect, i.e. all effects will stop at this point
    float mTransitionEnd = 1f;
    boolean mReverse;


    /**
     * @param cascadeEnd this value should never be 1, otherwise no transition will be applied to the last child
     */
    public Cascade(@IntRange(from = 0, to = 2) int type, @FloatRange(from = 0.0, to = 1.0) float cascadeEnd) {
        this(type, cascadeEnd, DEFAULT_INTERPOLATOR);
    }

    /**
     * @param cascadeEnd this value should never be 1, otherwise no transition will be applied to the last child
     */
    public Cascade(@IntRange(from = 0, to = 2) int type, @FloatRange(from = 0.0, to = 1.0) float cascadeEnd, @NonNull Interpolator interpolator) {
        this.type = type;
        this.cascadeEnd = cascadeEnd;
        this.interpolator = interpolator;
    }

    public int getType() {
        return type;
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public float getCascadeEnd() {
        return cascadeEnd;
    }

    public Cascade cascadeStart(@FloatRange(from = 0.0, to = 1.0) float cascadeStart) {
        this.mCascadeStart = cascadeStart;
        return this;
    }

    public float getCascadeStart() {
        return mCascadeStart;
    }

    public Cascade transitionEnd(@FloatRange(from = 0.0, to = 1.0) float transitionEnd) {
        this.mTransitionEnd = transitionEnd;
        return this;
    }

    public float getTransitionEnd() {
        return mTransitionEnd;
    }

    public Cascade reverse() {
        this.mReverse = true;
        return this;
    }

    public boolean isReverse() {
        return mReverse;
    }

    @Override
    public void transit(ViewTransitionBuilder builder, ViewTransitionBuilder.ViewGroupTransitionConfig config) {
        float cascadeLength = cascadeEnd - mCascadeStart;
        float minEffectLength = mTransitionEnd - cascadeEnd;
        float fraction = cascadeLength / (config.total - 1);
        float rangeStart = mCascadeStart + (mReverse ? config.total - 1 - config.getIndex() : config.getIndex()) * fraction;
        float rangeEnd;
        switch (type) {
            case STAGGERED:
                rangeEnd = rangeStart + minEffectLength;
                break;
            case RUN_TO_THE_END:
                rangeEnd = mTransitionEnd;
                break;
            case SEQUENTIAL:
                rangeEnd = rangeStart + fraction;
                if (rangeEnd > mTransitionEnd) {
                    rangeEnd = mTransitionEnd;
                }
                break;
            default:
                throw new IllegalArgumentException();
        }

        builder.range(interpolator.getInterpolation(rangeStart), interpolator.getInterpolation(rangeEnd));
    }
}