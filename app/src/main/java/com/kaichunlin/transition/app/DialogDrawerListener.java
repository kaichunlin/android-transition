package com.kaichunlin.transition.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import kaichunlin.transition.app.R;

/**
 * Created by Kai on 2015/5/28.
 */
public class DialogDrawerListener implements DrawerLayout.DrawerListener {
    private Activity mActivity;

    public DialogDrawerListener(Activity activity) {
        mActivity = activity;

        if (mActivity.getPreferences(0).getBoolean("dialog", true)) {
            new AlertDialog.Builder(mActivity).setMessage(R.string.dialog_slide_right).setNeutralButton(R.string.dialog_ok, null).create().show();
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        mActivity.getPreferences(0).edit().putBoolean("dialog", false).commit();
    }

    @Override
    public void onDrawerClosed(View drawerView) {
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }
}
