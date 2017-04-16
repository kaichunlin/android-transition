package com.kaichunlin.transition;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.kaichunlin.transition.internal.CustomTransitionController;
import com.kaichunlin.transition.internal.DefaultTransitionController;
import com.kaichunlin.transition.internal.TransitionController;
import com.kaichunlin.transition.internal.TransitionControllerManager;
import com.kaichunlin.transition.transformer.ScaledTransformer;
import com.kaichunlin.transition.transformer.ViewTransformer;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorInflater;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for {@link ViewTransition}.
 */
public class ViewTransitionBuilder extends AbstractTransitionBuilder<ViewTransitionBuilder, ViewTransition> implements ViewTransition.Setup {
    private static final String TAG = "ViewTransitionBuilder";
    protected static final int HEIGHT = 0;
    protected static final int TRANSLATION_X_AS_FRACTION_OF_WIDTH_WITH_VIEW = 1;
    protected static final int TRANSLATION_X_AS_FRACTIONS_OF_WIDTH_WITH_VIEW = 2;
    protected static final int TRANSLATION_Y_AS_FRACTION_OF_HEIGHT_WITH_VIEW = 3;
    protected static final int TRANSLATION_Y_AS_FRACTIONS_OF_HEIGHT_WITH_VIEW = 4;
    protected static final int DELAYED_TOTAL = 5;
    private ViewTransitionDelayedEvaluation mViewDelayedProcessor;

    /**
     * Creates a {@link ViewTransitionBuilder} instance without any target view, {@link #target(View)}
     * should be called before build* is called.
     *
     * @return
     */
    public static ViewTransitionBuilder transit() {
        return new ViewTransitionBuilder();
    }

    /**
     * Creates a {@link ViewTransitionBuilder} instance with the target view set.
     *
     * @param view
     * @return
     */
    public static ViewTransitionBuilder transit(@Nullable View view) {
        return new ViewTransitionBuilder(view);
    }

    private CustomTransitionController mCustomTransitionController;
    private List<ViewTransition.Setup> mSetupList = new ArrayList<>();
    private View mView;

    private ViewTransitionBuilder() {
    }

    private ViewTransitionBuilder(@Nullable View view) {
        if (view == null) {
            Log.w(TAG, "Null view is provided, may cause exception");
        }
        mView = view;
    }

    /**
     * @param view The view the created {@link ViewTransition} should manipulate.
     * @return
     */
    public ViewTransitionBuilder target(@Nullable View view) {
        if (view == null) {
            Log.w(TAG, "Null view is provided, may cause exception");
        }
        mView = view;
        return self();
    }

    public View getTargetView() {
        return mView;
    }

    /**
     * Adds a custom {@link ViewTransformer}, the builder must be cloned if this method  is called.
     *
     * @param viewTransformer
     * @return
     */
    public ViewTransitionBuilder addViewTransformer(@NonNull ViewTransformer viewTransformer) {
        checkModifiability();
        if (mCustomTransitionController == null) {
            mCustomTransitionController = new CustomTransitionController();
        }
        mCustomTransitionController.addViewTransformer(viewTransformer);
        return self();
    }

    @Override
    public ViewTransitionBuilder alpha(@FloatRange(from = 0.0, to = 1.0) float end) {
        return alpha(ViewHelper.getAlpha(mView), end);
    }

    @Override
    public ViewTransitionBuilder rotation(float end) {
        return rotation(ViewHelper.getRotation(mView), end);
    }

    @Override
    public ViewTransitionBuilder rotationX(float end) {
        return rotationX(ViewHelper.getRotationX(mView), end);
    }

    @Override
    public ViewTransitionBuilder rotationY(float end) {
        return rotationY(ViewHelper.getRotationY(mView), end);
    }

    @Override
    public ViewTransitionBuilder scaleX(@FloatRange(from = 0.0) float end) {
        return scaleX(ViewHelper.getScaleX(mView), end);
    }

    @Override
    public ViewTransitionBuilder scaleY(@FloatRange(from = 0.0) float end) {
        return scaleY(ViewHelper.getScaleY(mView), end);
    }

