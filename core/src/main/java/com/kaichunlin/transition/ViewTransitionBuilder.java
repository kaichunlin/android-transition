package com.kaichunlin.transition;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import com.kaichunlin.transition.adapter.ITransitionAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Allows the easy creation of {@link ViewTransition}
 * <p>
 * Created by Kai-Chun Lin on 2015/4/23.
 */
public class ViewTransitionBuilder extends BaseTransitionBuilder<ViewTransitionBuilder, ViewTransition> implements ViewTransition.Setup {

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
    public static ViewTransitionBuilder transit(View view) {
        return new ViewTransitionBuilder(view);
    }

    private List<ITransitionController> mTransitionControllersList = new ArrayList<>();
    private List<ViewTransition.Setup> mSetupList = new ArrayList<>();
    private View mView;
    private ITransitionAdapter mAdapter;

    private ViewTransitionBuilder() {
    }

    private ViewTransitionBuilder(View view) {
        mView = view;
    }

    /**
     * @param view the view the created {@link ViewTransition} should manipulate
     * @return
     */
    public ViewTransitionBuilder target(View view) {
        mView = view;
        return self();
    }

    /**
     * Adds a custom {@link ITransitionController}
     *
     * @param setup
     * @return
     */
    public ViewTransitionBuilder addTransitionController(ITransitionController setup) {
        mTransitionControllersList.add(setup);
        return self();
    }

