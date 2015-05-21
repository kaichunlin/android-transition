package com.kaichunlin.transition;

import com.kaichunlin.transition.util.TransitionStateHolder;

import java.util.Iterator;
import java.util.WeakHashMap;

/**
 * Provides transitions effects to all views other than one controlled by {@link android.view.MenuItem}
 * <p/>
 * Created by Kai-Chun Lin on 2015/4/18.
 */
public class ViewTransition extends BaseTransition<ViewTransition, ViewTransition.Setup> {
    private static final Object HARD_REF = new Object();
    private final WeakHashMap<Object, Setup> mSetup = new WeakHashMap<>();
    private SetupCreator mSetupCreator;
    private TransitionManager transitionManager;

    public ViewTransition() {
        this(null, null);
    }

    public ViewTransition(Setup setup) {
        this(null, setup);
    }

    /**
     *
     * @param id unique ID that can identify the transition
     * @param setup creates the {@link ITransitionController}'s when {@link #startTransition()} is called
     */
    public ViewTransition(String id, Setup setup) {
        this(id, HARD_REF, setup);
    }

    public ViewTransition(String id, Object weakLink, Setup setup) {
        super(id);
        if (setup != null) {
            mSetup.put(weakLink, setup);
        }
    }

    @Override
    public ViewTransition setSetup(Setup setup) {
        mSetup.put(HARD_REF, setup);
        return self();
    }

    public void setSetup(Object weakLink, Setup setup) {
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
        if (transitionManager != null) {
            transitionManager.reverse();
        }
        return self();
    }

    @Override
    public boolean startTransition() {
        //caches result
        if (transitionManager == null) {
            transitionManager = new TransitionManager(getId());
            if (mInterpolator != null) {
                transitionManager.setInterpolator(mInterpolator);
            }
            if (TransitionConfig.isDebug()) {
                mTarget.setTag(R.id.debug_id, new TransitionStateHolder(getId()));
            }
            transitionManager.setTarget(mTarget);
            transitionManager.setUpdateStateAfterUpdateProgress(mUpdateStateAfterUpdateProgress);
            Setup s = getSetup();
            if (s == null) {
                if (mSetupCreator == null) {
                    return false;
                }
                SetupCombo sc = mSetupCreator.create();
                mSetup.put(sc.keyRef, sc.setup);
                s = sc.setup;
            }
            s.setupAnimation(transitionManager);
            if (mReverse) {
                transitionManager.reverse();
            }
        }
        transitionManager.start();
        return true;
    }

    @Override
    public void updateProgress(float progress) {
        transitionManager.updateProgress(progress);
    }

    @Override
    public void stopTransition() {
        if (transitionManager != null) {
            transitionManager.end();
        }
    }

    @Override
    public ViewTransition clone() {
        ViewTransition newCopy = (ViewTransition) super.clone();
        //set to null for now, equivalent to calling invalidate()
        newCopy.transitionManager = null;
        return newCopy;
    }

    @Override
    protected void invalidate() {
        stopTransition();
        transitionManager = null;
    }

    @Override
    protected ViewTransition self() {
        return this;
    }

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
         * @param transitionManager the {@link TransitionManager} that the created {@link ITransitionController} should be added to
         */
        void setupAnimation(TransitionManager transitionManager);
    }
}