    @Override
    public ViewTransitionBuilder scale(@FloatRange(from = 0.0) float end) {
        return scaleX(ViewHelper.getScaleX(mView), end).scaleY(ViewHelper.getScaleY(mView), end);
    }

    @Override
    public ViewTransitionBuilder translationX(float end) {
        return translationX(ViewHelper.getTranslationX(mView), end);
    }

    /**
     * @param fraction
     * @return
     */
    public ViewTransitionBuilder translationXAsFractionOfWidth(float fraction) {
        return translationX(mView.getWidth() * fraction);
    }

    /**
     * @param widthFractions
     * @return
     */
    public ViewTransitionBuilder translationXAsFractionOfWidth(float... widthFractions) {
        int width = mView.getWidth();
        for (int i = 0, size = widthFractions.length; i < size; i++) {
            widthFractions[i] = width * widthFractions[i];
        }
        return translationX(widthFractions);
    }

    /**
     * @param fraction
     * @return
     */
    public ViewTransitionBuilder delayTranslationXAsFractionOfWidth(float fraction) {
        getDelayedProcessor().addProcess(TRANSLATION_X_AS_FRACTION_OF_WIDTH, fraction);
        return self();
    }

    /**
     * @param fractions
     * @return
     */
    public ViewTransitionBuilder delayTranslationXAsFractionOfWidth(float... fractions) {
        getDelayedProcessor().addProcess(TRANSLATION_X_AS_FRACTION_OF_WIDTH, fractions);
        return self();
    }

    /**
     * @param targetView
     * @param fraction
     * @return
     */
    public ViewTransitionBuilder translationXAsFractionOfWidth(@NonNull final View targetView, float fraction) {
        return translationX(targetView.getWidth() * fraction);
    }

    /**
     * @param targetView
     * @param widthFractions
     * @return
     */
    public ViewTransitionBuilder translationXAsFractionOfWidth(@NonNull final View targetView, float... widthFractions) {
        int width = targetView.getWidth();
        for (int i = 0, size = widthFractions.length; i < size; i++) {
            widthFractions[i] = width * widthFractions[i];
        }
        return translationX(widthFractions);
    }

    /**
     * @param targetView
     * @param fraction
     * @return
     */
    public ViewTransitionBuilder delayTranslationXAsFractionOfWidth(@NonNull final View targetView, final float fraction) {
        getViewDelayedProcessor().addProcess(TRANSLATION_X_AS_FRACTION_OF_WIDTH_WITH_VIEW, targetView, fraction);
        return self();
    }

    /**
     * @param targetView
     * @param widthFractions
     * @return
     */
    public ViewTransitionBuilder delayTranslationXAsFractionOfWidth(@NonNull final View targetView, final float... widthFractions) {
        getViewDelayedProcessor().addProcess(TRANSLATION_X_AS_FRACTIONS_OF_WIDTH_WITH_VIEW, targetView, widthFractions);
        return self();
    }

    @Override
    public ViewTransitionBuilder translationY(float end) {
        return translationY(ViewHelper.getTranslationY(mView), end);
    }

    /**
     * @param fraction
     * @return
     */
    public ViewTransitionBuilder translationYAsFractionOfHeight(float fraction) {
        return translationY(mView.getHeight() * fraction);
    }

    /**
     * @param heightFractions
     * @return
     */
    public ViewTransitionBuilder translationYAsFractionOfHeight(float... heightFractions) {
        int height = mView.getWidth();
        for (int i = 0, size = heightFractions.length; i < size; i++) {
            heightFractions[i] = height * heightFractions[i];
        }
        return translationY(heightFractions);
    }

    /**
     * @param fraction
     * @return
     */
    public ViewTransitionBuilder delayTranslationYAsFractionOfHeight(float fraction) {
        getDelayedProcessor().addProcess(TRANSLATION_Y_AS_FRACTION_OF_HEIGHT, fraction);
        return self();
    }

    /**
     * @param heightFractions
     * @return
     */
    public ViewTransitionBuilder delayTranslationYAsFractionOfHeight(final float... heightFractions) {
        getDelayedProcessor().addProcess(TRANSLATION_Y_AS_FRACTION_OF_HEIGHT, heightFractions);
        return self();
    }

