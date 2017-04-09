package com.kaichunlin.transition.animation;

/**
 * Controls the current animation state.
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