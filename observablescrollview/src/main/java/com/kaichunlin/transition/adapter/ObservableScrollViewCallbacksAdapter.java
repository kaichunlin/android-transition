package com.kaichunlin.transition.adapter;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.kaichunlin.transition.AbstractTransitionBuilder;
import com.kaichunlin.transition.Transition;

/**
 * TODO work in progress
 *
 * Created by Kai-Chun Lin on 2015/5/7.
 */
public class ObservableScrollViewCallbacksAdapter extends AbstractAdapter implements ObservableScrollViewCallbacks {

    private enum Direction {
        UNKOWN,
        UP,
        DOWN
    }

    private ArrayMap<Transition, Integer> mRanges = new ArrayMap<>();
    private ObservableScrollViewCallbacks mCallback;
    private int mStartY;
    private int mLastScrollY;
    private boolean mResetStartLocationOnChangeDirection;
    private Direction mDirection = Direction.UNKOWN;
    private OnChangeDirectionListener mOnChangeDirectionListener;

    @Override
    public void addTransition(AbstractTransitionBuilder transitionBuilder) {
        //TODO guess/retrieve range
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param transitionBuilder
     * @param range
     */
    public void addTransition(@NonNull AbstractTransitionBuilder transitionBuilder, int range) {
        addTransition(transitionBuilder.build(), range);
    }

    @Override
    public void addTransition(Transition transition) {
        //TODO guess/retrieve range
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param transition
     * @param range
     */
    public void addTransition(@NonNull Transition transition, int range) {
        super.addTransition(transition);
        mRanges.put(transition, range);
    }

    @Override
    public boolean removeTransition(Transition transition) {
        boolean result = super.removeTransition(transition);
        mRanges.remove(transition);
        return result;
    }

    /**
     *
     * @param resetStartLocationOnChangeDirection
     */
    public void setResetStartLocationOnChangeDirection(boolean resetStartLocationOnChangeDirection) {
        mResetStartLocationOnChangeDirection = resetStartLocationOnChangeDirection;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (firstScroll) {
            mStartY = scrollY;
            startTransition();
            return;
        }
        if (mLastScrollY < scrollY && mDirection != Direction.DOWN) {
            mDirection = Direction.DOWN;
            if (mOnChangeDirectionListener != null) {
                mOnChangeDirectionListener.onDown();
            }
            if (mResetStartLocationOnChangeDirection) {
                mStartY = scrollY;
            }
        } else if (mLastScrollY > scrollY && mDirection != Direction.UP) {
            mDirection = Direction.UP;
            if (mOnChangeDirectionListener != null) {
                mOnChangeDirectionListener.onUp();
            }
            if (mResetStartLocationOnChangeDirection) {
                mStartY = scrollY;
            }
        }

        getTransitionManager().updateProgress(scrollY - mStartY);

        mLastScrollY = scrollY;

        if (mCallback != null) {
            mCallback.onScrollChanged(scrollY, firstScroll, dragging);
        }
    }

    @Override
    public void onDownMotionEvent() {

        if (mCallback != null) {
            mCallback.onDownMotionEvent();
        }
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        stopTransition();

        if (mCallback != null) {
            mCallback.onUpOrCancelMotionEvent(scrollState);
        }
    }

    /**
     *
     * @param callback
     */
    public void setCallBack(ObservableScrollViewCallbacks callback) {
        this.mCallback = callback;
    }

    /**
     *
     * @return
     */
    public OnChangeDirectionListener getOnChangeDirectionListener() {
        return mOnChangeDirectionListener;
    }

    /**
     *
     * @param onChangeDirectionListener
     */
    public void setOnChangeDirectionListener(OnChangeDirectionListener onChangeDirectionListener) {
        this.mOnChangeDirectionListener = onChangeDirectionListener;
    }

    /**
     *
     */
    public interface OnChangeDirectionListener {
        void onUp();

        void onDown();
    }
}
