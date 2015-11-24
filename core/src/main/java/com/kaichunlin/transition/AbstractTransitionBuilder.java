package com.kaichunlin.transition;

import android.support.annotation.CheckResult;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Interpolator;

import com.kaichunlin.transition.animation.Animation;
import com.kaichunlin.transition.animation.AnimationManager;
import com.kaichunlin.transition.animation.TransitionAnimation;
import com.kaichunlin.transition.internal.TransitionController;
import com.nineoldandroids.animation.PropertyValuesHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides foundation to build classes that allows easy creation of ITransition
 * <p>
 * Created by Kai-Chun Lin on 2015/4/23.
 */
public abstract class AbstractTransitionBuilder<T extends AbstractTransitionBuilder, S extends Transition> implements Cloneable {

    static final int SCALE_FACTOR = 10_000;

    public static final int ALPHA = 0;
    public static final int ROTATION = 1;
    public static final int ROTATION_X = 2;
    public static final int ROTATION_Y = 3;
    public static final int SCALE = 4;
    public static final int SCALE_X = 5;
    public static final int SCALE_Y = 6;
    public static final int TRANSLATION_X = 7;
    public static final int TRANSLATION_Y = 8;
    public static final int X = 9;
    public static final int Y = 10;
    protected static final int VISIBILITY = 11;
    static final int TOTAL = 12;

    public static String getPropertyName(int propertyId) {
        switch (propertyId) {
            case ALPHA:
                return "alpha";
            case ROTATION:
                return "rotation";
            case ROTATION_X:
                return "rotationX";
            case ROTATION_Y:
                return "rotationY";
            case SCALE_X:
                return "scaleX";
            case SCALE_Y:
                return "scaleY";
            case TRANSLATION_X:
                return "translationX";
            case TRANSLATION_Y:
                return "translationY";
            case X:
                return "x";
            case Y:
                return "y";
        }
        throw new IllegalArgumentException();
    }

    SparseArray<PropertyValuesHolder> mHolders = new SparseArray<>(4);
    SparseArray<ShadowValuesHolder> mShadowHolders = new SparseArray<>(4);
    List<DelayedEvaluator<T>> mDelayed = new ArrayList<>(4);
    float mStart = TransitionController.DEFAULT_START;
    float mEnd = TransitionController.DEFAULT_END;
    String mId;
    boolean mReverse;
    Interpolator mInterpolator;
    int mDuration;
    DelayedProcessor delayedProcessor;

    AbstractTransitionBuilder() {
    }

    public T id(String id) {
        mId = id;
        return self();
    }

    /**
     * Sets the start and end range of the transition, this affects where the end the transition is reached and no further modification will be performed on the target view
     * For most {@link TransitionManager} the range will be [0..1]
     *
     * @param start
     * @param end
     * @return
     */
    public T range(float start, float end) {
        mStart = start;
        mEnd = end;
        return self();
    }

    /**
     * Sets the start range of the transition, this affects where the end the transition is reached and no further modification will be performed on the target view
     * For most {@link TransitionManager} the range will be [0..1]
     *
     * @param start
     * @return
     */
    public T startRange(float start) {
        mStart = start;
        return self();
    }

    /**
     * Sets the end range of the transition, this affects where the end the transition is reached and no further modification will be performed on the target view
     * For most {@link TransitionManager} the range will be [0..1]
     *
     * @param end
     * @return
     */
    public T endRange(float end) {
        mEnd = end;
        return self();
    }

    public float getStartRange() {
        return mStart;
    }

    public float getEndRange() {
        return mEnd;
    }

    public float getRange() {
        return mEnd - mStart;
    }

    //TODO this applies the range value to all the values set before this call and after the last withRange call
    private T withRange(float start, float end) {
        return self();
    }

    /**
     * Changes the alpha to the specified values
     *
     * @param vals
     * @return self
     */
    public T alpha(float... vals) {
        transitFloat(ALPHA, vals);
        return self();
    }


