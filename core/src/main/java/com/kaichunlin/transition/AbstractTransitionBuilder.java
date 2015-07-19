package com.kaichunlin.transition;

import android.support.annotation.CheckResult;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Interpolator;

import com.kaichunlin.transition.Animation.TransitionAnimation;
import com.kaichunlin.transition.Animation.Animation;
import com.kaichunlin.transition.internal.TransitionController;
import com.nineoldandroids.animation.PropertyValuesHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides foundation to build classes that allows easy creation of ITransition
 * <p>
 * Created by Kai-Chun Lin on 2015/4/23.
 */
public abstract class AbstractTransitionBuilder<T extends AbstractTransitionBuilder, S extends Transition> implements Cloneable {

    static final int SCALE_FACTOR = 10_000;

    public static final String ALPHA = "alpha";
    public static final String ROTATION = "rotation";
    public static final String ROTATION_X = "rotationX";
    public static final String ROTATION_Y = "rotationY";
    public static final String SCALE_X = "scaleX";
    public static final String SCALE_Y = "scaleY";
    public static final String TRANSLATION_X = "translationX";
    public static final String TRANSLATION_Y = "translationY";
    public static final String X = "x";
    public static final String Y = "y";

    Map<String, PropertyValuesHolder> mHolders = new HashMap<>();
    Map<String, ShadowValuesHolder> mShadowHolders = new HashMap<>();
    List<DelayedEvaluator> mDelayed = new ArrayList<>();
    float mStart = TransitionController.DEFAULT_START;
    float mEnd = TransitionController.DEFAULT_END;
    String mId;
    boolean mReverse;
    Interpolator mInterpolator;

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

    //TODO this applies the range value to all the values set before this call and after the last withRange call
    private T withRange(float start, float end) {
        return self();
    }

