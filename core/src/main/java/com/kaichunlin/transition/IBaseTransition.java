package com.kaichunlin.transition;

/**
 * Created by Kai on 2015/7/12.
 */
public interface IBaseTransition {
    /**
     * Starts the transition
     *
     * @return
     */
    boolean startTransition();

    boolean startTransition(float progress);

    /**
     * Updates the transition progress
     *
     * @param progress
     */
    void updateProgress(float progress);

    /**
     * Stops the transition
     */
    void stopTransition();
}
