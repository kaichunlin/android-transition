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
public class DefaultMenuOptionHandler implements IMenuOptionHandler {
    private final ITransitionAdapter mAdapter;
    private final AdapterState mAdapterState;
    private MenuOptionConfiguration mOpenConfig;
    private MenuOptionConfiguration mCloseConfig;

    public DefaultMenuOptionHandler(@NonNull ITransitionAdapter adapter, @NonNull AdapterState adapterState) {
        mAdapter = adapter;
        mAdapterState = adapterState;
    }

    @Override
    public AdapterState getAdapterState() {
        return mAdapterState;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Activity activity, @NonNull Menu menu) {
        onCreateOptionsMenu(activity, menu, mAdapterState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Activity activity, @NonNull Menu menu, @NonNull AdapterState adapterState) {
        if (adapterState.isOpen()) {
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

    @Override
    public void setupOption(@NonNull Activity activity, @Nullable MenuOptionConfiguration openConfig) {
        MenuItemTransition transition = openConfig.getTransition();
        setupOptions(activity, openConfig, new MenuOptionConfiguration(transition.clone().reverse(), openConfig.getMenuId()));
    }

    @Override
    public void setupOpenOption(@NonNull Activity activity, @Nullable MenuOptionConfiguration openConfig) {
        MenuItemTransition transition = openConfig.getTransition();
        setupOptions(activity, openConfig, new MenuOptionConfiguration(transition.clone().reverse(), -1));
    }

    @Override
    public void setupCloseOption(@NonNull Activity activity, @Nullable MenuOptionConfiguration closeConfig) {
        MenuItemTransition transition = closeConfig.getTransition();
        setupOptions(activity, new MenuOptionConfiguration(transition.clone().reverse(), -1), closeConfig);
    }

    @Override
    public void setupOptions(@NonNull Activity activity, @Nullable MenuOptionConfiguration openConfig, @Nullable MenuOptionConfiguration closeConfig) {
        if (mOpenConfig != null) {
            mAdapter.removeTransition(mOpenConfig.getTransition());
        }
        if (mCloseConfig != null) {
            mAdapter.removeTransition(mCloseConfig.getTransition());
        }
        mOpenConfig = openConfig;
        mCloseConfig = closeConfig;

        if (activity != null && mAdapterState.isOpen()) {
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

    @Override
    public void clearOptions() {
        setupOptions(null, null, null);
    }

    @Override
    public MenuOptionConfiguration getOpenConfig() {
        return mOpenConfig;
    }

    @Override
    public MenuOptionConfiguration getCloseConfig() {
        return mCloseConfig;
    }
}
