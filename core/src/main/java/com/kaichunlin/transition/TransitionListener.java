package com.kaichunlin.transition;

/**
 * Created by Kai on 2015/7/18.
 */
public interface TransitionListener {
    void onTransitionStart(TransitionManager transitionManager);

    void onTransitionEnd(TransitionManager transitionManager);
}