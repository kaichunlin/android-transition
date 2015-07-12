package com.kaichunlin.transition;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.kaichunlin.transition.internal.ITransitionController;
import com.kaichunlin.transition.internal.TransitionControllerManager;
import com.kaichunlin.transition.util.TransitionStateLogger;

import java.util.Iterator;
import java.util.WeakHashMap;

/**
 * Provides transitions effects to all views other than one controlled by {@link android.view.MenuItem}
 * <p>
 * Created by Kai-Chun Lin on 2015/4/18.
 */
@UiThread
public class ViewTransition extends BaseTransition<ViewTransition, ViewTransition.Setup> {
    private static final Object HARD_REF = new Object();
    private final WeakHashMap<Object, Setup> mSetup = new WeakHashMap<>();
    private SetupCreator mSetupCreator;
    private TransitionControllerManager transitionControllerManager;

    public ViewTransition() {
        this(null, null);
    }

    public ViewTransition(@Nullable Setup setup) {
        this(null, setup);
    }

    /**
     *
     * @param id unique ID that can identify the transition
     * @param setup creates the {@link ITransitionController}'s when {@link #startTransition()} is called
     */
    public ViewTransition (@Nullable String id, @Nullable Setup setup) {
        this(id, HARD_REF, setup);
    }

    public ViewTransition(@Nullable String id, @Nullable Object weakLink, @Nullable Setup setup) {
        super(id);
        if (setup != null) {
            mSetup.put(weakLink, setup);
        }
    }

    @Override
    public ViewTransition setSetup(@Nullable Setup setup) {
        mSetup.put(HARD_REF, setup);
        return self();
    }

    /**
     * May be removed in the future!
     *
     * @param weakLink
     * @param setup
     */
    public void setSetup(@NonNull Object weakLink, @NonNull Setup setup) {
        mSetup.put(weakLink, setup);
    }

    private Setup getSetup() {
        Iterator<Setup> i = mSetup.values().iterator();
        if (i.hasNext()) {
            return i.next();
        }
        throw new IllegalStateException("Setup has been GCed");
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
            Setup s = getSetup();
            if (s == null) {
                if (mSetupCreator == null) {
                    return false;
                }
                SetupCombo sc = mSetupCreator.create();
                mSetup.put(sc.keyRef, sc.setup);
                s = sc.setup;
            }
            s.setupAnimation(transitionControllerManager);
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

    @Nullable
    public void setSetupCreator(SetupCreator setupCreator) {
        this.mSetupCreator = setupCreator;
    }

    /**
     * Recreates Setup as necessary, so that Setup objects will not become a source of memory leak
     * TODO is this still needed now?
     */
    public interface SetupCreator {
        SetupCombo create();
    }

    /**
     * A keyRef/setup pair
     * TODO is this still needed now?
     */
    public static class SetupCombo {
        Object keyRef;
        Setup setup;
    }

    /**
     * Creates the {@link ITransitionController}'s when {@link #startTransition()} is called
     */
    public interface Setup extends BaseTransition.Setup {
        /**
         * @param transitionControllerManager the {@link TransitionControllerManager} that the created {@link ITransitionController} should be added to
         */
        void setupAnimation(TransitionControllerManager transitionControllerManager);
    }
}
