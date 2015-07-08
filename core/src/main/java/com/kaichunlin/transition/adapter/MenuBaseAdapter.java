package com.kaichunlin.transition.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
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

    public void onCreateOptionsMenu(@NonNull Activity activity, @NonNull Menu menu) {
        onCreateOptionsMenu(activity, menu, mMenuHandler.getAdapterState());
    }

    protected void onCreateOptionsMenu(@NonNull Activity activity, @NonNull Menu menu, @NonNull MenuOptionHandler.AdapterState adapterState) {
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
    public void setupOption(@NonNull Activity activity, MenuOptionConfiguration openConfig) {
        createMenuHandlerIfNecessary();
        mMenuHandler.setupOption(activity, openConfig);
    }

    public void setupOpenOption(@NonNull Activity activity, MenuOptionConfiguration openConfig) {
        createMenuHandlerIfNecessary();
        mMenuHandler.setupOpenOption(activity, openConfig);
    }

    public void setupCloseOption(@NonNull Activity activity, MenuOptionConfiguration closeConfig) {
        createMenuHandlerIfNecessary();
        mMenuHandler.setupCloseOption(activity, closeConfig);
    }

    public void setupOptions(@NonNull Activity activity, MenuOptionConfiguration openConfig, MenuOptionConfiguration closeConfig) {
        createMenuHandlerIfNecessary();
        mMenuHandler.setupOptions(activity, openConfig, closeConfig);
    }
}