    /**
     * Changes the alpha value from the target view's current value to the end value
     *
     * @param end
     * @return self
     */
    public abstract T alpha(@FloatRange(from = 0.0, to = 1.0) float end);

    /**
     * Similar to alpha(float), but wait until the transition is about to start to perform the evaluation
     *
     * @param end
     * @return self
     */
    public T delayAlpha(@FloatRange(from = 0.0, to = 1.0) float end) {
        getDelayedProcessor().addProcess(ALPHA, end);
        return self();
    }

    /**
     * Changes the rotation (rotationX and rotationY) value to the specified values
     *
     * @param vals
     * @return self
     */
    public T rotation(float... vals) {
        transitFloat(ROTATION, vals);
        return self();
    }

    /**
     * Changes the rotation value from the target view's current value to the end value
     *
     * @param end
     * @return self
     */
    public abstract T rotation(float end);

    /**
     * Similar to rotation(float), but wait until the transition is about to start to perform the evaluation
     *
     * @param end
     * @return self
     */
    public T delayRotation(float end) {
        getDelayedProcessor().addProcess(ROTATION, end);
        return self();
    }

    /**
     * Changes the rotationX value to the specified values
     *
     * @param vals
     * @return self
     */
    public T rotationX(float... vals) {
        transitFloat(ROTATION_X, vals);
        return self();
    }

    /**
     * Changes the rotationX value from the target view's current value to the end value
     *
     * @param end
     * @return self
     */
    public abstract T rotationX(float end);

    /**
     * Similar to rotationX(float), but wait until the transition is about to start to perform the evaluation
     *
     * @param end
     * @return self
     */
    public T delayRotationX(float end) {
        getDelayedProcessor().addProcess(ROTATION_X, end);
        return self();
    }

    /**
     * Changes the rotationY to the specified values
     *
     * @param vals
     * @return self
     */
    public T rotationY(float... vals) {
        transitFloat(ROTATION_Y, vals);
        return self();
    }

    /**
     * Changes the rotationY value from the target view's current value to the end value
     *
     * @param end
     * @return self
     */
    public abstract T rotationY(float end);

    /**
     * Similar to rotationY(float), but wait until the transition is about to start to perform the evaluation
     *
     * @param end
     * @return self
     */
    public T delayRotationY(float end) {
        getDelayedProcessor().addProcess(ROTATION_Y, end);
        return self();
    }

    /**
     * Changes the scaleX value to the specified values
     *
     * @param vals
     * @return self
     */
    public T scaleX(float... vals) {
        transitFloat(SCALE_X, vals);
        return self();
    }

    /**
     * Changes the scaleX value from the target view's current value to the end value
     *
     * @param end
     * @return self
     */
    public abstract T scaleX(@FloatRange(from = 0.0) float end);

    /**
     * Similar to scaleX(float), but wait until the transition is about to start to perform the evaluation
     *
     * @param end
     * @return self
     */
    public T delayScaleX(@FloatRange(from = 0.0) float end) {
        getDelayedProcessor().addProcess(SCALE_X, end);
        return self();
    }

    /**
     * Changes the scaleY value to the specified values
     *
     * @param vals
     * @return self
     */
    public T scaleY(float... vals) {
        transitFloat(SCALE_Y, vals);
        return self();
    }

    /**
     * Changes the scaleY value from the target view's current value to the end value
     *
     * @param end
     * @return self
     */
    public abstract T scaleY(@FloatRange(from = 0.0) float end);

    /**
     * Similar to scaleX(float), but wait until the transition is about to start to perform the evaluation
     *
     * @param end
     * @return self
     */
    public T delayScaleY(@FloatRange(from = 0.0) float end) {
        getDelayedProcessor().addProcess(SCALE_Y, end);
        return self();
    }

    /**
     * Changes the scale (scaleX and scaleY) value to the specified values
     *
     * @param vals
     * @return self
     */
    public T scale(float... vals) {
        transitFloat(SCALE_X, vals);
        transitFloat(SCALE_Y, vals);
        return self();
    }

    /**
     * Changes the scale value from the target view's current value to the end value
     *
     * @param end
     * @return self
     */
    public abstract T scale(@FloatRange(from = 0.0) float end);