    /**
     * @param targetView
     * @param fraction
     * @return
     */
    public ViewTransitionBuilder translationYAsFractionOfHeight(@NonNull final View targetView, final float fraction) {
        return translationY(targetView.getHeight() * fraction);
    }

    /**
     * @param targetView
     * @param heightFractions
     * @return
     */
    public ViewTransitionBuilder translationYAsFractionOfHeight(@NonNull final View targetView, final float... heightFractions) {
        int height = targetView.getWidth();
        for (int i = 0, size = heightFractions.length; i < size; i++) {
            heightFractions[i] = height * heightFractions[i];
        }
        return translationY(heightFractions);
    }

    /**
     * @param targetView
     * @param fraction
     * @return
     */
    public ViewTransitionBuilder delayTranslationYAsFractionOfHeight(@NonNull final View targetView, final float fraction) {
        getViewDelayedProcessor().addProcess(TRANSLATION_Y_AS_FRACTION_OF_HEIGHT_WITH_VIEW, targetView, fraction);
        return self();
    }

    /**
     * @param targetView
     * @param heightFractions
     * @return
     */
    public ViewTransitionBuilder delayTranslationYAsFractionOfHeight(@NonNull final View targetView, final float... heightFractions) {
        getViewDelayedProcessor().addProcess(TRANSLATION_Y_AS_FRACTIONS_OF_HEIGHT_WITH_VIEW, targetView, heightFractions);
        return self();
    }

    @Override
    public ViewTransitionBuilder x(float end) {
        return x(ViewHelper.getX(mView), end);
    }

    @Override
    public ViewTransitionBuilder y(float end) {
        return y(ViewHelper.getY(mView), end);
    }

    public ViewTransitionBuilder height(@IntRange(from = 0) final int targetHeight) {
        height(mView.getHeight(), targetHeight);
        return self();
    }

    public ViewTransitionBuilder height(@IntRange(from = 0) final int fromHeight, @IntRange(from = 0) final int targetHeight) {
        addViewTransformer(new HeightTransformer(fromHeight, targetHeight));
        return self();
    }

    public ViewTransitionBuilder delayHeight(@IntRange(from = 0) final int targetHeight) {
        getDelayedProcessor().addProcess(HEIGHT, targetHeight);
        return self();
    }

    @CheckResult
    @Override
    public ViewTransitionBuilder clone() {
        ViewTransitionBuilder newCopy = (ViewTransitionBuilder) super.clone();
        newCopy.mSetupList = new ArrayList<>(mSetupList.size());
        newCopy.mSetupList.addAll(mSetupList);
        return newCopy;
    }

    @Override
    public ViewTransitionBuilder reverse() {
        // super.reverse();
        mHolders.clear();
        for (int i = 0, size = mShadowHolders.size(); i < size; i++) {
            mHolders.put(mShadowHolders.keyAt(i), mShadowHolders.valueAt(i).createReverse());
        }
        float oldStart = mStart;
        mStart = mEnd;
        mEnd = oldStart;
        return self();
    }

    /**
     * The builder must be cloned if this method  is called.
     *
     * @param setup
     * @return
     */
    public ViewTransitionBuilder addSetup(@NonNull ViewTransition.Setup setup) {
        checkModifiability();
        mSetupList.add(setup);
        return self();
    }

    /**
     * @param res
     * @param fromColorId
     * @param toColorId
     * @return
     */
    public ViewTransitionBuilder backgroundColorResource(@NonNull Resources res, @ColorRes int fromColorId, @ColorRes int toColorId) {
        return backgroundColor(res.getColor(fromColorId), res.getColor(toColorId));
    }

    /**
     * @param fromColor
     * @param toColor
     * @return
     */
    public ViewTransitionBuilder backgroundColor(@ColorInt final int fromColor, @ColorInt final int toColor) {
        addSetup(new ViewTransition.Setup() {
            @Override
            public void setupAnimation(@NonNull final TransitionControllerManager manager) {
                ObjectAnimator animator = ObjectAnimator.ofInt(manager.getTarget(), "backgroundColor", fromColor, toColor);
                animator.setDuration(10_000);
                animator.setEvaluator(new ArgbEvaluator());
                manager.addTransitionController(DefaultTransitionController.wrapAnimator(animator));
            }
        });
        return self();
    }

