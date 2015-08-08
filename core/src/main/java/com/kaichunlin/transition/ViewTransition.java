package com.kaichunlin.transition;

import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.kaichunlin.transition.internal.TransitionController;
import com.kaichunlin.transition.internal.TransitionControllerManager;
import com.kaichunlin.transition.util.TransitionStateLogger;

/**
 * Provides transitions effects to all views other than one controlled by {@link android.view.MenuItem}
 * <p>
 * Created by Kai-Chun Lin on 2015/4/18.
 */
@UiThread
public class ViewTransition extends AbstractTransition<ViewTransition, ViewTransition.Setup> {
    private TransitionControllerManager transitionControllerManager;

    public ViewTransition() {
        this(null, null);
    }

    public ViewTransition(@Nullable Setup setup) {
        this(null, setup);
    }

    /**
     * @param id    unique ID that can identify the transition
     * @param setup creates the {@link TransitionController}'s when {@link #startTransition()} is called
     */
    public ViewTransition(@Nullable String id, @Nullable Setup setup) {
        super(id);
        setSetup(setup);
    }

    @Override
    public ViewTransition reverse() {
        super.reverse();
        if (transitionControllerManager != null) {
            transitionControllerManager.reverse();
        }
        return self();
    }

    @Override
    public boolean startTransition() {
        //caches result
        if (transitionControllerManager == null) {
            transitionControllerManager = new TransitionControllerManager(getId());
            if (mInterpolator != null) {
                transitionControllerManager.setInterpolator(mInterpolator);
            }
            if (TransitionConfig.isDebug()) {
                mTarget.setTag(R.id.debug_id, new TransitionStateLogger(getId()));
            }
            transitionControllerManager.setTarget(mTarget);
            transitionControllerManager.setUpdateStateAfterUpdateProgress(mUpdateStateAfterUpdateProgress);

            final int size = mSetupList.size();
            for (int i = 0; i < size; i++) {
                mSetupList.get(i).setupAnimation(transitionControllerManager);
            }
            if (mReverse) {
                transitionControllerManager.reverse();
            }
        }
        transitionControllerManager.start();
        return true;
    }

    @Override
    public void updateProgress(float progress) {
        transitionControllerManager.updateProgress(progress);
    }

    @Override
    public void stopTransition() {
        if (transitionControllerManager != null) {
            transitionControllerManager.end();
        }
    }

    @CheckResult
    @Override
    public ViewTransition clone() {
        ViewTransition newCopy = (ViewTransition) super.clone();
        //set to null for now, equivalent to calling invalidate()
        newCopy.transitionControllerManager = null;
        return newCopy;
    }

    @Override
    protected void invalidate() {
        stopTransition();
        transitionControllerManager = null;
    }

    @Override
    protected ViewTransition self() {
        return this;
    }

    /**
     * Creates the {@link TransitionController}'s when {@link #startTransition()} is called
     */
    public interface Setup extends AbstractTransition.Setup {
        /**
         * @param transitionControllerManager the {@link TransitionControllerManager} that the created {@link TransitionController} should be added to
         */
        void setupAnimation(TransitionControllerManager transitionControllerManager);
    }
}