    /**
     * Similar to scale(float), but wait until the transition is about to start to perform the evaluation
     *
     * @param end
     * @return self
     */
    public T delayScale(@FloatRange(from = 0.0) float end) {
        getDelayedProcessor().addProcess(SCALE, end);
        return self();
    }

    /**
     * Changes the translationX value from the target view's current value to the end value
     *
     * @param vals
     * @return self
     */
    public T translationX(float... vals) {
        transitFloat(TRANSLATION_X, vals);
        return self();
    }

    /**
     * Changes the translationX value from the target view's current value to the end value
     *
     * @param end
     * @return self
     */
    public abstract T translationX(float end);

    /**
     * Similar to delayTranslationX(float), but wait until the transition is about to start to perform the evaluation
     *
     * @param end
     * @return self
     */
    public T delayTranslationX(float end) {
        getDelayedProcessor().addProcess(TRANSLATION_X, end);
        return self();
    }

    /**
     * Changes the translationY value from the target view's current value to the end value
     *
     * @param vals
     * @return self
     */
    public T translationY(float... vals) {
        transitFloat(TRANSLATION_Y, vals);
        return self();
    }

    /**
     * Changes the translationY value from the target view's current value to the end value
     *
     * @param end
     * @return self
     */
    public abstract T translationY(float end);

    /**
     * Similar to delayTranslationY(float), but wait until the transition is about to start to perform the evaluation
     *
     * @param end
     * @return self
     */
    public T delayTranslationY(float end) {
        getDelayedProcessor().addProcess(TRANSLATION_Y, end);
        return self();
    }

    /**
     * Changes the x value from the target view's current value to the end value
     *
     * @param vals
     * @return self
     */
    public T x(float... vals) {
        transitFloat(X, vals);
        return self();
    }

    /**
     * Changes the x value from the target view's current value to the end value
     *
     * @param end
     * @return self
     */
    public abstract T x(float end);

    /**
     * Similar to x(float), but wait until the transition is about to start to perform the evaluation
     *
     * @param end
     * @return self
     */
    public T delayX(final float end) {
        getDelayedProcessor().addProcess(X, end);
        return self();
    }

    /**
     * Changes the y value from the target view's current value to the end value
     *
     * @param vals
     * @return self
     */
    public T y(float... vals) {
        transitFloat(Y, vals);
        return self();
    }

    /**
     * Changes the y value from the target view's current value to the end value
     *
     * @param end
     * @return self
     */
    public abstract T y(float end);

    /**
     * Similar to y(float), but wait until the transition is about to start to perform the evaluation
     *
     * @param end
     * @return self
     */
    public T delayY(final float end) {
        getDelayedProcessor().addProcess(Y, end);
        return self();
    }

    /**
     * Only applies when building {@link Animation}
     *
     * @param duration
     * @return
     */
    public T duration(@IntRange(from = 0) int duration) {
        mDuration = duration;
        return self();
    }

    /**
     * Transits a float propertyId from the start value to the end value
     *
     * @param propertyId
     * @param vals
     * @return self
     */
    public T transitFloat(int propertyId, float... vals) {
        String property = getPropertyName(propertyId);
        mHolders.put(propertyId, PropertyValuesHolder.ofFloat(property, vals));
        mShadowHolders.put(propertyId, ShadowValuesHolder.ofFloat(property, vals));
        return self();
    }

//    /**
//     * Transits a float property from the start value to the end value
//     *
//     * @param property
//     * @param start
//     * @param end
//     * @return self
//     */
//    public T transitFloat(@NonNull String property, float start, float end) {
//        mHolders.put(property, PropertyValuesHolder.ofFloat(property, start, end));
//        mShadowHolders.put(property, ShadowValuesHolder.ofFloat(property, start, end));
//        return self();
//    }