    /**
     * @param res
     * @param fromColorId
     * @param toColorId
     * @return
     */
    public ViewTransitionBuilder backgroundColorResourceHSV(@NonNull Resources res, @ColorRes int fromColorId, @ColorRes int toColorId) {
        return backgroundColorHSV(res.getColor(fromColorId), res.getColor(toColorId));
    }

    /**
     * @param fromColor
     * @param toColor
     * @return
     */
    public ViewTransitionBuilder backgroundColorHSV(@ColorInt final int fromColor, @ColorInt final int toColor) {
        addViewTransformer(new BackgroundColorHsvTransformer(fromColor, toColor));
        return self();
    }

    /**
     * TODO Current support is rudimentary, may expand support if there are enough demand for this.
     * <p>
     * Converts an animator to ITransition when built, note that not all functions of Animator are supported.
     * <p>
     * Non-working functions: repeatMode, repeatCount, delay, duration (when in a set), Interpolator.
     * <p>
     * Furthermore, {@link #transitViewGroup(ViewGroupTransition)} does not work with this method.
     *
     * @param animator
     * @return
     */
    public ViewTransitionBuilder animator(@NonNull final Animator animator) {
        addSetup(new ViewTransition.Setup() {
            @Override
            public void setupAnimation(@NonNull final TransitionControllerManager manager) {
                Animator animator2 = animator.clone();
                if (animator2 instanceof AnimatorSet) {
                    manager.addTransitionController(DefaultTransitionController.wrapAnimatorSet((AnimatorSet) animator2));
                } else {
                    manager.addTransitionController(DefaultTransitionController.wrapAnimator(animator2));
                }
            }
        });
        return self();
    }

    /**
     * See {@link #animator(Animator)}.
     *
     * @param context
     * @param animatorId
     * @return
     */
    public ViewTransitionBuilder animator(@NonNull Context context, int animatorId) {
        animator(AnimatorInflater.loadAnimator(context, animatorId));
        return this;
    }

    /**
     * The view previously set (through {@link #target(View)}) is casted as a ViewGroup, and the specified
     * {@link ViewGroupTransition} will {@link ViewGroupTransition#transit(ViewTransitionBuilder, ViewGroupTransitionConfig)}
     * all the children views.
     *
     * @param viewGroupTransition
     * @return
     * @throws ClassCastException If the target view is not a ViewGroup.
     */
    public ViewTransitionBuilder transitViewGroup(@NonNull ViewGroupTransition viewGroupTransition) {
        ViewGroup vg = (ViewGroup) mView;
        int total = vg.getChildCount();
        View view;
        ViewGroupTransitionConfig config = new ViewGroupTransitionConfig(vg, total);
        for (int i = 0; i < total; i++) {
            view = vg.getChildAt(i);
            config.childView = view;
            config.index = i;
            viewGroupTransition.transit(target(view), config);
        }
        return self();
    }

    /**
     * Similar to {@link #transitViewGroup(ViewGroupTransition)}, but with builder.range() auto filled for each child View, how
     * the start and end value is determined is defined by {@link Cascade} parameter.
     *
     * @param viewGroupTransition
     * @param cascade
     * @return
     */
    public ViewTransitionBuilder transitViewGroup(@NonNull final ViewGroupTransition viewGroupTransition, @NonNull final Cascade cascade) {
        transitViewGroup(new ViewGroupTransition() {
            @Override
            public void transit(ViewTransitionBuilder builder, ViewGroupTransitionConfig config) {
                cascade.transit(builder, config); //sets up builder.range()
                viewGroupTransition.transit(builder, config);
            }
        });
        return self();
    }

