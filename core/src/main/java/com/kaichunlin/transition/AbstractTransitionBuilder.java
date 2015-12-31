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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides foundation to build classes that allows easy creation of ITransition
 * <p>
 * Created by Kai-Chun Lin on 2015/4/23.
 */
public abstract class AbstractTransitionBuilder<T extends AbstractTransitionBuilder, S extends Transition> implements Cloneable {

    static final int SCALE_FACTOR = 10_000;

    protected static final int ALPHA = 0;
    protected static final int ROTATION = 1;
    protected static final int ROTATION_X = 2;
    protected static final int ROTATION_Y = 3;
    protected static final int SCALE = 4;
    protected static final int SCALE_X = 5;
    protected static final int SCALE_Y = 6;
    protected static final int TRANSLATION_X = 7;
    protected static final int TRANSLATION_Y = 8;
    protected static final int X = 9;
    protected static final int Y = 10;
    protected static final int VISIBILITY = 11;
    protected static final int TRANSLATION_X_AS_FRACTION_OF_WIDTH = 12;
    protected static final int TRANSLATION_Y_AS_FRACTION_OF_HEIGHT = 13;
    protected static final int TOTAL = 14;
    protected boolean mAutoClone;

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
            case TRANSLATION_X_AS_FRACTION_OF_WIDTH:
                return "translationXAsFractionOfWidth";
            case TRANSLATION_Y_AS_FRACTION_OF_HEIGHT:
                return "translationYAsFractionOfHeight";
        }
        throw new IllegalArgumentException();
    }

    SparseArray<PropertyValuesHolder> mHolders = new SparseArray<>(4);
    SparseArray<ShadowValuesHolder> mShadowHolders = new SparseArray<>(4);
    List<DelayedEvaluator<T>> mDelayed;
    float mStart = TransitionController.DEFAULT_START;
    float mEnd = TransitionController.DEFAULT_END;
    String mId;
    boolean mReverse;
    Interpolator mInterpolator;
    int mDuration;
    DelayedProcessor mDelayedProcessor;
    transient WeakReference<Object> mOwnerRef;

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
     * Similar to alpha(float...), but wait until the transition is about to start to perform the evaluation
     *
     * @param transitions
     * @return self
     */
    public T delayAlpha(float... transitions) {
        getDelayedProcessor().addProcess(ALPHA, transitions);
        return self();
    }

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
     * Similar to rotation(float...), but wait until the transition is about to start to perform the evaluation
     *
     * @param transitions
     * @return self
     */
    public T delayRotation(float... transitions) {
        getDelayedProcessor().addProcess(ROTATION, transitions);
        return self();
    }

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
     * Similar to rotationX(float...), but wait until the transition is about to start to perform the evaluation
     *
     * @param transitions
     * @return self
     */
    public T delayRotationX(float... transitions) {
        getDelayedProcessor().addProcess(ROTATION_X, transitions);
        return self();
    }

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
     * Similar to rotationY(float...), but wait until the transition is about to start to perform the evaluation
     *
     * @param transitions
     * @return self
     */
    public T delayRotationY(float... transitions) {
        getDelayedProcessor().addProcess(ROTATION_Y, transitions);
        return self();
    }

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
     * Similar to scaleX(float...), but wait until the transition is about to start to perform the evaluation
     *
     * @param transition
     * @return self
     */
    public T delayScaleX(float... transition) {
        getDelayedProcessor().addProcess(SCALE_X, transition);
        return self();
    }

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
     * Similar to scaleX(float...), but wait until the transition is about to start to perform the evaluation
     *
     * @param transitions
     * @return self
     */
    public T delayScaleY(@FloatRange(from = 0.0) float... transitions) {
        getDelayedProcessor().addProcess(SCALE_Y, transitions);
        return self();
    }

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
     * Similar to scale(float...), but wait until the transition is about to start to perform the evaluation
     *
     * @param transitions
     * @return self
     */
    public T delayScale(float... transitions) {
        getDelayedProcessor().addProcess(SCALE, transitions);
        return self();
    }

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
     * Similar to delayTranslationX(float...), but wait until the transition is about to start to perform the evaluation
     *
     * @param transitions
     * @return self
     */
    public T delayTranslationX(float... transitions) {
        getDelayedProcessor().addProcess(TRANSLATION_X, transitions);
        return self();
    }

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
     * Similar to delayTranslationY(float...), but wait until the transition is about to start to perform the evaluation
     *
     * @param transitions
     * @return self
     */
    public T delayTranslationY(float... transitions) {
        getDelayedProcessor().addProcess(TRANSLATION_Y, transitions);
        return self();
    }

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
     * @param transitions
     * @return self
     */
    public T delayX(final float... transitions) {
        getDelayedProcessor().addProcess(X, transitions);
        return self();
    }

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
     * @param transitions
     * @return self
     */
    public T delayY(final float... transitions) {
        getDelayedProcessor().addProcess(Y, transitions);
        return self();
    }

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


