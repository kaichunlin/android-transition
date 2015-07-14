package com.kaichunlin.transition.adapter;

import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;

import com.kaichunlin.transition.MenuItemTransition;

/**
 * Stores the menu ID-MenuItemTransition pair
 * <p>
 * Created by Kai-Chun Lin on 2015/5/10.
 */
public class MenuOptionConfiguration {

    private final MenuItemTransition mTransition;
    private final int mMenuId;

    public MenuOptionConfiguration(@NonNull MenuItemTransition transition) {
        mTransition = transition;
        mMenuId = -1;
    }

    public MenuOptionConfiguration(@NonNull MenuItemTransition transition, @MenuRes int menuId) {
        mTransition = transition;
        mMenuId = menuId;
    }

    /**
     *
     * @return MenuItemTransition for the menu item
     */
    public MenuItemTransition getTransition() {
        return mTransition;
    }

    /**
     *
     * @return the menu item's ID
     */
    public int getMenuId() {
        return mMenuId;
    }
}
