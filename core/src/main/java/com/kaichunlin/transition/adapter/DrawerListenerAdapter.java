package com.kaichunlin.transition.adapter;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

/**
 * Adapter for DrawerListener, the transition range goes from 0.0f to 1.0f, where 0.0f is the closed state and 1.0f is the opened state.
 * <p>
 * Created by Kai-Chun Lin on 2015/4/14.
 */
public class DrawerListenerAdapter extends MenuBaseAdapter implements DrawerLayout.DrawerListener {
    private final ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout.DrawerListener mDrawerListener;
    private int mDrawerId;
    private DrawerLayout mDrawerLayout;

    public DrawerListenerAdapter(@NonNull ActionBarDrawerToggle mDrawerToggle) {
        this(mDrawerToggle, 0);
    }

    public DrawerListenerAdapter(@NonNull ActionBarDrawerToggle mDrawerToggle, @IdRes int mDrawerId) {
        this.mDrawerToggle = mDrawerToggle;
        this.mDrawerId = mDrawerId;
    }

    /**
     * @param activity
     * @return is the drawer in the opened state
     */
    public boolean isOpened(@NonNull Activity activity) {
        return mDrawerLayout.isDrawerOpen(activity.findViewById(mDrawerId));
    }

    @Override
    public void onDrawerOpened(@NonNull View view) {
        mDrawerToggle.onDrawerOpened(view);
        getAdapterState().setState(AdapterState.OPEN);
        stopTransition();

        if (mDrawerListener != null) {
            mDrawerListener.onDrawerOpened(view);
        }
    }

    @Override
    public void onDrawerClosed(@NonNull View view) {
        mDrawerToggle.onDrawerClosed(view);
        getAdapterState().setState(AdapterState.CLOSE);
        stopTransition();

        if (mDrawerListener != null) {
            mDrawerListener.onDrawerClosed(view);
        }
    }

    @Override
    public void onDrawerStateChanged(int state) {
        mDrawerToggle.onDrawerStateChanged(state);
        switch (state) {
            case DrawerLayout.STATE_DRAGGING:
            case DrawerLayout.STATE_SETTLING:
                startTransition();
                break;
            case DrawerLayout.STATE_IDLE:
                stopTransition();
                break;
        }

        if (mDrawerListener != null) {
            mDrawerListener.onDrawerStateChanged(state);
        }
    }

    @Override
    public void onDrawerSlide(View view, float slideOffset) {
        getTransitionManager().updateProgress(slideOffset);

        if (view == null) {
            return;
        }
        mDrawerToggle.onDrawerSlide(view, slideOffset);

        if (mDrawerListener != null) {
            mDrawerListener.onDrawerSlide(view, slideOffset);
        }
    }

    /**
     * @param mDrawerListener
     */
    public void setDrawerListener(@Nullable DrawerLayout.DrawerListener mDrawerListener) {
        this.mDrawerListener = mDrawerListener;
    }

    /**
     * @param drawerLayout
     */
    public void setDrawerLayout(@NonNull DrawerLayout drawerLayout) {
        this.mDrawerLayout = drawerLayout;
        drawerLayout.setDrawerListener(this);
    }

    /**
     * @param drawerId Drawer view ID
     */
    public void setDrawerId(@IdRes int drawerId) {
        mDrawerId = drawerId;
    }

    @Override
    protected MenuOptionHandler createMenuHandler() {
        return new DefaultMenuOptionHandler(this, getAdapterState());
    }
}
