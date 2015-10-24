package com.kaichunlin.transition.internal;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Interpolator;

import com.kaichunlin.transition.R;
import com.kaichunlin.transition.TransitionConfig;
import com.kaichunlin.transition.TransitionManager;
import com.kaichunlin.transition.util.TransitionStateLogger;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the transition state of a set of {@link TransitionController}
 * <p>
 * Created by Kai-Chun Lin on 2015/4/14.
 */
public class TransitionControllerManager implements Cloneable {
    private List<TransitionController> mTransitionControls = new ArrayList<>();
    private Interpolator mInterpolator;
    private String mId;
    private View mTarget;
    float mLastProgress;
    private boolean mUpdateStateAfterUpdateProgress;

    public TransitionControllerManager(String id) {
        mId = id;
    }

    /**
     * Adds an Animator as {@link TransitionController}
     *
     * @param mAnim
     * @return
     */
    public TransitionController addAnimatorAsTransition(@NonNull Animator mAnim) {
        AnimatorSet as = new AnimatorSet();
        as.play(mAnim);
        return addAnimatorSetAsTransition(null, as);
    }

    /**
     * Adds an Animator as {@link TransitionController}
     *
     * @param target
     * @param animator
     * @return
     */
    public TransitionController addAnimatorAsTransition(@Nullable View target, @NonNull Animator animator) {
        AnimatorSet as = new AnimatorSet();
        as.play(animator);
        return addAnimatorSetAsTransition(target, as);
    }

    /**
     * Adds an AnimatorSet as {@link TransitionController}
     *
     * @param animatorSet
     * @return
     */
    public TransitionController addAnimatorSetAsTransition(@NonNull AnimatorSet animatorSet) {
        return addAnimatorSetAsTransition(null, animatorSet);
    }

    /**
     * Adds an AnimatorSet as {@link TransitionController}
     *
     * @param target
     * @param animatorSet
     * @return
     */
    public TransitionController addAnimatorSetAsTransition(@Nullable View target, @NonNull AnimatorSet animatorSet) {
        return addTransitionController(new DefaultTransitionController(target, animatorSet));
    }

    /**
     * @param transitionController the TransitionController to be managed by this object
     * @return
     */
    public TransitionController addTransitionController(@NonNull TransitionController transitionController) {
        transitionController.setId(mId);
        boolean changed = false;
        if (!mTransitionControls.contains(transitionController)) {
            mTransitionControls.add(transitionController);
            changed = true;
        }
        if (!changed && TransitionConfig.isDebug()) {
            getTransitionStateHolder().append(mId + "->" + mTarget, this, "Possible duplicate: " + transitionController.getId());
        }
        return transitionController;
    }

    /**
     * Starts the transition
     */
    public void start() {
        if (TransitionConfig.isDebug()) {
            getTransitionStateHolder().start();
        }

        mLastProgress = Float.MIN_VALUE;

        TransitionController transitionController;
        for (int i = 0, size = mTransitionControls.size(); i < size; i++) {
            transitionController = mTransitionControls.get(i);
            if (mInterpolator != null) {
                transitionController.setInterpolator(mInterpolator);
            }
            //required for ViewPager transitions to work
            if (mTarget != null) {
                transitionController.setTarget(mTarget);
            }
            transitionController.setUpdateStateAfterUpdateProgress(mUpdateStateAfterUpdateProgress);
            transitionController.start();
        }
    }

    private TransitionStateLogger getTransitionStateHolder() {
        return (TransitionStateLogger) getTarget().getTag(R.id.debug_id);
    }

    private String getTransitionStateHolderId() {
        return ((TransitionStateLogger) getTarget().getTag(R.id.debug_id)).mId;
    }

    /**
     * Ends the transition
     */
    public void end() {
        if (TransitionConfig.isPrintDebug()) {
            getTransitionStateHolder().end();
            getTransitionStateHolder().print();
        }

        for (int i = 0, size = mTransitionControls.size(); i < size; i++) {
            mTransitionControls.get(i).end();
        }
    }

    /**
     * Updates the transition progress
     *
     * @param progress the possible range of values depends on the {@link TransitionManager} being used
     */
    public void updateProgress(float progress) {
        if (mLastProgress == progress) {
            return;
        }
        mLastProgress = progress;
        //TODO this makes ViewPager work, but will probably break more complex transition setup, will think of a better solution
        if (mUpdateStateAfterUpdateProgress) {
            boolean positive = progress >= 0;
            TransitionController transitionController;
            for (int i = 0, size = mTransitionControls.size(); i < size; i++) {
                transitionController = mTransitionControls.get(i);
                if (positive) {
                    if (transitionController.getEnd() > 0) {
                        transitionController.setEnable(true);
                    } else {
                        transitionController.setEnable(false);
                    }
                } else {
                    if (transitionController.getEnd() < 0) {
                        transitionController.setEnable(true);
                    } else {
                        transitionController.setEnable(false);
                    }
                }
            }
        }

        TransitionController transitionController;
        for (int i = 0, size = mTransitionControls.size(); i < size; i++) {
            transitionController = mTransitionControls.get(i);
            if (transitionController.isEnable()) {
                transitionController.updateProgress(progress);
            }
        }
    }

    /**
     * @param target the view that all {@link TransitionController} managed by this object should work on
     */
    public void setTarget(@Nullable View target) {
        mTarget = target;
        for (int i = 0, size = mTransitionControls.size(); i < size; i++) {
            mTransitionControls.get(i).setTarget(target);
        }
    }

    /**
     * @return
     */
    @Nullable
    public View getTarget() {
        return mTarget;
    }

    /**
     * Reverses all the TransitionControllers managed by this TransitionManager
     */
    public void reverse() {
        for (int i = 0, size = mTransitionControls.size(); i < size; i++) {
            mTransitionControls.get(i).reverse();
        }
    }

    /**
     * @param interpolator the Interpolator to be applied to all {@link TransitionController} managed by this object
     */
    public void setInterpolator(@Nullable Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    /**
     * @param updateStateAfterUpdateProgress whether or not to update a controller's enable state after each {@link #updateProgress(float)} call
     */
    public void setUpdateStateAfterUpdateProgress(boolean updateStateAfterUpdateProgress) {
        mUpdateStateAfterUpdateProgress = updateStateAfterUpdateProgress;
    }

    @CheckResult
    @Override
    public TransitionControllerManager clone() {
        TransitionControllerManager newClone = null;
        try {
            newClone = (TransitionControllerManager) super.clone();
            newClone.mTransitionControls = new ArrayList<>(mTransitionControls.size());
            for (int i = 0, size = mTransitionControls.size(); i < size; i++) {
                newClone.mTransitionControls.add(mTransitionControls.get(i).clone());
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return newClone;
    }
}