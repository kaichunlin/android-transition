package com.kaichunlin.transition.widget;

import android.view.View;

import com.kaichunlin.transition.ScaledTransitionHandler;
import com.kaichunlin.transition.internal.TransitionController;

/**
 * Transit from one color to another
 */
public abstract class ColorTransition extends ScaledTransitionHandler {
    final int startColor;
    final int endColor;

    public ColorTransition(int startColor, int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
    }

    @Override
    protected void onUpdateScaledProgress(TransitionController controller, View target, float modifiedProgress) {
        onUpdateColor(controller, target, evaluate(modifiedProgress, startColor, endColor));
    }

    protected abstract void onUpdateColor(TransitionController controller, View target, int color);

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