//    public boolean shouldClone() {
//        return mOwnerRef != null && mOwnerRef.get() != null;
//    }

    protected void checkModifiability() {
        if (mOwnerRef == null || mOwnerRef.get() == null) {
            mOwnerRef = null;
        } else {
            throw new IllegalStateException("clone() should be called when building multiple animations/transitions from the same builder, i.e. after build*() is called.");
        }
    }

//    /**
//     * Auto clone itself when necessary.
//     *
//     * @return
//     */
//    public T autoClone() {
//        mAutoClone = true;
//        return self();
//    }
//
//    public T cloneIfNecessary() {
//        if(shouldClone()) {
//            return (T) clone();
//        }
//        return self();
//    }

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
        markObjectAsModifiabilityFlag(vt);
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
        markObjectAsModifiabilityFlag(animation);
        if (mDuration != 0) {
            animation.setDuration(mDuration);
        }
        return animation;
    }

    public TransitionAnimation buildAnimationFor(AnimationManager animationManager) {
        TransitionAnimation animation = buildAnimation();
        markObjectAsModifiabilityFlag(animation);
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
        markObjectAsModifiabilityFlag(transitionManager);
        S transition = build();
        transitionManager.addTransition(transition);
        return transition;
    }

    protected void markObjectAsModifiabilityFlag(Object owner) {
        mOwnerRef = new WeakReference<>(owner);
    }

    /**
     * Adds a DelayedEvaluator whose evaluate(View, BaseTransitionBuilder) method will only be called when the transition is about to start
     *
     * @param delayed
     * @return
     */
    public T addDelayedEvaluator(@NonNull DelayedEvaluator delayed) {
        if (mDelayed == null) {
            mDelayed = new ArrayList<>(4);
        }
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
            newCopy.mOwnerRef = null;
            newCopy.mHolders = new SparseArray<>(mHolders.size());
            for (int i = 0, size = mHolders.size(); i < size; i++) {
                newCopy.mHolders.put(mHolders.keyAt(i), mHolders.valueAt(i).clone());
            }
            newCopy.mShadowHolders = new SparseArray<>(mShadowHolders.size());
            for (int i = 0, size = mShadowHolders.size(); i < size; i++) {
                newCopy.mShadowHolders.put(mShadowHolders.keyAt(i), mShadowHolders.valueAt(i).clone());
            }
            if (mDelayed != null) {
                newCopy.mDelayed = new ArrayList<>(mDelayed.size());
                newCopy.mDelayed.addAll(mDelayed);
            }
            if (mDelayedProcessor != null) {
                newCopy.mDelayedProcessor = mDelayedProcessor.clone();
                newCopy.mDelayed.remove(mDelayedProcessor);
                newCopy.mDelayed.add(newCopy.mDelayedProcessor);
            }
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

    protected boolean hasDelayedProcessor() {
        return mDelayedProcessor != null;
    }

    protected DelayedProcessor getDelayedProcessor() {
        if (mDelayedProcessor == null) {
            mDelayedProcessor = new DelayedProcessor();
            addDelayedEvaluator(mDelayedProcessor);
        }
        return mDelayedProcessor;
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
    protected static class DelayedProcessor implements DelayedEvaluator, Cloneable {
        float[] process;
        float[][] multiValueProcess;
        int cachedViewState = -1;

        DelayedProcessor() {
            process = new float[TOTAL];
            for (int i = 0; i < TOTAL; i++) {
                process[i] = Float.MIN_VALUE;
            }
            multiValueProcess = new float[TOTAL][];
        }

        private float[] createNewValues(float[] values, float firstVal) {
            float[] newValues = new float[values.length + 1];
            newValues[0] = firstVal;
            System.arraycopy(values, 0, newValues, 1, values.length);
            return newValues;
        }

        private View getView(AbstractTransitionBuilder builder) {
            return ((ViewTransitionBuilder) builder).getTargetView();
        }

        @Override
        public void evaluate(View view, AbstractTransitionBuilder builder) {
            float value;
            float[] values;
            value = process[ALPHA];
            if (value != Float.MIN_VALUE) {
                builder.alpha(value);
            }
            values = multiValueProcess[ALPHA];
            if (values != null) {
                builder.alpha(createNewValues(values, getView(builder).getAlpha()));
            }

            value = process[ROTATION];
            if (value != Float.MIN_VALUE) {
                builder.rotation(value);
            }
            values = multiValueProcess[ROTATION];
            if (values != null) {
                builder.rotation(createNewValues(values, getView(builder).getRotation()));
            }

            value = process[ROTATION_X];
            if (value != Float.MIN_VALUE) {
                builder.rotationX(value);
            }
            values = multiValueProcess[ROTATION_X];
            if (values != null) {
                builder.rotationX(createNewValues(values, getView(builder).getRotationX()));
            }

            value = process[ROTATION_Y];
            if (value != Float.MIN_VALUE) {
                builder.rotationY(value);
            }
            values = multiValueProcess[ROTATION_Y];
            if (values != null) {
                builder.rotationY(createNewValues(values, getView(builder).getRotationY()));
            }

            value = process[SCALE];
            if (value != Float.MIN_VALUE) {
                builder.scale(value, value);
            }
            values = multiValueProcess[SCALE];
            if (values != null) {
                //TODO assumes scaleX & scaleY are equal
                builder.scale(createNewValues(values, getView(builder).getScaleX()));
            }

            value = process[SCALE_X];
            if (value != Float.MIN_VALUE) {
                builder.scaleX(value);
            }
            values = multiValueProcess[SCALE_X];
            if (values != null) {
                builder.scaleX(createNewValues(values, getView(builder).getScaleX()));
            }

            value = process[SCALE_Y];
            if (value != Float.MIN_VALUE) {
                builder.scaleY(value);
            }
            values = multiValueProcess[SCALE_Y];
            if (values != null) {
                builder.scaleY(createNewValues(values, getView(builder).getScaleY()));
            }

            value = process[TRANSLATION_X];
            if (value != Float.MIN_VALUE) {
                builder.translationX(value);
            }
            values = multiValueProcess[TRANSLATION_X];
            if (values != null) {
                builder.translationX(createNewValues(values, getView(builder).getTranslationX()));
            }

            value = process[TRANSLATION_Y];
            if (value != Float.MIN_VALUE) {
                builder.translationY(value);
            }
            values = multiValueProcess[TRANSLATION_Y];
            if (values != null) {
                builder.translationY(createNewValues(values, getView(builder).getTranslationY()));
            }

            value = process[X];
            if (value != Float.MIN_VALUE) {
                builder.x(value);
            }
            values = multiValueProcess[X];
            if (values != null) {
                builder.x(createNewValues(values, getView(builder).getX()));
            }

            value = process[Y];
            if (value != Float.MIN_VALUE) {
                builder.y(value);
            }
            values = multiValueProcess[Y];
            if (values != null) {
                builder.y(createNewValues(values, getView(builder).getY()));
            }

            value = process[VISIBILITY];
            if (value != Float.MIN_VALUE) {
                if (cachedViewState == -1) {
                    cachedViewState = view.getVisibility();
                    view.setVisibility((int) value);
                } else {
                    view.setVisibility(cachedViewState);
                }
            }
            value = process[TRANSLATION_X_AS_FRACTION_OF_WIDTH];
            if (value != Float.MIN_VALUE) {
                builder.translationX(getView(builder).getWidth() * value);
            }
            values = multiValueProcess[TRANSLATION_X_AS_FRACTION_OF_WIDTH];
            if (values != null) {
                float[] widths = new float[values.length + 1];
                int width = getView(builder).getWidth();
                widths[0] = width;
                for (int i = 0; i < values.length; i++) {
                    widths[i + 1] = width * values[i];
                }
                builder.translationY(widths);
            }

            value = process[TRANSLATION_Y_AS_FRACTION_OF_HEIGHT];
            if (value != Float.MIN_VALUE) {
                builder.translationY(getView(builder).getHeight() * value);
            }
            values = multiValueProcess[TRANSLATION_Y_AS_FRACTION_OF_HEIGHT];
            if (values != null) {
                float[] heights = new float[values.length + 1];
                int height = getView(builder).getHeight();
                heights[0] = height;
                for (int i = 0; i < values.length; i++) {
                    heights[i + 1] = height * values[i];
                }
                builder.translationY(heights);
            }
        }

        void addProcess(int type, int value) {
            process[type] = value;
        }

        void addProcess(int type, float value) {
            process[type] = value;
        }

        public void addProcess(int type, float[] values) {
            multiValueProcess[type] = values;
        }

        @CheckResult
        protected DelayedProcessor clone() {
            try {
                DelayedProcessor dp = (DelayedProcessor) super.clone();
                dp.process = new float[TOTAL];
                System.arraycopy(process, 0, dp.process, 0, process.length);
                //dp.process[VISIBILITY] = Float.MIN_VALUE;
                return dp;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
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
