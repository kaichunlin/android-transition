package com.kaichunlin.transition.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Adapter for SlidingUpPanelLayout, the transition range goes from 0.0f to 1.0f, where 0.0f is the collapsed state and 1.0f is the expanded state.
 * <p>
 * Created by Kai-Chun Lin on 2015/4/14.
 */
public class SlidingUpPanelLayoutAdapter extends MenuBaseAdapter implements SlidingUpPanelLayout.PanelSlideListener {
    private SlidingUpPanelLayout.PanelSlideListener mListener;

    /**
     * Sets a listener that can provide further customization, the respective calls to the listener is performed after the adapter has completed its own processing.
     *
     * @param listener
     */
    public void setPanelSlideListener(@Nullable SlidingUpPanelLayout.PanelSlideListener listener) {
        mListener = listener;
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        startTransition();
        getTransitionManager().updateProgress(slideOffset);

        if (mListener != null) {
            mListener.onPanelSlide(panel, slideOffset);
        }
    }

    @Override
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
        if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            getAdapterState().setState(AdapterState.CLOSE);
        } else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            getAdapterState().setState(AdapterState.OPEN);
        }
        stopTransition();
        mListener.onPanelStateChanged(panel, previousState, newState);
    }

    @Override
    protected MenuOptionHandler createMenuHandler() {
        return new DefaultMenuOptionHandler(this, getAdapterState());
    }
}
