package com.kaichunlin.transition.util;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Useful utility methods for manipulating Views
 * <p>
 * Created by Kai-Chun Lin on 2015/4/18.
 */
public class ViewUtil {
    /**
     * Get the list of visible MenuItems
     *
     * @param toolbar
     * @return the list of visible MenuItems
     */
    public static List<MenuItem> getVisibleMenuItemList(@NonNull Toolbar toolbar) {
        List<MenuItem> list = new ArrayList<>();
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            final View v = toolbar.getChildAt(i);
            if (v instanceof ActionMenuView) {
                int childCount = ((ActionMenuView) v).getChildCount();
                for (int j = 0; j < childCount; j++) {
                    final View innerView = ((ActionMenuView) v).getChildAt(j);
                    if (innerView instanceof ActionMenuItemView) {
                        list.add(((ActionMenuItemView) innerView).getItemData());
                    }
                }
            }
        }
        return list;
    }

    /**
     * Helper that removes the drudgery of using ViewTreeObserver
     *
     * @param view
     * @param listener
     */
    public static void executeOnGlobalLayout(@NonNull final View view, @NonNull final ViewTreeObserver.OnGlobalLayoutListener listener) {
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver()
                            .removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver()
                            .removeOnGlobalLayoutListener(this);
                }

                listener.onGlobalLayout();
            }
        });
    }
}
