package com.kaichunlin.transition.adapter;

import android.app.Activity;
import android.view.View;

import com.kaichunlin.transition.ITransition;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Adapter for SlidingUpPanelLayout, the transition range goes from 0.0f to 1.0f, where 0.0f is the collapsed state and 1.0f is the expanded state.
 *
 * Created by Kai-Chun Lin on 2015/4/14.
 */
public class SlidingUpPanelLayoutAdapter extends MenuBaseAdapter implements SlidingUpPanelLayout.PanelSlideListener {
    private boolean mIsExpanded;

    public SlidingUpPanelLayoutAdapter() {
        mMenuHandler = new DefaultMenuOptionHandler(this) {
            @Override
            boolean isOpened(Activity activity) {
                return mIsExpanded;
            }
        };
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        startTransition();
        for (ITransition trans : mTransitionList.values()) {
            trans.updateProgress(slideOffset);
        }
    }

    @Override
    public void stopTransition() {
        super.stopTransition();
    }

    @Override
    public void onPanelCollapsed(View panel) {
        stopTransition();
        mIsExpanded = false;
    }

    @Override
    public void onPanelExpanded(View panel) {
        stopTransition();
        mIsExpanded = true;
    }

    @Override
    public void onPanelAnchored(View panel) {
        stopTransition();
    }

    @Override
    public void onPanelHidden(View panel) {
        stopTransition();
    }
}
