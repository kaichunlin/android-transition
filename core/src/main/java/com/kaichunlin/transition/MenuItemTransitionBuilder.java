package com.kaichunlin.transition;

import android.app.Activity;
import android.support.annotation.CheckResult;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.kaichunlin.transition.internal.TransitionControllerManager;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Builder for {@link MenuItemTransition}.
 */
public class MenuItemTransitionBuilder extends AbstractTransitionBuilder<MenuItemTransitionBuilder, MenuItemTransition> implements MenuItemTransition.Setup {

    /**
     * Creates a transition effects builder for Toolbar {@link MenuItem}s.
     *
     * @param toolbar
     * @return
     */
    public static MenuItemTransitionBuilder transit(@NonNull Toolbar toolbar) {
        return new MenuItemTransitionBuilder(toolbar);
    }

    /**
     * Creates a builder for Toolbar {@link MenuItem}s.
     *
     * @param id Same as calling {@link Transition#setId(String)}, set an ID for debugging purpose.
     * @param toolbar
     * @return
     */
    public static MenuItemTransitionBuilder transit(String id, @NonNull Toolbar toolbar) {
        return new MenuItemTransitionBuilder(id, toolbar);
    }


    /**
     * Creates a builder for a specific Toolbar {@link MenuItem}.
     *
     * @param menuId The ID of the MenuItem to have transition effect created.
     * @param toolbar
     * @return
     */
    public static MenuItemTransitionBuilder transit(@IdRes int menuId, @NonNull Toolbar toolbar) {
        return new MenuItemTransitionBuilder(menuId, toolbar);
    }

    /**
     * Creates a builder for a specific Toolbar {@link MenuItem}.
     *
     * @param id Same as calling {@link Transition#setId(String)}, set an ID for debugging purpose.
     * @param menuId The ID of the MenuItem to have transition effect created.
     * @param toolbar
     * @return
     */
    public static MenuItemTransitionBuilder transit(String id, @IdRes int menuId, @NonNull Toolbar toolbar) {
        return new MenuItemTransitionBuilder(id, menuId, toolbar);
    }

    private Toolbar mToolbar;
    private int mMenuId;
    private float mCascade;
    private boolean mVisibleOnStartTransition;
    private Activity mActivity;
    private boolean mInvalidateOptionOnStopAnimation;

    private MenuItemTransitionBuilder(@NonNull Toolbar toolbar) {
        mToolbar = toolbar;
    }

    private MenuItemTransitionBuilder(String id, @NonNull Toolbar toolbar) {
        mId = id;
        mToolbar = toolbar;
    }

    private MenuItemTransitionBuilder(int menuId, @NonNull Toolbar toolbar) {
        mMenuId = menuId;
        mToolbar = toolbar;
    }

    private MenuItemTransitionBuilder(String id, int menuId, @NonNull Toolbar toolbar) {
        mId = id;
        mMenuId = menuId;
        mToolbar = toolbar;
    }

    @Override
    public MenuItemTransitionBuilder alpha(@FloatRange(from = 0.0, to = 1.0) float end) {
        return alpha(1f, end);
    }

    @Override
    public MenuItemTransitionBuilder rotation(float end) {
        return rotation(0f, end);
    }

    @Override
    public MenuItemTransitionBuilder rotationX(float end) {
        return rotationX(0f, end);
    }

    @Override
    public MenuItemTransitionBuilder rotationY(float end) {
        return rotationY(0f, end);
    }

    @Override
    public MenuItemTransitionBuilder scaleX(@FloatRange(from = 0.0) float end) {
        return scaleX(0f, end);
    }

    @Override
    public MenuItemTransitionBuilder scaleY(@FloatRange(from = 0.0) float end) {
        return scaleY(0f, end);
    }

    @Override
    public MenuItemTransitionBuilder scale(@FloatRange(from = 0.0) float end) {
        return scale(0f, end);
    }

    @Override
    public MenuItemTransitionBuilder translationX(float end) {
        return translationX(0f, end);
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

    /**
     * Cascades the starting range of transition for each {@link android.view.MenuItem} such that the
     * first {@link android.view.MenuItem} will transit from <i>start</i>, the 2nd MenuItem will
     * transit from <i>start+cascade</i> and so on.
     *
     * @param cascade
     * @return
     */
    public MenuItemTransitionBuilder cascade(@FloatRange(from = 0.0) float cascade) {
        mCascade = cascade;
        return self();
    }

    /**
     * See {@link MenuItemTransition#setVisibleOnStartAnimation(boolean)}.
     *
     * @param visible
     * @return
     */
    public MenuItemTransitionBuilder visibleOnStartAnimation(boolean visible) {
        mVisibleOnStartTransition = visible;
        return self();
    }

    /**
     * See {@link MenuItemTransition#setInvalidateOptionOnStopTransition(Activity, boolean)}}.
     *
     * @param activity Activity that should have its invalidateOptionsMenu() method called, or null
     *                 if invalidate parameter is false
     * @param invalidate
     * @return
     */
    public MenuItemTransitionBuilder invalidateOptionOnStopTransition(@NonNull Activity activity, boolean invalidate) {
        mActivity = activity;
        mInvalidateOptionOnStopAnimation = invalidate;
        return self();
    }

    @CheckResult
    @Override
    public MenuItemTransitionBuilder clone() {
        MenuItemTransitionBuilder clone = (MenuItemTransitionBuilder) super.clone();
        return clone;
    }

    @CheckResult(suggest = "The created MenuItemTransition should be utilized")
    @Override
    protected MenuItemTransition createTransition() {
        MenuItemTransition transition = new MenuItemTransition(mId, mToolbar);
        if (mMenuId != 0) {
            transition.setMenuId(mMenuId);
        }
        //TODO clone() is required since the class implements ViewTransition.Setup and passes itself to ViewTransition, without clone ViewTransitions made from the same Builder will have their states intertwined
        transition.setSetup(clone());
        transition.setVisibleOnStartAnimation(mVisibleOnStartTransition);
        if (mActivity != null) {
            transition.setInvalidateOptionOnStopTransition(mActivity, mInvalidateOptionOnStopAnimation);
        }
        return transition;
    }

    @Override
    protected MenuItemTransitionBuilder self() {
        return this;
    }

    @Override
    public void setupAnimation(@NonNull MenuItem mMenuItem, @NonNull TransitionControllerManager manager,
                               @IntRange(from = 0) int itemIndex, @IntRange(from = 0) int menuCount) {
        if(mDelayed!=null) {
            final int size = mDelayed.size();
            for (int i = 0; i < size; i++) {
                mDelayed.get(i).evaluate(manager.getTarget(), this);
            }
        }

        ObjectAnimator anim = new ObjectAnimator();
        anim.setValues(getValuesHolders());
        AnimatorSet set = new AnimatorSet();
        set.play(anim);
        set.setStartDelay((long) (itemIndex * mCascade * SCALE_FACTOR));
        set.setDuration((long) (SCALE_FACTOR - itemIndex * mCascade * SCALE_FACTOR));
        manager.addAnimatorSetAsTransition(set).setRange(mStart, mEnd);
    }
}
