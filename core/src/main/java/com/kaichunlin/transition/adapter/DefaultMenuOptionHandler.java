package com.kaichunlin.transition.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;

import com.kaichunlin.transition.MenuItemTransition;
import com.kaichunlin.transition.TransitionManager;

/**
 * Provides default support for handling MenuItem transitions
 * <p>
 * Created by Kai on 2015/5/10.
 */
public class DefaultMenuOptionHandler implements MenuOptionHandler {
    private final TransitionManager mTransitionManager;
    private final AdapterState mAdapterState;
    private MenuOptionConfiguration mOpenConfig;
    private MenuOptionConfiguration mCloseConfig;

    public DefaultMenuOptionHandler(@NonNull TransitionManager transitionManager, @NonNull AdapterState adapterState) {
        mTransitionManager = transitionManager;
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
                    // TODO may cause issues when mOpenConfig.getTransition() has been merged
                    mTransitionManager.removeTransition(mOpenConfig.getTransition());
                }
                mTransitionManager.addTransition(mCloseConfig.getTransition());
            }
        } else {
            boolean hasClose = mCloseConfig != null;
            if (hasClose && mCloseConfig.getMenuId() > 0) {
                activity.getMenuInflater().inflate(mCloseConfig.getMenuId(), menu);
            }
            if (mOpenConfig != null) {
                if (hasClose) {
                    mTransitionManager.removeTransition(mCloseConfig.getTransition());
                }
                mTransitionManager.addTransition(mOpenConfig.getTransition());
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
            mTransitionManager.removeTransition(mOpenConfig.getTransition());
        }
        if (mCloseConfig != null) {
            mTransitionManager.removeTransition(mCloseConfig.getTransition());
        }
        mOpenConfig = openConfig;
        mCloseConfig = closeConfig;

        if (activity != null && mAdapterState.isOpen()) {
            if (mCloseConfig != null) {
                if (mCloseConfig.getTransition().isInvalidateOptionOnStopTransition()) {
                    activity.invalidateOptionsMenu();
                }
                mTransitionManager.addTransition(mCloseConfig.getTransition());
            }
        } else {
            if (mOpenConfig != null) {
                if (mOpenConfig.getTransition().isInvalidateOptionOnStopTransition()) {
                    activity.invalidateOptionsMenu();
                }
                mTransitionManager.addTransition(mOpenConfig.getTransition());
            }
        }
    }

    @Override
    public void clearOptions() {
        setupOptions(null, null, null);
    }

    @Nullable
    @Override
    public MenuOptionConfiguration getOpenConfig() {
        return mOpenConfig;
    }

    @Nullable
    @Override
    public MenuOptionConfiguration getCloseConfig() {
        return mCloseConfig;
    }
}
