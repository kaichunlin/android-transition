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
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.kaichunlin.transition.internal.CustomTransitionController;
import com.kaichunlin.transition.internal.DefaultTransitionController;
import com.kaichunlin.transition.internal.TransitionController;
import com.kaichunlin.transition.internal.TransitionControllerManager;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorInflater;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows the easy creation of {@link ViewTransition}
 * <p>
 * Created by Kai-Chun Lin on 2015/4/23.
 */
public class ViewTransitionBuilder extends AbstractTransitionBuilder<ViewTransitionBuilder, ViewTransition> implements ViewTransition.Setup {

    /**
     * Creates a {@link ViewTransitionBuilder} instance with no target view set
     *
     * @return
     */
    public static ViewTransitionBuilder transit() {
        return new ViewTransitionBuilder();
    }

    /**
     * Creates  a {@link ViewTransitionBuilder} instance with the target view set
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
        mView = view;
    }

    /**
     * @param view the view the created {@link ViewTransition} should manipulate
     * @return
     */
    public ViewTransitionBuilder target(@Nullable View view) {
        mView = view;
        return self();
    }

    /**
     * Adds a custom {@link TransitionHandler}
     *
     * @param transitionHandler
     * @return
     */
    public ViewTransitionBuilder addTransitionHandler(@NonNull TransitionHandler transitionHandler) {
        if (mCustomTransitionController == null) {
            mCustomTransitionController = new CustomTransitionController();
        }
        mCustomTransitionController.addTransitionHandler(transitionHandler);
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
     * @param percent
     * @return
     */
    public ViewTransitionBuilder translationXAsFractionOfWidth(final float percent) {
        return translationX(mView.getWidth() * percent);
    }

    /**
     * @param percent
     * @return
     */
    public ViewTransitionBuilder delayTranslationXAsFractionOfWidth(final float percent) {
        final View mView = this.mView;
        addDelayedEvaluator(new DelayedEvaluator<ViewTransitionBuilder>() {
            @Override
            public void evaluate(View view, ViewTransitionBuilder builder) {
                builder.translationX(mView.getWidth() * percent);
            }
        });
        return self();
    }

    /**
     * @param targetView
     * @param percent
     * @return
     */
    public ViewTransitionBuilder translationXAsFractionOfWidth(@NonNull final View targetView, final float percent) {
        return translationX(targetView.getWidth() * percent);
    }

    /**
     * @param targetView
     * @param percent
     * @return
     */
    public ViewTransitionBuilder delayTranslationXAsFractionOfWidth(@NonNull final View targetView, final float percent) {
        addDelayedEvaluator(new DelayedEvaluator<ViewTransitionBuilder>() {
            @Override
            public void evaluate(View view, ViewTransitionBuilder builder) {
                builder.translationX(targetView.getWidth() * percent);
            }
        });
        return self();
    }

    @Override
    public ViewTransitionBuilder translationY(float end) {
        return translationY(ViewHelper.getTranslationY(mView), end);
    }

    /**
     * @param percent
     * @return
     */
    public ViewTransitionBuilder translationYAsFractionOfHeight(final float percent) {
        return translationY(mView.getHeight() * percent);
    }

    /**
     * @param percent
     * @return
     */
    public ViewTransitionBuilder delayTranslationYAsFractionOfHeight(final float percent) {
        final View mView = this.mView;
        addDelayedEvaluator(new DelayedEvaluator<ViewTransitionBuilder>() {
            @Override
            public void evaluate(View view, ViewTransitionBuilder builder) {
                builder.translationY(mView.getHeight() * percent);
            }
        });
        return self();
    }

    /**
     * @param targetView
     * @param percent
     * @return
     */
    public ViewTransitionBuilder translationYAsFractionOfHeight(@NonNull final View targetView, final float percent) {
        return translationY(targetView.getHeight() * percent);
    }

    /**
     * @param targetView
     * @param percent
     * @return
     */
    public ViewTransitionBuilder delayTranslationYAsFractionOfHeight(@NonNull final View targetView, final float percent) {
        addDelayedEvaluator(new DelayedEvaluator<ViewTransitionBuilder>() {
            @Override
            public void evaluate(View view, ViewTransitionBuilder builder) {
                builder.translationY(targetView.getHeight() * percent);
            }
        });
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
        addTransitionHandler(new HeightTransitionHandler(fromHeight, targetHeight));
        return self();
    }

    public ViewTransitionBuilder delayHeight(@IntRange(from = 0) final int targetHeight) {
        addDelayedEvaluator(new DelayedEvaluator<ViewTransitionBuilder>() {
            @Override
            public void evaluate(View view, ViewTransitionBuilder builder) {
                builder.height(targetHeight);
            }
        });
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
     * @param setup
     * @return
     */
    public ViewTransitionBuilder addSetup(@NonNull ViewTransition.Setup setup) {
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
            public void setupAnimation(final TransitionControllerManager transitionControllerManager) {
                ObjectAnimator animator = ObjectAnimator.ofInt(transitionControllerManager.getTarget(), "backgroundColor", fromColor, toColor);
                animator.setDuration(10_000);
                animator.setEvaluator(new ArgbEvaluator());
                transitionControllerManager.addTransitionController(DefaultTransitionController.wrapAnimator(animator));
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
        addTransitionHandler(new BackgroundColorHsvTransitionHandler(fromColor, toColor));
        return self();
    }

    /**
     * TODO Current support is rudimentary, may expand support if there are enough demand for this
     * <p>
     * Converts an animator to ITransition when built, note that not all functions of Animator are supported.
     * <p>
     * Non-working functions: repeatMode, repeatCount, delay, duration (when in a set), Interpolator.
     * <p>
     * Furthermore, {@link #transitViewGroup(ViewGroupTransition)} does not work with this method
     *
     * @param animator
     * @return
     */
    public ViewTransitionBuilder animator(@NonNull final Animator animator) {
        addSetup(new ViewTransition.Setup() {
            @Override
            public void setupAnimation(TransitionControllerManager transitionControllerManager) {
                Animator animator2 = animator.clone();
                if (animator2 instanceof AnimatorSet) {
                    transitionControllerManager.addTransitionController(DefaultTransitionController.wrapAnimatorSet((AnimatorSet) animator2));
                } else {
                    transitionControllerManager.addTransitionController(DefaultTransitionController.wrapAnimator(animator2));
                }
            }
        });
        return self();
    }

    /**
     * See animator(Animator)
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
     * Throws ClassCastException if the target view is not a ViewGroup. Apply the specified {@link ViewGroupTransition} to all the children views of the target view.
     *
     * @param viewGroupTransition
     * @return
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
     * Similar to {@link #transitViewGroup(ViewGroupTransition)}, but with builder.range() auto filled for each child View
     *
     * @param viewGroupTransition
     * @param cascade
     * @return
     */
    public ViewTransitionBuilder transitViewGroup(@NonNull final ViewGroupTransition viewGroupTransition, @NonNull final Cascade cascade) {
        transitViewGroup(new ViewGroupTransition() {
            @Override
            public void transit(ViewTransitionBuilder builder, ViewGroupTransitionConfig config) {
                float fraction = (float) config.index / (config.total + 1) * cascade.transitionEnd;
                if (cascade.reverse) {
                    fraction = cascade.transitionEnd - fraction;
                }
                float offset = cascade.interpolator.getInterpolation(fraction) * (cascade.cascadeEnd - cascade.cascadeStart);
                builder.range(cascade.cascadeStart + offset, cascade.transitionEnd);
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
    public void setupAnimation(@NonNull TransitionControllerManager transitionControllerManager) {
        if (mView == null) {
            mView = transitionControllerManager.getTarget();
        }

        for (int i = 0, size = mDelayed.size(); i < size; i++) {
            mDelayed.get(i).evaluate(transitionControllerManager.getTarget(), this);
        }

        for (int i = 0, size = mSetupList.size(); i < size; i++) {
            mSetupList.get(i).setupAnimation(transitionControllerManager);
        }

        if (mCustomTransitionController != null) {
            mCustomTransitionController.setTarget(transitionControllerManager.getTarget());
            mCustomTransitionController.setRange(mStart, mEnd);
            transitionControllerManager.addTransitionController(mCustomTransitionController.clone());
        }

        ObjectAnimator anim = new ObjectAnimator();
        anim.setTarget(mView);
        anim.setValues(getValuesHolders());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(anim);
        animatorSet.setDuration(SCALE_FACTOR);
        transitionControllerManager.addAnimatorSetAsTransition(mView, animatorSet).setRange(mStart, mEnd);
    }

    /**
     * Allows customized {@link ViewTransition} to be applied to each child view of a ViewGroup
     */
    public interface ViewGroupTransition {
        /**
         * @param builder
         */
        void transit(ViewTransitionBuilder builder, ViewGroupTransitionConfig config);
    }

    /**
     * Encapsulates relevant data when transiting a ViewGroup
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

    /**
     * Controls how cascading effect should be applied
     */
    public static class Cascade {
        public Interpolator interpolator;
        public float cascadeStart;
        public float cascadeEnd;
        public float transitionEnd = 1f; //end of transition effect
        public boolean reverse;

        /**
         * @param cascadeEnd this value should never be 1
         */
        public Cascade(float cascadeEnd) {
            this(cascadeEnd, new LinearInterpolator());
        }

        /**
         * @param reverse  should be effect be applied in reverse, i.e. last child first
         * @param cascadeEnd this value should never be 1
         */
        public Cascade(boolean reverse, float cascadeEnd) {
            this(reverse, cascadeEnd, new LinearInterpolator());
        }

        /**
         * @param cascadeEnd   this value should never be 1
         * @param interpolator
         */
        public Cascade(float cascadeEnd, Interpolator interpolator) {
            this.cascadeEnd = cascadeEnd;
            this.interpolator = interpolator;
        }

        /**
         * @param reverse  should be effect be applied in reverse, i.e. last child first
         * @param cascadeEnd   this value should never be 1
         * @param interpolator
         */
        public Cascade(boolean reverse, float cascadeEnd, Interpolator interpolator) {
            this.cascadeEnd = cascadeEnd;
            this.interpolator = interpolator;
            this.reverse = reverse;
        }
    }

    private static class HeightTransitionHandler extends ScaledTransitionHandler {
        private final int curHeight;
        private final int targetHeight;

        public HeightTransitionHandler(int curHeight, int targetHeight) {
            this.curHeight = curHeight;
            this.targetHeight = targetHeight;
        }

        @Override
        public void onUpdateScaledProgress(TransitionController controller, View target, float modifiedProgress) {
            if (controller.isReverse()) {
                target.getLayoutParams().height = (int) ((curHeight - targetHeight) * modifiedProgress + targetHeight);
            } else {
                target.getLayoutParams().height = (int) ((targetHeight - curHeight) * modifiedProgress + curHeight);
            }
            target.requestLayout();
        }
    }

    private static class BackgroundColorHsvTransitionHandler extends ScaledTransitionHandler {
        private final int fromColor;
        private final int toColor;

        public BackgroundColorHsvTransitionHandler(int fromColor, int toColor) {
            this.fromColor = fromColor;
            this.toColor = toColor;
        }

        @Override
        public void onUpdateScaledProgress(TransitionController controller, View target, float modifiedProgress) {
            //source: http://stackoverflow.com/questions/18216285/android-animate-color-change-from-color-to-color
            final float[] from = new float[3],
                    to = new float[3];
            Color.colorToHSV(fromColor, from);
            Color.colorToHSV(toColor, to);

            final float[] hsv = new float[3];
            // Transition along each axis of HSV (hue, saturation, value)
            hsv[0] = from[0] + (to[0] - from[0]) * modifiedProgress;
            hsv[1] = from[1] + (to[1] - from[1]) * modifiedProgress;
            hsv[2] = from[2] + (to[2] - from[2]) * modifiedProgress;

            target.setBackgroundColor(Color.HSVToColor(hsv));
        }
    }
}
