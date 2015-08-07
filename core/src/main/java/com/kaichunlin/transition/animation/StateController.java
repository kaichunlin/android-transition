package com.kaichunlin.transition.animation;

/**
 * Used to advance animation state.
 *
 * Created by Kai on 2015/8/7.
 */
interface StateController {
    void addAnimation(AbstractAnimation animation);

    void setAnimationDuration(long duration);

    void startController();

    void pauseController();

    void resumeController();

    void endController();

    void resetController();

    void cancelController();
}