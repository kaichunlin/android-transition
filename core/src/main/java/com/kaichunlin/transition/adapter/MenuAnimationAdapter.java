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
public class MenuAnimationAdapter extends AnimationAdapter implements IMenuOptionHandler {
    private IMenuOptionHandler mMenuOptionHandler = new DefaultMenuOptionHandler(this, new IMenuOptionHandler.AdapterState() {
        @Override
        public boolean isOpened(Activity activity) {
            return mOpened;
        }
    });
    private boolean mOpened;

    public MenuAnimationAdapter() {
        super();
    }

    /**
     * Wraps an existing {@link MenuBaseAdapter} to reuse its onCreateOptionsMenu(...) logic and its transition effects
     *
     * @param adapter
     */
    public MenuAnimationAdapter(@Nullable MenuBaseAdapter adapter) {
        super(adapter);
    }

    @Override
    public void startAnimation(@IntRange(from = 0) int duration) {
        setAnimationInReverse(mOpened);
        super.startAnimation(duration);
        mOpened=!mOpened;
    }

    public void setMenuOptionHandler(@NonNull IMenuOptionHandler menuOptionHandler) {
        mMenuOptionHandler = menuOptionHandler;
    }

    protected IMenuOptionHandler createMenuHandler() {
        return mMenuOptionHandler;
    }

    private IMenuOptionHandler getHandler() {
        return mAdapter == null?mMenuOptionHandler:(IMenuOptionHandler)mAdapter;
    }

    @Override
    public AdapterState getAdapterState() {
        getHandler().getAdapterState();
        return null;
    }

    @Override
    public boolean isOpened(@NonNull Activity activity) {
        return getHandler().isOpened(activity);
    }

    public void onCreateOptionsMenu(@NonNull Activity activity, @NonNull Menu menu) {
        if (mAdapter == null) {
            mMenuOptionHandler.onCreateOptionsMenu(activity, menu);
        } else {
            ((MenuBaseAdapter)mAdapter).onCreateOptionsMenu(activity, menu, mMenuOptionHandler.getAdapterState());
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