    @CheckResult(suggest = "The created ViewTransition should be utilized")
    @Override
    protected ViewTransition createTransition() {
        ViewTransition vt = new ViewTransition();

        vt.setTarget(mView);

        //TODO clone() is required since the class implements ViewTransition.Setup and passes itself to ViewTransition,
        // without clone ViewTransitions made from the same Builder will have their states intertwined
        vt.setSetup(clone());

        return vt;
    }

    @Override
    protected ViewTransitionBuilder self() {
        return this;
    }

    @Override
    public void setupAnimation(@NonNull TransitionControllerManager manager) {
        if (mView == null) {
            mView = manager.getTarget();
        }

        if (mDelayed != null) {
            for (int i = 0, size = mDelayed.size(); i < size; i++) {
                mDelayed.get(i).evaluate(manager.getTarget(), this);
            }
        }

        for (int i = 0, size = mSetupList.size(); i < size; i++) {
            mSetupList.get(i).setupAnimation(manager);
        }

        if (mCustomTransitionController != null) {
            mCustomTransitionController.setTarget(manager.getTarget());
            mCustomTransitionController.setRange(mStart, mEnd);
            manager.addTransitionController(mCustomTransitionController.clone());
        }

        ObjectAnimator anim = new ObjectAnimator();
        anim.setTarget(mView);
        anim.setValues(getValuesHolders());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(anim);
        animatorSet.setDuration(SCALE_FACTOR);
        manager.addAnimatorSetAsTransition(mView, animatorSet).setRange(mStart, mEnd);
    }

    protected ViewTransitionDelayedEvaluation getViewDelayedProcessor() {
        if (mViewDelayedProcessor == null) {
            mViewDelayedProcessor = new ViewTransitionDelayedEvaluation();
            addDelayedEvaluator(mViewDelayedProcessor);
        }
        return mViewDelayedProcessor;
    }

    /**
     * Performs evaluation on delayed operations.
     */
    protected static class ViewTransitionDelayedEvaluation implements DelayedEvaluator<ViewTransitionBuilder> {
        View[] views;
        float[] process;
        float[][] process2;

        ViewTransitionDelayedEvaluation() {
            views = new View[DELAYED_TOTAL];
            process = new float[DELAYED_TOTAL];
            for (int i = 0; i < DELAYED_TOTAL; i++) {
                process[i] = Float.MIN_VALUE;
            }
            process2 = new float[DELAYED_TOTAL][];
        }

        @Override
        public void evaluate(View view, ViewTransitionBuilder builder) {
            View targetView;
            float value;
            float[] orgValues;
            float[] newValues;
            value = process[HEIGHT];
            if (value != Float.MIN_VALUE) {
                builder.height((int) value);
            }

            value = process[TRANSLATION_X_AS_FRACTION_OF_WIDTH_WITH_VIEW];
            if (value != Float.MIN_VALUE) {
                builder.height((int) value);
                builder.translationX(views[TRANSLATION_X_AS_FRACTION_OF_WIDTH_WITH_VIEW].getWidth() * value);
            }
            if (process2[TRANSLATION_X_AS_FRACTIONS_OF_WIDTH_WITH_VIEW] != null) {
                orgValues = process2[TRANSLATION_X_AS_FRACTIONS_OF_WIDTH_WITH_VIEW];
                newValues = new float[orgValues.length + 1];
                targetView = views[TRANSLATION_X_AS_FRACTIONS_OF_WIDTH_WITH_VIEW];
                int width = (targetView != null ? targetView : view).getWidth();
                newValues[0] = width;
                for (int i = 0; i < orgValues.length; i++) {
                    newValues[i + 1] = width * orgValues[i];
                }
                builder.translationX(newValues);
            }

            value = process[TRANSLATION_Y_AS_FRACTION_OF_HEIGHT_WITH_VIEW];
            if (value != Float.MIN_VALUE) {
                builder.height((int) value);
                builder.translationY(views[TRANSLATION_Y_AS_FRACTION_OF_HEIGHT_WITH_VIEW].getHeight() * value);
            }
            if (process2[TRANSLATION_Y_AS_FRACTIONS_OF_HEIGHT_WITH_VIEW] != null) {
                orgValues = process2[TRANSLATION_Y_AS_FRACTIONS_OF_HEIGHT_WITH_VIEW];
                newValues = new float[orgValues.length + 1];
                targetView = views[TRANSLATION_Y_AS_FRACTIONS_OF_HEIGHT_WITH_VIEW];
                int height = (targetView != null ? targetView : view).getHeight();
                newValues[0] = height;
                for (int i = 0; i < orgValues.length; i++) {
                    newValues[i + 1] = height * orgValues[i];
                }
                builder.translationY(newValues);
            }
        }

