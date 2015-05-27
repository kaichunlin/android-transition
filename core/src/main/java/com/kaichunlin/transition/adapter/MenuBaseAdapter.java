package com.kaichunlin.transition.adapter;

import android.app.Activity;
import android.view.Menu;

/**
 * Adds support for {@link android.view.MenuItem} transition
 * <p>
 * Created by Kai-Chun Lin on 2015/5/11.
 */
public abstract class MenuBaseAdapter extends BaseAdapter {
    DefaultMenuOptionHandler mMenuHandler;

    @Override
    public void clearTransition() {
        super.clearTransition();
        mMenuHandler.clearOptions();
    }

    protected DefaultMenuOptionHandler getMenuOptionHandler() {
        return mMenuHandler;
    }

    public void onCreateOptionsMenu(Activity activity, Menu menu) {
        mMenuHandler.onCreateOptionsMenu(activity, menu);
    }

    /**
     * Sets up MenuOptionConfiguration for the open state,
     *
     * @param activity
     * @param openConfig
     */
    public void setupOption(Activity activity, MenuOptionConfiguration openConfig) {
        mMenuHandler.setupOption(activity, openConfig);
    }

    public void setupOpenOption(Activity activity, MenuOptionConfiguration openConfig) {
        mMenuHandler.setupOpenOption(activity, openConfig);
    }

    public void setupCloseOption(Activity activity, MenuOptionConfiguration closeConfig) {
        mMenuHandler.setupCloseOption(activity, closeConfig);
    }

    public void setupOptions(Activity activity, MenuOptionConfiguration openConfig, MenuOptionConfiguration closeConfig) {
        mMenuHandler.setupOptions(activity, openConfig, closeConfig);
    }
}
