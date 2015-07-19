package com.kaichunlin.transition.adapter;

import android.app.Activity;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;

/**
 * Similar to {@link AnimationAdapter} with the addition of MenuItem animation support.
 * <p>
 * Created by Kai on 2015/6/22.
 */
public class MenuAnimationAdapter extends AnimationAdapter implements MenuOptionHandler {
    private MenuOptionHandler mMenuOptionHandler = new DefaultMenuOptionHandler(this, getAdapterState());

    public MenuAnimationAdapter() {
        super();
    }

    /**
     * Wraps an existing {@link MenuBaseAdapter} to reuse its onCreateOptionsMenu(...) logic and its transition effects
     *
     * @param adapter
     */
    public MenuAnimationAdapter(@Nullable TransitionAdapter adapter) {
        super(adapter);
    }

    @Override
    public void startAnimation(@IntRange(from = 0) int duration) {
        AdapterState adapterState = getAdapterState();
        adapterState.setState(adapterState.getState() == AdapterState.OPEN ? AdapterState.CLOSE : AdapterState.OPEN);

        setReverseAnimation(adapterState.isOpen());
        super.startAnimation(duration);
    }

    public void setMenuOptionHandler(@NonNull MenuOptionHandler menuOptionHandler) {
        mMenuOptionHandler = menuOptionHandler;
    }

    private MenuOptionHandler getHandler() {
        return getAdapter() == null ? mMenuOptionHandler : (MenuOptionHandler) getAdapter();
    }

    public void onCreateOptionsMenu(@NonNull Activity activity, @NonNull Menu menu) {
        if (getAdapter() == null) {
            mMenuOptionHandler.onCreateOptionsMenu(activity, menu);
        } else {
            ((MenuBaseAdapter) getAdapter()).onCreateOptionsMenu(activity, menu, mMenuOptionHandler.getAdapterState());
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Activity activity, @NonNull Menu menu, @NonNull AdapterState adapterState) {
        getHandler().onCreateOptionsMenu(activity, menu, adapterState);
    }

    public void setupOption(@NonNull Activity activity, @Nullable MenuOptionConfiguration openConfig) {
        getHandler().setupOption(activity, openConfig);
    }

    public void setupOpenOption(@NonNull Activity activity, @Nullable MenuOptionConfiguration openConfig) {
        getHandler().setupOpenOption(activity, openConfig);
    }

    public void setupCloseOption(@NonNull Activity activity, @Nullable MenuOptionConfiguration closeConfig) {
        getHandler().setupCloseOption(activity, closeConfig);
    }

    public void setupOptions(@NonNull Activity activity, @Nullable MenuOptionConfiguration openConfig, @Nullable MenuOptionConfiguration closeConfig) {
        getHandler().setupOptions(activity, openConfig, closeConfig);
    }

    @Override
    public void clearOptions() {
        getHandler().clearOptions();
    }

    @Override
    public MenuOptionConfiguration getOpenConfig() {
        return getHandler().getOpenConfig();
    }

    @Override
    public MenuOptionConfiguration getCloseConfig() {
        return getHandler().getCloseConfig();
    }
}
