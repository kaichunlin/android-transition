package com.kaichunlin.transition.transformer;

import android.view.View;

import com.kaichunlin.transition.internal.TransitionController;

/**
 * Helper to transform from one color to another, how this color is used depends on the subclass.
 */
public abstract class ColorTransformer extends ScaledTransformer {
    final int startColor;
    final int endColor;

    public ColorTransformer(int startColor, int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
    }

    @Override
    protected void updateViewScaled(TransitionController controller, View target, float scaledProgress) {
        updateViewWithColor(controller, target, evaluate(scaledProgress, startColor, endColor));
    }

    /**
     * Similar to {@link #updateView(TransitionController, View, float)}, with the addition of the current color.
     *
     * @param controller
     * @param target
     * @param color The current color calculated using the starting color, the end color, and the current progress.
     */
    protected abstract void updateViewWithColor(TransitionController controller, View target, int color);

    private int evaluate(float fraction, int startValue, int endValue) {
        int startA = (startValue >>> 24);
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >>> 24);
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }
}
