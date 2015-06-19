package com.kaichunlin.transition;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.kaichunlin.transition.util.TransitionStateHolder;
import com.kaichunlin.transition.util.ViewUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides transition support to a MenuItem
 * <p/>
 * Created by Kai-Chun Lin on 2015/4/18.
 */
public class MenuItemTransition extends BaseTransition<MenuItemTransition, MenuItemTransition.Setup> {
    private List<TransitionManager> mTransittingMenuItems = new ArrayList<>();
    private final Toolbar mToolbar;
    private boolean mStarted;
    private boolean mSetVisibleOnStartTransition;
    private boolean mInvalidateOptionOnStopTransition;
    private WeakReference<Activity> mActivityRef;

    public MenuItemTransition(Toolbar toolbar) {
        this(null, toolbar, null);
    }

    public MenuItemTransition(String id, Toolbar toolbar) {
        this(id, toolbar, null);
    }

    public MenuItemTransition(Toolbar toolbar, View view) {
        this(null, toolbar, view);
    }

    public MenuItemTransition(String id, Toolbar toolbar, View view) {
        super(id);
        this.mToolbar = toolbar;
        this.mTarget = view;
    }

    /**
     * @param setVisibleOnStartAnimation should the visibility of the target MenuItem be forcibly set when {@link #startTransition()} is called
     * @return
     */
    public MenuItemTransition setVisibleOnStartAnimation(boolean setVisibleOnStartAnimation) {
        this.mSetVisibleOnStartTransition = setVisibleOnStartAnimation;
        return self();
    }

    @Override
    public MenuItemTransition reverse() {
        super.reverse();
        mTransittingMenuItems.clear();
        return self();
    }

    @Override
    public boolean startTransition() {
        if (mStarted) {
            return false;
        }
        if (mSetVisibleOnStartTransition) {
            mToolbar.getMenu().setGroupVisible(0, true);
        }
        if (mTransittingMenuItems.size() == 0) {
            mTransittingMenuItems.clear();
            List<MenuItem> list = ViewUtil.getVisibleMenuItemList(mToolbar);
            for (int i = 0; i < list.size(); i++) {
                MenuItem menuItem = list.get(i);
                TransitionManager transitionManager = new TransitionManager(getId());
                if (mInterpolator != null) {
                    transitionManager.setInterpolator(mInterpolator);
                }
                mSetup.setupAnimation(menuItem, transitionManager, i, list.size());
                View view;
                if (mTarget == null) {
                    view = LayoutInflater.from(mToolbar.getContext()).inflate(R.layout.menu_animation, null).findViewById(R.id.menu_animation);
                    ((ImageView) view).setImageDrawable(menuItem.getIcon());
                } else {
                    view = mTarget;
                }
                if (TransitionConfig.isDebug()) {
                    view.setTag(R.id.debug_id, new TransitionStateHolder(getId()));
                }
                if (mReverse) {
                    transitionManager.reverse();
                }
                menuItem.setActionView(view);
                transitionManager.setTarget(view);
                transitionManager.start();
                mTransittingMenuItems.add(transitionManager);
            }
        }
        mStarted = true;
        return true;
    }

    @Override
    public void updateProgress(float progress) {
        for (TransitionManager animator : mTransittingMenuItems) {
            animator.updateProgress(progress);
        }
    }

    @Override
    public void stopTransition() {
        for (TransitionManager ani : mTransittingMenuItems) {
            ani.end();
        }
        List<MenuItem> list = ViewUtil.getVisibleMenuItemList(mToolbar);
        for (MenuItem menuItem : list) {
            menuItem.setActionView(null);
        }
        mTransittingMenuItems.clear();
        if (mInvalidateOptionOnStopTransition) {
            Activity activity = getActivity();
            if (activity != null) {
                activity.invalidateOptionsMenu();
            }
        }
        mStarted = false;
    }

    private Activity getActivity() {
        return mActivityRef == null ? null : mActivityRef.get();
    }

    /**
     * @return should {@link Activity#invalidateOptionsMenu()} be called when transition stops
     */
    public boolean isInvalidateOptionOnStopTransition() {
        return mInvalidateOptionOnStopTransition;
    }

    /**
     * Sets whether or not to call {@link Activity#invalidateOptionsMenu()} after a transition stops.
     *
     * @param activity                        Activity that should have its {@link Activity#invalidateOptionsMenu()} method called, or null if invalidateOptionOnStopAnimation parameter is false
     * @param invalidateOptionOnStopAnimation
     * @return
     */
    public MenuItemTransition setInvalidateOptionOnStopTransition(Activity activity, boolean invalidateOptionOnStopAnimation) {
        this.mActivityRef = new WeakReference<>(activity);
        this.mInvalidateOptionOnStopTransition = invalidateOptionOnStopAnimation;
        return self();
    }

    @Override
    public MenuItemTransition clone() {
        MenuItemTransition newCopy = (MenuItemTransition) super.clone();
        newCopy.mTransittingMenuItems = new ArrayList<>();
        for (TransitionManager tm : mTransittingMenuItems) {
            newCopy.mTransittingMenuItems.add(tm.clone());
        }
        return newCopy;
    }

    @Override
    protected void invalidate() {

    }

    @Override
    protected MenuItemTransition self() {
        return this;
    }

    /**
     * Represents an object that will create {@link ITransitionController} Objects to be added to a {@link TransitionManager}
     */
    public interface Setup extends BaseTransition.Setup {
        /**
         * Create one or more {@link ITransitionController} for each {@link android.view.MenuItem} and add them to transitionManager
         *
         * @param mMenuItem
         * @param transitionManager
         * @param itemIndex
         * @param menuCount
         */
        void setupAnimation(MenuItem mMenuItem, TransitionManager transitionManager, int itemIndex, int menuCount);
    }
}
