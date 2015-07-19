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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Manages the transition state of a set of {@link TransitionController}
 * <p>
 * Created by Kai-Chun Lin on 2015/4/14.
 */
public class TransitionControllerManager implements Cloneable {
    private Set<TransitionController> mTransitionControls = new HashSet<>();
    //    private final TimeAnimator mTimeAnim;
//    private final AnimatorSet mInternalAnimSet;
    private Interpolator mInterpolator;
    private String mId;
    private View mTarget;
    float mLastProgress;
    private boolean mUpdateStateAfterUpdateProgress;

    public TransitionControllerManager(String id) {
        mId = id;

//        this.mInternalAnimSet = new AnimatorSet();
//        mTimeAnim = new TimeAnimator();
//        mTimeAnim.setTimeListener(this);
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
        boolean changed = mTransitionControls.add(transitionController);
        if (TransitionConfig.isDebug() && !changed) {
            getTransitionStateHolder().append(mId+"->"+mTarget, this, "Possible duplicate: " + transitionController.getId());
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

        for (TransitionController ctrl : mTransitionControls) {
            if (mInterpolator != null) {
                ctrl.setInterpolator(mInterpolator);
            }
            //required for ViewPager transitions to work
            if (mTarget != null) {
                ctrl.setTarget(mTarget);
            }
            ctrl.setUpdateStateAfterUpdateProgress(mUpdateStateAfterUpdateProgress);
            ctrl.start();
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

        for (TransitionController ctrl : mTransitionControls) {
            ctrl.end();
        }
//        mTimeAnim.end();
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
            for (TransitionController ctrl : mTransitionControls) {
                if (positive) {
                    if (ctrl.getEnd() > 0) {
                        ctrl.setEnable(true);
                    } else {
                        ctrl.setEnable(false);
                    }
                } else {
                    if (ctrl.getEnd() < 0) {
                        ctrl.setEnable(true);
                    } else {
                        ctrl.setEnable(false);
                    }
                }
            }
        }

        for (TransitionController ctrl : mTransitionControls) {
            if (ctrl.isEnable()) {
                ctrl.updateProgress(progress);
            }
        }
    }

    /**
     * @param target the view that all {@link TransitionController} managed by this object should work on
     */
    public void setTarget(@Nullable View target) {
        mTarget = target;
        for (TransitionController at : mTransitionControls) {
            at.setTarget(target);
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
        for (TransitionController at : mTransitionControls) {
            at.reverse();
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
            Iterator<TransitionController> at = mTransitionControls.iterator();
            newClone.mTransitionControls = new HashSet<>();
            while (at.hasNext()) {
                newClone.mTransitionControls.add(at.next().clone());
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return newClone;
    }
}