    /**
     * Transits a float property from the start value to the end value
     *
     * @param propertyId
     * @param vals
     * @return self
     */
    public T transitInt(int propertyId, int... vals) {
        String property = getPropertyName(propertyId);
        mHolders.put(propertyId, PropertyValuesHolder.ofInt(property, vals));
        mShadowHolders.put(propertyId, ShadowValuesHolder.ofInt(property, vals));
        return self();
    }

//    /**
//     * Transits an integer property from the start value to the end value
//     *
//     * @param property
//     * @param start
//     * @param end
//     * @return self
//     */
//    public T transitInt(@NonNull String property, int start, int end) {
//        mHolders.put(property, PropertyValuesHolder.ofInt(property, start, end));
//        mShadowHolders.put(property, ShadowValuesHolder.ofInt(property, start, end));
//        return self();
//    }

    /**
     * Reverse the transition effect
     *
     * @return self
     */
    public T reverse() {
        mReverse = !mReverse;
        return self();
    }

    public T interpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
        return self();
    }

    @CheckResult
    /**
     * Asks the subclass to create and set a ITransition
     *
     * @return ITransition that would perform the desired transition
     */
    protected abstract S createTransition();

    /**
     * Builds a ITransition, the created object will not be modified when the builder's modifier methods are called.
     *
     * @return
     */
    public final S build() {
        S vt = createTransition();
        vt.setId(mId);

        if (mInterpolator != null) {
            vt.setInterpolator(mInterpolator);
        }
        if (mReverse) {
            vt.reverse();
        }
        return vt;
    }

    public TransitionAnimation buildAnimation() {
        TransitionAnimation animation = new TransitionAnimation(mStart < mEnd ? build() : build().reverse());
        if (mDuration != 0) {
            animation.setDuration(mDuration);
        }
        return animation;
    }

    public TransitionAnimation buildAnimationFor(AnimationManager animationManager) {
        TransitionAnimation animation = buildAnimation();
        animationManager.addAnimation(animation);
        return animation;
    }

    /**
     * Sets the {@link TransitionManager}, once set calling {@link build()} would automatically add
     * the created {@link ViewTransition} to the adapter.
     *
     * @param transitionManager
     * @return
     */
    public S buildFor(@NonNull TransitionManager transitionManager) {
        S transition = build();
        transitionManager.addTransition(transition);
        return transition;
    }

    /**
     * Adds a DelayedEvaluator whose evaluate(View, BaseTransitionBuilder) method will only be called when the transition is about to start
     *
     * @param delayed
     * @return
     */
    public T addDelayedEvaluator(@NonNull DelayedEvaluator delayed) {
        mDelayed.add(delayed);
        return self();
    }

    protected abstract T self();

    @CheckResult
    @Override
    public AbstractTransitionBuilder clone() {
        AbstractTransitionBuilder newCopy = null;
        try {
            newCopy = (AbstractTransitionBuilder) super.clone();
            newCopy.mHolders = new SparseArray<>(mHolders.size());
            for (int i = 0, size = mHolders.size(); i < size; i++) {
                newCopy.mHolders.put(mHolders.keyAt(i), mHolders.valueAt(i).clone());
            }
            newCopy.mShadowHolders = new SparseArray<>(mShadowHolders.size());
            for (int i = 0, size = mShadowHolders.size(); i < size; i++) {
                newCopy.mShadowHolders.put(mShadowHolders.keyAt(i), mShadowHolders.valueAt(i).clone());
            }
            newCopy.mDelayed = new ArrayList<>(mDelayed.size());
            newCopy.mDelayed.addAll(mDelayed);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return newCopy;
    }

    public T visible() {
        getDelayedProcessor().addProcess(VISIBILITY, View.VISIBLE);
        return self();
    }

    public T invisible() {
        getDelayedProcessor().addProcess(VISIBILITY, View.INVISIBLE);
        return self();
    }

    public T gone() {
        getDelayedProcessor().addProcess(VISIBILITY, View.GONE);
        return self();
    }

    protected DelayedProcessor getDelayedProcessor() {
        if (delayedProcessor == null) {
            delayedProcessor = new DelayedProcessor();
            addDelayedEvaluator(delayedProcessor);
        }
        return delayedProcessor;
    }

    protected PropertyValuesHolder[] getValuesHolders() {
        final int size = mHolders.size();
        PropertyValuesHolder[] holders = new PropertyValuesHolder[size];
        for (int i = 0; i < size; i++) {
            holders[i] = mHolders.valueAt(i);
        }
        return holders;
    }

    //a shared static DelayedEvaluator class to reduce object creation
    protected static class DelayedProcessor implements DelayedEvaluator {
        final float[] process = new float[TOTAL];

        DelayedProcessor() {
            for (int i = 0; i < process.length; i++) {
                process[i] = Float.MIN_VALUE;
            }
        }

        @Override
        public void evaluate(View view, AbstractTransitionBuilder builder) {
            float value;
            value = process[ALPHA];
            if (value != Float.MIN_VALUE) {
                builder.alpha(value);
            }
            value = process[ROTATION];
            if (value != Float.MIN_VALUE) {
                builder.rotation(value);
            }
            value = process[ROTATION_X];
            if (value != Float.MIN_VALUE) {
                builder.rotationX(value);
            }
            value = process[ROTATION_Y];
            if (value != Float.MIN_VALUE) {
                builder.rotationY(value);
            }
            value = process[SCALE];
            if (value != Float.MIN_VALUE) {
                builder.scale(value, value);
            }
            value = process[SCALE_X];
            if (value != Float.MIN_VALUE) {
                builder.scaleX(value);
            }
            value = process[SCALE_Y];
            if (value != Float.MIN_VALUE) {
                builder.scaleY(value);
            }
            value = process[TRANSLATION_X];
            if (value != Float.MIN_VALUE) {
                builder.translationX(value);
            }
            value = process[TRANSLATION_Y];
            if (value != Float.MIN_VALUE) {
                builder.translationY(value);
            }
            value = process[X];
            if (value != Float.MIN_VALUE) {
                builder.x(value);
            }
            value = process[Y];
            if (value != Float.MIN_VALUE) {
                builder.y(value);
            }
            value = process[VISIBILITY];
            if (value != Float.MIN_VALUE) {
                view.setVisibility((int) value);
            }
        }

        void addProcess(int type, int value) {
            process[type] = value;
        }

        void addProcess(int type, float value) {
            process[type] = value;
        }
    }

    /**
     * Holds values so the reversed version of PropertyValuesHolder can be created
     */
    static class ShadowValuesHolder implements Cloneable {
        String property;
        int[] iVals;
        float[] fVals;
        boolean isFloat;

        ShadowValuesHolder(@NonNull String property, float... vals) {
            this.property = property;
            fVals = vals;
            isFloat = true;
        }

        ShadowValuesHolder(@NonNull String property, int... vals) {
            this.property = property;
            iVals = vals;
        }


        static ShadowValuesHolder ofFloat(@NonNull String property, float... vals) {
            return new ShadowValuesHolder(property, vals);
        }

        static ShadowValuesHolder ofInt(@NonNull String property, int... vals) {
            return new ShadowValuesHolder(property, vals);
        }

        @CheckResult
        PropertyValuesHolder createReverse() {
            final int max;
            if (isFloat) {
                float[] newfVals = new float[fVals.length];
                max = fVals.length;
                for (int i = 0; i < max; i++) {
                    newfVals[i] = fVals[max - i - 1];
                }
                return PropertyValuesHolder.ofFloat(property, newfVals);
            } else {
                int[] newiVals = new int[iVals.length];
                max = iVals.length;
                for (int i = 0; i < max; i++) {
                    newiVals[i] = iVals[max - i - 1];
                }
                return PropertyValuesHolder.ofInt(property, newiVals);
            }
        }

        @CheckResult
        protected ShadowValuesHolder clone() {
            try {
                return (ShadowValuesHolder) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * This delays the evaluation to the time when transition is about to start, so the current state of the target view can be used in the evaluation
     */
    public interface DelayedEvaluator<T extends AbstractTransitionBuilder> {
        void evaluate(View view, T builder);
    }
}
