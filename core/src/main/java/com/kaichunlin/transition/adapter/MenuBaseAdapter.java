package com.kaichunlin.transition.adapter;

import android.app.Activity;
import android.view.Menu;

/**
 * Adds support for {@link android.view.MenuItem} transition
 * <p>
 * Created by Kai-Chun Lin on 2015/5/11.
 */
public abstract class MenuBaseAdapter extends BaseAdapter {
    MenuOptionHandler mMenuHandler;

    protected abstract MenuOptionHandler createMenuHandler();

    @Override
    public void clearTransition() {
        super.clearTransition();
        createMenuHandlerIfNecessary();
        mMenuHandler.clearOptions();
    }

    protected MenuOptionHandler getMenuOptionHandler() {
        return mMenuHandler;
    }

    public void onCreateOptionsMenu(Activity activity, Menu menu) {
        onCreateOptionsMenu(activity, menu, mMenuHandler.getAdapterState());
    }

    protected void onCreateOptionsMenu(Activity activity, Menu menu, MenuOptionHandler.AdapterState adapterState) {
        createMenuHandlerIfNecessary();
        mMenuHandler.onCreateOptionsMenu(activity, menu, adapterState);
    }

    private void createMenuHandlerIfNecessary() {
        if(mMenuHandler==null) {
            mMenuHandler = createMenuHandler();
        }
    }

    /**
     * Sets up MenuOptionConfiguration for the open state,
     *
     * @param activity
     * @param openConfig
     */
    public void setupOption(Activity activity, MenuOptionConfiguration openConfig) {
        createMenuHandlerIfNecessary();
        mMenuHandler.setupOption(activity, openConfig);
    }

    public void setupOpenOption(Activity activity, MenuOptionConfiguration openConfig) {
        createMenuHandlerIfNecessary();
        mMenuHandler.setupOpenOption(activity, openConfig);
    }

    public void setupCloseOption(Activity activity, MenuOptionConfiguration closeConfig) {
        createMenuHandlerIfNecessary();
        mMenuHandler.setupCloseOption(activity, closeConfig);
    }

    public void setupOptions(Activity activity, MenuOptionConfiguration openConfig, MenuOptionConfiguration closeConfig) {
        createMenuHandlerIfNecessary();
        mMenuHandler.setupOptions(activity, openConfig, closeConfig);
    }
}
