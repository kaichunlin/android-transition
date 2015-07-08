package com.kaichunlin.transition.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;

import com.kaichunlin.transition.MenuItemTransition;

/**
 * Provides default support for handling MenuItem transitions
 * <p>
 * Created by Kai on 2015/5/10.
 */
public class MenuOptionHandler {
    private final ITransitionAdapter mAdapter;
    private final AdapterState mAdapterState;
    private MenuOptionConfiguration mOpenConfig;
    private MenuOptionConfiguration mCloseConfig;

    public MenuOptionHandler(@NonNull ITransitionAdapter adapter, @NonNull AdapterState adapterState) {
        mAdapter = adapter;
        mAdapterState = adapterState;
    }

    public AdapterState getAdapterState() {
        return mAdapterState;
    }

    /**
     * Is it the opened state, the definition of opened state is dependent on the ITransitionAdapter
     *
     * @param activity
     * @return
     */
    public boolean isOpened(@NonNull Activity activity) {
        return mAdapterState.isOpened(activity);
    }

    /**
     * Syncs current state of Menu with transitions
     *
     * @param activity
     * @param menu
     */
    public void onCreateOptionsMenu(@NonNull Activity activity, @NonNull Menu menu) {
        onCreateOptionsMenu(activity, menu, mAdapterState);
    }

    protected void onCreateOptionsMenu(@NonNull Activity activity, @NonNull Menu menu, @NonNull AdapterState adapterState) {
        if (adapterState.isOpened(activity)) {
            boolean hasOpen = mOpenConfig != null;
            if (hasOpen && mOpenConfig.getMenuId() > 0) {
                activity.getMenuInflater().inflate(mOpenConfig.getMenuId(), menu);
            }
            if (mCloseConfig != null) {
                if (hasOpen) {
                    mAdapter.removeTransition(mOpenConfig.getTransition());
                }
                mAdapter.addTransition(mCloseConfig.getTransition());
            }
        } else {
            boolean hasClose = mCloseConfig != null;
            if (hasClose && mCloseConfig.getMenuId() > 0) {
                activity.getMenuInflater().inflate(mCloseConfig.getMenuId(), menu);
            }
            if (mOpenConfig != null) {
                if (hasClose) {
                    mAdapter.removeTransition(mCloseConfig.getTransition());
                }
                mAdapter.addTransition(mOpenConfig.getTransition());
            }
        }
    }

    /**
     * Sets transitions to {@link android.view.MenuItem} in both the opened state and closed state with only
     * the opened state's transition specified, which is used to build menu transition in the closed state
     *
     * @param activity
     * @param openConfig
     */
    public void setupOption(@NonNull Activity activity, @Nullable MenuOptionConfiguration openConfig) {
        MenuItemTransition transition = openConfig.getTransition();
        setupOptions(activity, openConfig, new MenuOptionConfiguration(transition.clone().reverse(), openConfig.getMenuId()));
    }

    /**
     * Sets transitions to {@link android.view.MenuItem} in the opened state
     *
     * @param activity
     * @param openConfig
     */
    public void setupOpenOption(@NonNull Activity activity, @Nullable MenuOptionConfiguration openConfig) {
        MenuItemTransition transition = openConfig.getTransition();
        setupOptions(activity, openConfig, new MenuOptionConfiguration(transition.clone().reverse(), -1));
    }

    /**
     * Sets transitions to {@link android.view.MenuItem} in the closed state
     *
     * @param activity
     * @param closeConfig
     */
    public void setupCloseOption(@NonNull Activity activity, @Nullable MenuOptionConfiguration closeConfig) {
        MenuItemTransition transition = closeConfig.getTransition();
        setupOptions(activity, new MenuOptionConfiguration(transition.clone().reverse(), -1), closeConfig);
    }

    /**
     * Sets transitions to {@link android.view.MenuItem} in both the opened state and closed state
     *
     * @param activity
     * @param openConfig
     * @param closeConfig
     */
    public void setupOptions(@NonNull Activity activity, @Nullable MenuOptionConfiguration openConfig, @Nullable MenuOptionConfiguration closeConfig) {
        if (mOpenConfig != null) {
            mAdapter.removeTransition(mOpenConfig.getTransition());
        }
        if (mCloseConfig != null) {
            mAdapter.removeTransition(mCloseConfig.getTransition());
        }
        mOpenConfig = openConfig;
        mCloseConfig = closeConfig;

        if (activity != null && isOpened(activity)) {
            if (mCloseConfig != null) {
                if (mCloseConfig.getTransition().isInvalidateOptionOnStopTransition()) {
                    activity.invalidateOptionsMenu();
                }
                mAdapter.addTransition(mCloseConfig.getTransition());
            }
        } else {
            if (mOpenConfig != null) {
                if (mOpenConfig.getTransition().isInvalidateOptionOnStopTransition()) {
                    activity.invalidateOptionsMenu();
                }
                mAdapter.addTransition(mOpenConfig.getTransition());
            }
        }
    }

    /**
     * Clears menu transition
     */
    public void clearOptions() {
        setupOptions(null, null, null);
    }

    public interface AdapterState {
        boolean isOpened(Activity activity);
    }
}
