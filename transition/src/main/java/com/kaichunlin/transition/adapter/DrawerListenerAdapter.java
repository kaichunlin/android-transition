package com.kaichunlin.transition.adapter;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

import com.kaichunlin.transition.ITransition;

/**
 * Adapter for DrawerListener, the transition range goes from 0.0f to 1.0f, where 0.0f is the closed state and 1.0f is the opened state.
 * <p>
 * Created by Kai-Chun Lin on 2015/4/14.
 */
public class DrawerListenerAdapter extends MenuBaseAdapter implements DrawerLayout.DrawerListener {
    private final ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout.DrawerListener mDrawerListener;
    private boolean mOpened;
    private int mDrawerId;
    private DrawerLayout mDrawerLayout;

    public DrawerListenerAdapter(ActionBarDrawerToggle mDrawerToggle) {
        this(mDrawerToggle, 0);
    }
    public DrawerListenerAdapter(ActionBarDrawerToggle mDrawerToggle, int mDrawerId) {
        this.mDrawerToggle = mDrawerToggle;
        this.mDrawerId = mDrawerId;
        mMenuHandler = new DefaultMenuOptionHandler(this) {
            @Override
            boolean isOpened(Activity activity) {
                return DrawerListenerAdapter.this.isOpened(activity);
            }
        };
    }

    /**
     * @param activity
     * @return is the drawer in the opened state
     */
    public boolean isOpened(Activity activity) {
        return mDrawerLayout.isDrawerOpen(activity.findViewById(mDrawerId));
    }

    @Override
    public void onDrawerOpened(View view) {
        mDrawerToggle.onDrawerOpened(view);
        stopTransition();
        mOpened = true;

        if (mDrawerListener != null) {
            mDrawerListener.onDrawerOpened(view);
        }
    }

    @Override
    public void onDrawerClosed(View view) {
        mDrawerToggle.onDrawerClosed(view);
        stopTransition();
        mOpened = false;

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
    protected void startTransition() {
        startTransition(mOpened ? 1f : 0f);
    }

    @Override
    public void onDrawerSlide(View view, float slideOffset) {
        for (ITransition trans : mTransitionList.values()) {
            trans.updateProgress(slideOffset);
        }

        if (view == null) {
            return;
        }
        mDrawerToggle.onDrawerSlide(view, slideOffset);

        if (mDrawerListener != null) {
            mDrawerListener.onDrawerSlide(view, slideOffset);
        }
    }

    /**
     *
     * @param mDrawerListener
     */
    public void setDrawerListener(DrawerLayout.DrawerListener mDrawerListener) {
        this.mDrawerListener = mDrawerListener;
    }

    /**
     *
     * @param drawerLayout
     */
    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.mDrawerLayout = drawerLayout;
        drawerLayout.setDrawerListener(this);
    }

    /**
     *
     * @param drawerId Drawer view ID
     */
    public void setDrawerId(int drawerId) {
        mDrawerId = drawerId;
    }
}
