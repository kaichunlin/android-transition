package com.kaichunlin.transition;

import android.view.View;
import android.view.animation.Interpolator;

import com.kaichunlin.transition.util.TransitionStateHolder;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Manages the transition state of a set of {@link ITransitionController}
 * <p>
 * Created by Kai-Chun Lin on 2015/4/14.
 */
public class TransitionManager implements /*TimeAnimator.TimeListener,*/ Cloneable {
    private Set<ITransitionController> mTransitionControls = new HashSet<>();
    //    private final TimeAnimator mTimeAnim;
//    private final AnimatorSet mInternalAnimSet;
    private Interpolator mInterpolator;
    private String mId;
    private View mTarget;
    float mLastProgress;
    private boolean mUpdateStateAfterUpdateProgress;

    public TransitionManager(String id) {
        mId = id;

//        this.mInternalAnimSet = new AnimatorSet();
//        mTimeAnim = new TimeAnimator();
//        mTimeAnim.setTimeListener(this);
    }

    /**
     * Adds an Animator as {@link ITransitionController}
     *
     * @param mAnim
     * @return
     */
    public ITransitionController addAnimatorAsTransition(Animator mAnim) {
        AnimatorSet as = new AnimatorSet();
        as.play(mAnim);
        return addAnimatorSetAsTransition(null, as);
    }

    /**
     * Adds an Animator as {@link ITransitionController}
     *
     * @param target
     * @param animator
     * @return
     */
    public ITransitionController addAnimatorAsTransition(View target, Animator animator) {
        AnimatorSet as = new AnimatorSet();
        as.play(animator);
        return addAnimatorSetAsTransition(target, as);
    }

    /**
     * Adds an AnimatorSet as {@link ITransitionController}
     *
     * @param animatorSet
     * @return
     */
    public ITransitionController addAnimatorSetAsTransition(AnimatorSet animatorSet) {
        return addAnimatorSetAsTransition(null, animatorSet);
    }

    /**
     * Adds an AnimatorSet as {@link ITransitionController}
     *
     * @param target
     * @param animatorSet
     * @return
     */
    public ITransitionController addAnimatorSetAsTransition(View target, AnimatorSet animatorSet) {
        return addTransitionController(new DefaultTransitionController(target, animatorSet));
    }

    /**
     * @param transitionController the ITransitionController to be managed by this object
     * @return
     */
    public ITransitionController addTransitionController(ITransitionController transitionController) {
        transitionController.setId(mId);
        boolean changed = mTransitionControls.add(transitionController);
        if (TransitionConfig._debug && !changed) {
            getTransitionStateHolder().append(mId, this, "Possible duplicate: " + transitionController.getId());
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
//        //XXX share this among all TransitionManager's
//        ValueAnimator dummyAnim = ObjectAnimator.ofInt(new Object() {
//            private int dummy;
//
//            public int getDummy() {
//                return dummy;
//            }
//
//            public void setDummy(int dummy) {
//                this.dummy = dummy;
//            }
//        }, "dummy", 0, 1);
//        dummyAnim.setDuration(Integer.MAX_VALUE);
//        mInternalAnimSet.play(dummyAnim).with(mTimeAnim);
//        mInternalAnimSet.start();

        for (ITransitionController ctrl : mTransitionControls) {
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

    private TransitionStateHolder getTransitionStateHolder() {
        return (TransitionStateHolder) getTarget().getTag(R.id.debug_id);
    }

    private String getTransitionStateHolderId() {
        return ((TransitionStateHolder) getTarget().getTag(R.id.debug_id)).mId;
    }

    /**
     * Ends the transition
     */
    public void end() {
        if (TransitionConfig.isDebug()) {
            getTransitionStateHolder().end();
            getTransitionStateHolder().print();
        }

        for (ITransitionController ctrl : mTransitionControls) {
            ctrl.end();
        }
//        mTimeAnim.end();
    }

    /**
     * Updates the transition progress
     *
     * @param progress the possible range of values depends on the {@link com.kaichunlin.transition.adapter.ITransitionAdapter} being used
     */
    public void updateProgress(float progress) {
        if (mLastProgress == progress) {
            return;
        }
        mLastProgress = progress;
        //TODO this makes ViewPager work, but will probably break more complex transition setup, will think of a better solution
        if (mUpdateStateAfterUpdateProgress) {
            boolean positive = progress >= 0;
            for (ITransitionController ctrl : mTransitionControls) {
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

        for (ITransitionController ctrl : mTransitionControls) {
            if (ctrl.isEnable()) {
                ctrl.updateProgress(progress);
            }
        }
    }

//    @Override
//    public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
//        for (IAnimationTransition ctrl : mTransitionControls) {
//            if (ctrl.isEnable()) {
//                ctrl.updateState();
//            }
//        }
//    }

    /**
     * @param target the view that all {@link ITransitionController} managed by this object should work on
     */
    public void setTarget(View target) {
        mTarget = target;
        for (ITransitionController at : mTransitionControls) {
            at.setTarget(target);
        }
    }

    /**
     * @return
     */
    public View getTarget() {
        return mTarget;
    }

    /**
     * Reverses all the TransitionControllers managed by this TransitionManager
     */
    public void reverse() {
        for (ITransitionController at : mTransitionControls) {
            at.reverse();
        }
    }

    /**
     * @param interpolator the Interpolator to be applied to all {@link ITransitionController} managed by this object
     */
    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    /**
     * @param updateStateAfterUpdateProgress whether or not to update a controller's enable state after each {@link #updateProgress(float)} call
     */
    public void setUpdateStateAfterUpdateProgress(boolean updateStateAfterUpdateProgress) {
        mUpdateStateAfterUpdateProgress = updateStateAfterUpdateProgress;
    }

    @Override
    public TransitionManager clone() {
        TransitionManager newClone = null;
        try {
            newClone = (TransitionManager) super.clone();
            Iterator<ITransitionController> at = mTransitionControls.iterator();
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