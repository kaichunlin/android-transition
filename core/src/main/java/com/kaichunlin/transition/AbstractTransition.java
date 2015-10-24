package com.kaichunlin.transition;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Provides common implementations for all transitions.
 * <p>
 * Created by Kai-Chun Lin on 2015/4/18.
 */
public abstract class AbstractTransition<T extends AbstractTransition, S extends AbstractTransition.Setup> implements Transition<S> {
    List<S> mSetupList = new ArrayList<>();
    String mId;
    boolean mReverse;
    Interpolator mInterpolator;
    View mTarget;
    boolean mUpdateStateAfterUpdateProgress;

    public AbstractTransition(@Nullable String id) {
        this.mId = id;
    }

    @Override
    public T setId(@Nullable String id) {
        mId = id;
        return self();
    }

    @Override
    public String getId() {
        return mId == null ? (mId = toString()) : mId;
    }

    @Override
    public boolean startTransition(float progress) {
        if (startTransition()) {
            updateProgress(progress);
            return true;
        }
        return false;
    }

    @Override
    public void setProgress(float progress) {
        //TODO optimize
        startTransition();
        updateProgress(progress);
        stopTransition();
    }

    @Override
    public T reverse() {
        String id = getId();
        String REVERSE = "_REVERSE";
        if (id.endsWith(REVERSE)) {
            setId(id.substring(0, id.length() - REVERSE.length()));
        } else {
            setId(id + REVERSE);
        }

        mReverse = !mReverse;
        return self();
    }

    @Override
    public T setSetup(@NonNull S setup) {
        if (setup != null) {
            mSetupList.add(setup);
        }
        return self();
    }

    /**
     * Only true if it has merged another Transition
     *
     * @return
     */
    boolean hasMultipleSetup() {
        return mSetupList.size() > 1;
    }

    @Override
    public void setTarget(@Nullable View target) {
        mTarget = target;
        invalidate();
    }

    @Override
    public View getTarget() {
        return mTarget;
    }

    public T setUpdateStateAfterUpdateProgress(boolean updateStateAfterUpdateProgress) {
        mUpdateStateAfterUpdateProgress = updateStateAfterUpdateProgress;
        return self();
    }

    /**
     * Invalidates the current transition, which may mean the currently running transition is stopped
     */
    protected abstract void invalidate();

    protected abstract T self();

    @Override
    public T setInterpolator(@Nullable Interpolator interpolator) {
        mInterpolator = interpolator;
        return self();
    }

    @CheckResult
    @Override
    public AbstractTransition clone() {
        AbstractTransition newClone = null;
        try {
            newClone = (AbstractTransition) super.clone();
            newClone.setId(newClone.getId() + "_CLONE");
            newClone.mSetupList = new ArrayList<>(mSetupList.size());
            newClone.mSetupList.addAll(mSetupList);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return newClone;
    }

    public boolean compatible(AbstractTransition another) {
        if (getClass().equals(another.getClass()) && mTarget == another.mTarget && mReverse == another.mReverse && ((mInterpolator == null && another.mInterpolator == null) ||
                mInterpolator.getClass().equals(another.mInterpolator.getClass()))) {
            return true;
        }
        return false;
    }

    public boolean merge(AbstractTransition another) {
        if (!compatible(another)) {
            return false;
        }
        if (another.mId != null) {
            if (mId == null) {
                mId = another.mId;
            } else {
                mId += "_MERGED_" + another.mId;
            }
        }
        mUpdateStateAfterUpdateProgress |= another.mUpdateStateAfterUpdateProgress;
        mSetupList.addAll(another.mSetupList);
        Collections.sort(mSetupList, new Comparator<S>() {
            @Override
            public int compare(S lhs, S rhs) {
                if (lhs instanceof AbstractTransitionBuilder && rhs instanceof AbstractTransitionBuilder) {
                    AbstractTransitionBuilder left = (AbstractTransitionBuilder) lhs;
                    AbstractTransitionBuilder right = (AbstractTransitionBuilder) rhs;
                    float startLeft = left.mReverse ? left.mEnd : left.mStart;
                    float startRight = right.mReverse ? right.mEnd : right.mStart;
                    return (int) ((startRight - startLeft) * 1000);
                }
                return 0;
            }
        });

        return true;
    }

    /**
     * Represents an object that will create ITransitionController Objects to be added to a TransitionManager
     */
    public interface Setup {
    }
}
