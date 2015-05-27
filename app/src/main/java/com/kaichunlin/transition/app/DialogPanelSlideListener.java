package com.kaichunlin.transition.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import kaichunlin.transition.app.R;

/**
 * Created by Kai on 2015/5/28.
 */
public class DialogPanelSlideListener implements SlidingUpPanelLayout.PanelSlideListener {
    private Activity mActivity;

    public DialogPanelSlideListener(Activity activity) {
        mActivity = activity;

        if (mActivity.getPreferences(0).getBoolean("dialog", true)) {
            new AlertDialog.Builder(mActivity).setMessage(R.string.dialog_slide_up).setNeutralButton(R.string.dialog_ok, null).create().show();
        }
    }

    @Override
    public void onPanelSlide(View view, float v) {
    }

    @Override
    public void onPanelCollapsed(View view) {
    }

    @Override
    public void onPanelExpanded(View view) {
        mActivity.getPreferences(0).edit().putBoolean("dialog", false).commit();
    }

    @Override
    public void onPanelAnchored(View view) {
    }

    @Override
    public void onPanelHidden(View view) {
    }
}