    /**
     * Changes the alpha value from the target view's current value to the end value
     *
     * @param start
     * @param end
     * @return self
     */
    public T alpha(@FloatRange(from = 0.0, to = 1.0) float start, @FloatRange(from = 0.0, to = 1.0) float end) {
        transitFloat(ALPHA, start, end);
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
    public T delayAlpha(@FloatRange(from = 0.0, to = 1.0) final float end) {
        addDelayedEvaluator(new DelayedEvaluator() {
            @Override
            public void evaluate(View view, AbstractTransitionBuilder builder) {
                builder.alpha(end);
            }
        });
        return self();
    }

    /**
     * Changes the rotation (rotationX and rotationY) value from the target view's current value to the end value
     *
     * @param start
     * @param end
     * @return self
     */
    public T rotation(float start, float end) {
        transitFloat(ROTATION, start, end);
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
    public T delayRotation(final float end) {
        addDelayedEvaluator(new DelayedEvaluator() {
            @Override
            public void evaluate(View view, AbstractTransitionBuilder builder) {
                builder.rotation(end);
            }
        });
        return self();
    }

    /**
     * Changes the rotationX value from the target view's current value to the end value
     *
     * @param start
     * @param end
     * @return self
     */
    public T rotationX(float start, float end) {
        transitFloat(ROTATION_X, start, end);
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
    public T delayRotationX(final float end) {
        addDelayedEvaluator(new DelayedEvaluator() {
            @Override
            public void evaluate(View view, AbstractTransitionBuilder builder) {
                builder.rotationX(end);
            }
        });
        return self();
    }

    /**
     * Changes the rotationY value from the target view's current value to the end value
     *
     * @param start
     * @param end
     * @return self
     */
    public T rotationY(float start, float end) {
        transitFloat(ROTATION_Y, start, end);
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
    public T delayRotationY(final float end) {
        addDelayedEvaluator(new DelayedEvaluator() {
            @Override
            public void evaluate(View view, AbstractTransitionBuilder builder) {
                builder.rotationY(end);
            }
        });
        return self();
    }

    /**
     * Changes the scaleX value from the target view's current value to the end value
     *
     * @param start
     * @param end
     * @return self
     */
    public T scaleX(@FloatRange(from = 0.0, to = 1.0) float start, @FloatRange(from = 0.0, to = 1.0) float end) {
        transitFloat(SCALE_X, start, end);
        return self();
    }

    /**
     * Changes the scaleX value from the target view's current value to the end value
     *
     * @param end
     * @return self
     */
    public abstract T scaleX(@FloatRange(from = 0.0, to = 1.0) float end);

    /**
     * Similar to scaleX(float), but wait until the transition is about to start to perform the evaluation
     *
     * @param end
     * @return self
     */
    public T delayScaleX(@FloatRange(from = 0.0, to = 1.0) final float end) {
        addDelayedEvaluator(new DelayedEvaluator() {
            @Override
            public void evaluate(View view, AbstractTransitionBuilder builder) {
                builder.scaleX(end);
            }
        });
        return self();
    }

    /**
     * Changes the scaleY value from the target view's current value to the end value
     *
     * @param start
     * @param end
     * @return self
     */
    public T scaleY(@FloatRange(from = 0.0, to = 1.0) float start, @FloatRange(from = 0.0, to = 1.0) float end) {
        transitFloat(SCALE_Y, start, end);
        return self();
    }

    /**
     * Changes the scaleY value from the target view's current value to the end value
     *
     * @param end
     * @return self
     */
    public abstract T scaleY(@FloatRange(from = 0.0, to = 1.0) float end);

    /**
     * Similar to scaleX(float), but wait until the transition is about to start to perform the evaluation
     *
     * @param end
     * @return self
     */
    public T delayScaleY(@FloatRange(from = 0.0, to = 1.0) final float end) {
        addDelayedEvaluator(new DelayedEvaluator() {
            @Override
            public void evaluate(View view, AbstractTransitionBuilder builder) {
                builder.scaleY(end);
            }
        });
        return self();
    }

    /**
     * Changes the scale (scaleX and scaleY) value from the target view's current value to the end value
     *
     * @param start
     * @param end
     * @return self
     */
    public T scale(@FloatRange(from = 0.0, to = 1.0) float start, @FloatRange(from = 0.0, to = 1.0) float end) {
        transitFloat(SCALE_X, start, end);
        transitFloat(SCALE_Y, start, end);
        return self();
    }

    /**
     * Changes the scale value from the target view's current value to the end value
     *
     * @param end
     * @return self
     */
    public abstract T scale(@FloatRange(from = 0.0, to = 1.0) float end);

    /**
     * Similar to scale(float), but wait until the transition is about to start to perform the evaluation
     *
     * @param end
     * @return self
     */
    public T delayScale(@FloatRange(from = 0.0, to = 1.0) final float end) {
        addDelayedEvaluator(new DelayedEvaluator() {
            @Override
            public void evaluate(View view, AbstractTransitionBuilder builder) {
                builder.scaleX(end).scaleY(end);
            }
        });
        return self();
    }

    /**
     * Changes the translationX value from the target view's current value to the end value
     *
     * @param start
     * @param end
     * @return self
     */
    public T translationX(float start, float end) {
        transitFloat(TRANSLATION_X, start, end);
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
    public T delayTranslationX(final float end) {
        addDelayedEvaluator(new DelayedEvaluator() {
            @Override
            public void evaluate(View view, AbstractTransitionBuilder builder) {
                builder.translationX(end);
            }
        });
        return self();
    }

    /**
     * Changes the translationY value from the target view's current value to the end value
     *
     * @param start
     * @param end
     * @return self
     */
    public T translationY(float start, float end) {
        transitFloat(TRANSLATION_Y, start, end);
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
    public T delayTranslationY(final float end) {
        addDelayedEvaluator(new DelayedEvaluator() {
            @Override
            public void evaluate(View view, AbstractTransitionBuilder builder) {
                builder.translationY(end);
            }
        });
        return self();
    }

    /**
     * Changes the x value from the target view's current value to the end value
     *
     * @param start
     * @param end
     * @return self
     */
    public T x(float start, float end) {
        transitFloat(X, start, end);
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
        addDelayedEvaluator(new DelayedEvaluator() {
            @Override
            public void evaluate(View view, AbstractTransitionBuilder builder) {
                builder.x(end);
            }
        });
        return self();
    }

    /**
     * Changes the y value from the target view's current value to the end value
     *
     * @param start
     * @param end
     * @return self
     */
    public T y(float start, float end) {
        transitFloat(Y, start, end);
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
        addDelayedEvaluator(new DelayedEvaluator() {
            @Override
            public void evaluate(View view, AbstractTransitionBuilder builder) {
                builder.y(end);
            }
        });
        return self();
    }

    /**
     * Transits a float property from the start value to the end value
     *
     * @param property
     * @param start
     * @param end
     * @return self
     */
    public T transitFloat(@NonNull String property, float start, float end) {
        mHolders.put(property, PropertyValuesHolder.ofFloat(property, start, end));
        mShadowHolders.put(property, ShadowValuesHolder.ofFloat(property, start, end));
        return self();
    }

    /**
     * Transits an integer property from the start value to the end value
     *
     * @param property
     * @param start
     * @param end
     * @return self
     */
    public T transitInt(@NonNull String property, int start, int end) {
        mHolders.put(property, PropertyValuesHolder.ofInt(property, start, end));
        mShadowHolders.put(property, ShadowValuesHolder.ofInt(property, start, end));
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
    abstract S createTransition();

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

    public Animation buildAnimation() {
        TransitionAnimation animation=new TransitionAnimation(mStart < mEnd?build():build().reverse());
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
            newCopy.mHolders = new HashMap<>();
            for (Map.Entry<String, PropertyValuesHolder> entry : mHolders.entrySet()) {
                newCopy.mHolders.put(entry.getKey(), entry.getValue().clone());
            }
            newCopy.mShadowHolders = new HashMap<>();
            for (Map.Entry<String, ShadowValuesHolder> entry : mShadowHolders.entrySet()) {
                newCopy.mShadowHolders.put(entry.getKey(), entry.getValue().clone());
            }
            newCopy.mDelayed = new ArrayList<>();
            newCopy.mDelayed.addAll(mDelayed);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return newCopy;
    }

    /**
     * Holds values so the reversed version of PropertyValuesHolder can be created
     */
    static class ShadowValuesHolder implements Cloneable {
        String property;
        int iStart, iEnd;
        float fStart, fEnd;
        boolean isFloat;

        ShadowValuesHolder(@NonNull String property, float start, float end) {
            this.property = property;
            fStart = start;
            fEnd = end;
            isFloat = true;
        }

        ShadowValuesHolder(@NonNull String property, int start, int end) {
            this.property = property;
            iStart = start;
            iEnd = end;
        }


        static ShadowValuesHolder ofFloat(@NonNull String property, float start, float end) {
            return new ShadowValuesHolder(property, start, end);
        }

        static ShadowValuesHolder ofInt(@NonNull String property, int start, int end) {
            return new ShadowValuesHolder(property, start, end);
        }

        @CheckResult
        PropertyValuesHolder createReverse() {
            if (isFloat) {
                return PropertyValuesHolder.ofFloat(property, fEnd, fStart);
            } else {
                return PropertyValuesHolder.ofInt(property, iEnd, iStart);
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
    public interface DelayedEvaluator {
        void evaluate(View view, AbstractTransitionBuilder builder);
    }
}