    @Override
    public ViewTransitionBuilder alpha(float end) {
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
    public ViewTransitionBuilder scaleX(float end) {
        return scaleX(ViewHelper.getScaleX(mView), end);
    }

    @Override
    public ViewTransitionBuilder scaleY(float end) {
        return scaleY(ViewHelper.getScaleY(mView), end);
    }

    @Override
    public ViewTransitionBuilder scale(float end) {
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
        addDelayedEvaluator(new DelayedEvaluator() {
            @Override
            public void evaluate(View view, BaseTransitionBuilder builder) {
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
    public ViewTransitionBuilder translationXAsFractionOfWidth(final View targetView, final float percent) {
        return translationX(targetView.getWidth() * percent);
    }

    /**
     * @param targetView
     * @param percent
     * @return
     */
    public ViewTransitionBuilder delayTranslationXAsFractionOfWidth(final View targetView, final float percent) {
        addDelayedEvaluator(new DelayedEvaluator() {
            @Override
            public void evaluate(View view, BaseTransitionBuilder builder) {
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
        addDelayedEvaluator(new DelayedEvaluator() {
            @Override
            public void evaluate(View view, BaseTransitionBuilder builder) {
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
    public ViewTransitionBuilder translationYAsFractionOfHeight(final View targetView, final float percent) {
        return translationY(targetView.getHeight() * percent);
    }

    /**
     * @param targetView
     * @param percent
     * @return
     */
    public ViewTransitionBuilder delayTranslationYAsFractionOfHeight(final View targetView, final float percent) {
        addDelayedEvaluator(new DelayedEvaluator() {
            @Override
            public void evaluate(View view, BaseTransitionBuilder builder) {
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

    @Override
    public ViewTransitionBuilder clone() {
        ViewTransitionBuilder newCopy = (ViewTransitionBuilder) super.clone();
        newCopy.mTransitionControllersList = new ArrayList<>();
        newCopy.mTransitionControllersList.addAll(mTransitionControllersList);
        newCopy.mSetupList = new ArrayList<>();
        newCopy.mSetupList.addAll(mSetupList);
        return newCopy;
    }

    @Override
    public ViewTransitionBuilder reverse() {
        mHolders.clear();
        for (Map.Entry<String, ShadowValuesHolder> entry : mShadowHolders.entrySet()) {
            mHolders.put(entry.getKey(), entry.getValue().createReverse());
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
    public ViewTransitionBuilder addSetup(ViewTransition.Setup setup) {
        mSetupList.add(setup);
        return self();
    }

    /**
     * @param res
     * @param fromColorId
     * @param toColorId
     * @return
     */
    public ViewTransitionBuilder backgroundColorResource(Resources res, int fromColorId, int toColorId) {
        return backgroundColor(res.getColor(fromColorId), res.getColor(toColorId));
    }

    /**
     * @param fromColor
     * @param toColor
     * @return
     */
    public ViewTransitionBuilder backgroundColor(final int fromColor, final int toColor) {
        addSetup(new ViewTransition.Setup() {
            @Override
            public void setupAnimation(final TransitionManager transitionManager) {
                ObjectAnimator animator = ObjectAnimator.ofInt(transitionManager.getTarget(), "backgroundColor", fromColor, toColor);
                animator.setDuration(10_000);
                animator.setEvaluator(new ArgbEvaluator());
                transitionManager.addTransitionController(DefaultTransitionController.wrap(animator));
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
    public ViewTransitionBuilder backgroundColorResourceHSV(Resources res, int fromColorId, int toColorId) {
        return backgroundColorHSV(res.getColor(fromColorId), res.getColor(toColorId));
    }

    /**
     * @param fromColor
     * @param toColor
     * @return
     */
    public ViewTransitionBuilder backgroundColorHSV(final int fromColor, final int toColor) {
        addSetup(new ViewTransition.Setup() {
            @Override
            public void setupAnimation(final TransitionManager transitionManager) {
                //source: http://stackoverflow.com/questions/18216285/android-animate-color-change-from-color-to-color
                final float[] from = new float[3],
                        to = new float[3];
                Color.colorToHSV(fromColor, from);
                Color.colorToHSV(toColor, to);

                ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
                anim.setDuration(10_000);

                final float[] hsv = new float[3];
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        // Transition along each axis of HSV (hue, saturation, value)
                        hsv[0] = from[0] + (to[0] - from[0]) * animation.getAnimatedFraction();
                        hsv[1] = from[1] + (to[1] - from[1]) * animation.getAnimatedFraction();
                        hsv[2] = from[2] + (to[2] - from[2]) * animation.getAnimatedFraction();

                        transitionManager.getTarget().setBackgroundColor(Color.HSVToColor(hsv));
                    }
                });

                transitionManager.addTransitionController(DefaultTransitionController.wrap(anim));
            }
        });
        return self();
    }

    /**
     * @param vgt
     * @return
     */
    public ViewTransitionBuilder transitViewGroup(ViewGroupTransition vgt) {
        ViewGroup vg = (ViewGroup) mView;
        int total = vg.getChildCount();
        View view;
        for (int i = 0; i < total; i++) {
            view = vg.getChildAt(i);
            vgt.transit(target(view), vg, view, i, total);
        }
        return self();
    }

    /**
     * Sets the {@link ITransitionAdapter}, once set {@link build()} would automatically add
     * the created {@link ViewTransition} to the adapter.
     *
     * @param adapter
     * @return
     */
    public ViewTransitionBuilder adapter(ITransitionAdapter adapter) {
        mAdapter = adapter;
        return self();
    }

    @Override
    public ViewTransition createTransition() {
        ViewTransition vt = new ViewTransition();

        vt.setTarget(mView);

        //TODO clone() is required since the class implements ViewTransition.Setup and passes itself to ViewTransition, without clone ViewTransitions made from the same Builder will have their states intertwined
        vt.setSetup(clone());

        if (mAdapter != null) {
            mAdapter.addTransition(vt);
        }

        return vt;
    }

    @Override
    protected ViewTransitionBuilder self() {
        return this;
    }

    @Override
    public void setupAnimation(TransitionManager transitionManager) {
        if (mView == null) {
            mView = transitionManager.getTarget();
        }

        for (DelayedEvaluator de : mDelayed) {
            de.evaluate(transitionManager.getTarget(), this);
        }

        for (ViewTransition.Setup setup : mSetupList) {
            setup.setupAnimation(transitionManager);
        }

        for (ITransitionController setup : mTransitionControllersList) {
            setup.setTarget(transitionManager.getTarget());
            transitionManager.addTransitionController(setup.clone());
        }

        ObjectAnimator anim = new ObjectAnimator();
        anim.setTarget(mView);
        anim.setValues(mHolders.values().toArray(new PropertyValuesHolder[0]));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(anim);
        animatorSet.setDuration(SCALE_FACTOR);
        transitionManager.addAnimatorSetAsTransition(mView, animatorSet).setRange(mStart, mEnd);
    }

    /**
     * Allows customized {@link ViewTransition} to be applied to each child view of a ViewGroup
     */
    public interface ViewGroupTransition {
        void transit(ViewTransitionBuilder builder, ViewGroup viewGroup, View view, int index, int total);
    }
}