        void addProcess(int type, int value) {
            process[type] = value;
        }

        void addProcess(int type, View view, int value) {
            process[type] = value;
            views[type] = view;
        }

        void addProcess(int type, float value) {
            process[type] = value;
        }

        void addProcess(int type, View view, float value) {
            process[type] = value;
            views[type] = view;
        }

        public void addProcess(int type, View view, float[] values) {
            process2[type] = values;
            views[type] = view;
        }

        @CheckResult
        protected DelayedProcessor clone() {
            try {
                DelayedProcessor dp = (DelayedProcessor) super.clone();
                dp.process = new float[TOTAL];
                System.arraycopy(process, 0, dp.process, 0, process.length);
                return dp;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Allows customized {@link ViewTransition} to be applied to each child view of a ViewGroup, see
     * {@link #transitViewGroup(ViewGroupTransition)} / {@link #transitViewGroup(ViewGroupTransition, Cascade)}
     */
    public interface ViewGroupTransition {
        /**
         * @param builder
         */
        void transit(ViewTransitionBuilder builder, ViewGroupTransitionConfig config);
    }

    /**
     * Encapsulates relevant data when transiting a ViewGroup, including total number of children,
     * the current View being processed, and its position within the ViewGroup.
     */
    public class ViewGroupTransitionConfig {
        public final ViewGroup parentViewGroup;
        public final int total;
        // the child view to be transitioned
        private View childView;
        private int index;

        /**
         * @param viewGroup the parent ViewGroup this view belongs to
         * @param total     total number of children
         */
        public ViewGroupTransitionConfig(ViewGroup viewGroup, int total) {
            this.parentViewGroup = viewGroup;
            this.total = total;
        }

        public ViewGroup getParentViewGroup() {
            return parentViewGroup;
        }

        public View getChildView() {
            return childView;
        }

        public int getIndex() {
            return index;
        }

        public int getChildrenCount() {
            return total;
        }
    }

    private static class HeightTransformer extends ScaledTransformer {
        private final int curHeight;
        private final int targetHeight;

        public HeightTransformer(int curHeight, int targetHeight) {
            this.curHeight = curHeight;
            this.targetHeight = targetHeight;
        }

        @Override
        public void updateViewScaled(TransitionController controller, View target, float scaledProgress) {
            if (controller.isReverse()) {
                target.getLayoutParams().height = (int) ((curHeight - targetHeight) * scaledProgress + targetHeight);
            } else {
                target.getLayoutParams().height = (int) ((targetHeight - curHeight) * scaledProgress + curHeight);
            }
            target.requestLayout();
        }
    }

    private static class BackgroundColorHsvTransformer extends ScaledTransformer {
        private final int fromColor;
        private final int toColor;

        public BackgroundColorHsvTransformer(int fromColor, int toColor) {
            this.fromColor = fromColor;
            this.toColor = toColor;
        }

        @Override
        public void updateViewScaled(TransitionController controller, View target, float scaledProgress) {
            //source: http://stackoverflow.com/questions/18216285/android-animate-color-change-from-color-to-color
            final float[] from = new float[3],
                    to = new float[3];
            Color.colorToHSV(fromColor, from);
            Color.colorToHSV(toColor, to);

            final float[] hsv = new float[3];
            // Transition along each axis of HSV (hue, saturation, value)
            hsv[0] = from[0] + (to[0] - from[0]) * scaledProgress;
            hsv[1] = from[1] + (to[1] - from[1]) * scaledProgress;
            hsv[2] = from[2] + (to[2] - from[2]) * scaledProgress;

            target.setBackgroundColor(Color.HSVToColor(hsv));
        }
    }
}
