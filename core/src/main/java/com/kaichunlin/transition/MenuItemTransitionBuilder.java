package com.kaichunlin.transition;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.Interpolator;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;

/**
 * Allows the easy creation of {@link MenuItemTransition}
 * <p>
 * Created by Kai-Chun Lin on 2015/4/23.
 */
public class MenuItemTransitionBuilder extends BaseTransitionBuilder<MenuItemTransitionBuilder, MenuItemTransition> implements MenuItemTransition.Setup {

    /**
     *
     * @param toolbar
     * @return
     */
    public static MenuItemTransitionBuilder transit(Toolbar toolbar) {
        return new MenuItemTransitionBuilder(toolbar);
    }

    /**
     *
     * @param id
     * @param toolbar
     * @return
     */
    public static MenuItemTransitionBuilder transit(String id, Toolbar toolbar) {
        return new MenuItemTransitionBuilder(id, toolbar);
    }

    private Toolbar mToolbar;
    private float mCascade;
    private boolean mVisibleOnStartTransition;
    private Activity mActivity;
    private boolean mInvalidateOptionOnStopAnimation;

    private MenuItemTransitionBuilder(Toolbar toolbar) {
        mToolbar = toolbar;
    }

    private MenuItemTransitionBuilder(String id, Toolbar toolbar) {
        mId = id;
        mToolbar = toolbar;
    }

    @Override
    public MenuItemTransitionBuilder alpha(float start, float end) {
        transitFloat(ALPHA, start, end);
        return self();
    }

    @Override
    public MenuItemTransitionBuilder alpha(float end) {
        return alpha(1f, end);
    }

    @Override
    public MenuItemTransitionBuilder rotation(float start, float end) {
        transitFloat(ROTATION, start, end);
        return self();
    }

    @Override
    public MenuItemTransitionBuilder rotation(float end) {
        return rotation(0f, end);
    }

    @Override
    public MenuItemTransitionBuilder rotationX(float start, float end) {
        transitFloat(ROTATION_X, start, end);
        return self();
    }

    @Override
    public MenuItemTransitionBuilder rotationX(float end) {
        return rotationX(0f, end);
    }

    @Override
    public MenuItemTransitionBuilder rotationY(float start, float end) {
        transitFloat(ROTATION_Y, start, end);
        return self();
    }

    @Override
    public MenuItemTransitionBuilder rotationY(float end) {
        return rotationY(0f, end);
    }

    @Override
    public MenuItemTransitionBuilder scaleX(float start, float end) {
        transitFloat(SCALE_X, start, end);
        return self();
    }

    @Override
    public MenuItemTransitionBuilder scaleX(float end) {
        return scaleX(0f, end);
    }

    @Override
    public MenuItemTransitionBuilder scaleY(float start, float end) {
        transitFloat(SCALE_Y, start, end);
        return self();
    }

    @Override
    public MenuItemTransitionBuilder scaleY(float end) {
        return scaleY(0f, end);
    }

    @Override
    public MenuItemTransitionBuilder scale(float start, float end) {
        transitFloat(SCALE_X, start, end);
        transitFloat(SCALE_Y, start, end);
        return self();
    }

    @Override
    public MenuItemTransitionBuilder scale(float end) {
        return scale(0f, end);
    }

    @Override
    public MenuItemTransitionBuilder translationX(float start, float end) {
        transitFloat(TRANSLATION_X, start, end);
        return self();
    }

    @Override
    public MenuItemTransitionBuilder translationX(float end) {
        return translationX(0f, end);
    }

    @Override
    public MenuItemTransitionBuilder translationY(float start, float end) {
        transitFloat(TRANSLATION_Y, start, end);
        return self();
    }

    @Override
    public MenuItemTransitionBuilder translationY(float end) {
        return translationY(0f, end);
    }

    @Override
    public MenuItemTransitionBuilder x(float end) {
        return x(0f, end);
    }

    @Override
    public MenuItemTransitionBuilder y(float end) {
        return y(0f, end);
    }

    @Override
    public MenuItemTransitionBuilder transitFloat(String property, float start, float end) {
        mHolders.put(property, PropertyValuesHolder.ofFloat(property, start, end));
        return self();
    }

    @Override
    public MenuItemTransitionBuilder transitInt(String property, int start, int end) {
        mHolders.put(property, PropertyValuesHolder.ofInt(property, start, end));
        return self();
    }

    /**
     * Cascades the starting range of transition for each {@link android.view.MenuItem} such that the first {@link android.view.MenuItem} will transit from <i>start</i>, the 2nd MenuItem will transit from <i>start+cascade</i> and so on
     *
     * @param cascade
     * @return
     */
    public MenuItemTransitionBuilder cascade(float cascade) {
        mCascade = cascade;
        return self();
    }

    /**
     * See {@link MenuItemTransition#setVisibleOnStartAnimation(boolean)}
     *
     * @param visibleOnStartTransition
     * @return
     */
    public MenuItemTransitionBuilder visibleOnStartAnimation(boolean visibleOnStartTransition) {
        mVisibleOnStartTransition = visibleOnStartTransition;
        return self();
    }

    /**
     * See {@link MenuItemTransition#setInterpolator(Interpolator)}
     *
     * @param activity Activity that should have its invalidateOptionsMenu() method called, or null if invalidateOptionOnStopAnimation parameter is false
     * @param invalidateOptionOnStopAnimation
     * @return
     */
    public MenuItemTransitionBuilder invalidateOptionOnStopTransition(Activity activity, boolean invalidateOptionOnStopAnimation) {
        mActivity = activity;
        mInvalidateOptionOnStopAnimation = invalidateOptionOnStopAnimation;
        return self();
    }

    @Override
    public MenuItemTransitionBuilder clone() {
        MenuItemTransitionBuilder clone = (MenuItemTransitionBuilder) super.clone();
        return clone;
    }

    @Override
    public MenuItemTransition createTransition() {
        MenuItemTransition vt = new MenuItemTransition(mId, mToolbar);
        vt.setId(mId);
        //TODO clone() is required since the class implements ViewTransition.Setup and passes itself to ViewTransition, without clone ViewTransitions made from the same Builder will have their states intertwined
        vt.setSetup(clone());
        vt.setVisibleOnStartAnimation(mVisibleOnStartTransition);
        if (mActivity != null) {
            vt.setInvalidateOptionOnStopTransition(mActivity, mInvalidateOptionOnStopAnimation);
        }
        return vt;
    }

    @Override
    protected MenuItemTransitionBuilder self() {
        return this;
    }

    @Override
    public void setupAnimation(MenuItem mMenuItem, TransitionManager transitionManager, int itemIndex, int menuCount) {
        for (DelayedEvaluator de : mDelayed) {
            de.evaluate(transitionManager.getTarget(), this);
        }

        ObjectAnimator anim = new ObjectAnimator();
        anim.setValues(mHolders.values().toArray(new PropertyValuesHolder[0]));
        AnimatorSet set = new AnimatorSet();
        set.play(anim);
        set.setStartDelay((long) (itemIndex * mCascade * SCALE_FACTOR));
        set.setDuration((long) (SCALE_FACTOR - itemIndex * mCascade * SCALE_FACTOR));
        transitionManager.addAnimatorSetAsTransition(set).setRange(mStart, mEnd);
    }
}
