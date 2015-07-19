package com.kaichunlin.transition.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;

/**
 * Created by Kai on 2015/7/10.
 */
public interface MenuOptionHandler {
    AdapterState getAdapterState();

    /**
     * Syncs current state of Menu with transitions
     *
     * @param activity
     * @param menu
     */
    void onCreateOptionsMenu(@NonNull Activity activity, @NonNull Menu menu);

    /**
     * Syncs current state of Menu with transitions
     *
     * @param activity
     * @param menu
     * @param adapterState a different {@link AdapterState} than the one retrieved by calling getAdapterState()
     */
    void onCreateOptionsMenu(@NonNull Activity activity, @NonNull Menu menu, @NonNull AdapterState adapterState);

    /**
     * Sets transitions to {@link android.view.MenuItem} in both the opened state and closed state with only
     * the opened state's transition specified, which is used to build menu transition in the closed state
     *
     * @param activity
     * @param openConfig
     */
    void setupOption(@NonNull Activity activity, @Nullable MenuOptionConfiguration openConfig);

    /**
     * Sets transitions to {@link android.view.MenuItem} in the opened state
     *
     * @param activity
     * @param openConfig
     */
    void setupOpenOption(@NonNull Activity activity, @Nullable MenuOptionConfiguration openConfig);

    /**
     * Sets transitions to {@link android.view.MenuItem} in the closed state
     *
     * @param activity
     * @param closeConfig
     */
    void setupCloseOption(@NonNull Activity activity, @Nullable MenuOptionConfiguration closeConfig);

    /**
     * Sets transitions to {@link android.view.MenuItem} in both the opened state and closed state
     *
     * @param activity
     * @param openConfig
     * @param closeConfig
     */
    void setupOptions(@NonNull Activity activity, @Nullable MenuOptionConfiguration openConfig, @Nullable MenuOptionConfiguration closeConfig);

    /**
     * Clears menu transition
     */
    void clearOptions();

    MenuOptionConfiguration getOpenConfig();

    MenuOptionConfiguration getCloseConfig();
}
