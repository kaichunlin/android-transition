package com.kaichunlin.transition.animation;

/**
 * Auto play the animation in reverse right after the animation ended, currently only works when the initial animation is not set
 * with {@link Animation#setReverseAnimation(boolean)} as true.
 *
 * Created by Kai on 2015/12/18.
 */
public class AutoReverseAnimationListener implements AnimationListener {
    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (!animation.isReverseAnimation()) {
            animation.setReverseAnimation(true);
            animation.startAnimation();
        }
    }

    @Override
    public void onAnimationCancel(Animation animation) {
    }

    @Override
    public void onAnimationReset(Animation animation) {
    }
}