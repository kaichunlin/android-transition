package com.kaichunlin.transition.adapter;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.kaichunlin.transition.ITransitionHandler;
import com.kaichunlin.transition.ITransition;
import com.kaichunlin.transition.R;
import com.kaichunlin.transition.TransitionConfig;
import com.kaichunlin.transition.ViewTransition;
import com.kaichunlin.transition.ViewTransitionBuilder;
import com.kaichunlin.transition.internal.ITransitionController;
import com.kaichunlin.transition.util.TransitionStateLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Adapter for PageView
 * <p>
 * Created by Kai-Chun Lin on 2015/4/18.
 */
public class OnPageChangeListenerAdapter extends BaseAdapter implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {
    public static final float LEFT_OF_CENTER = -1f;
    public static final float CENTER = 0f;
    public static final float RIGHT_OF_CENTER = 1f;

    /**
     * Keeps the left fragment in the center of the screen
     */
    public static final ITransitionHandler LEFT_IN_PLACE = new ITransitionHandler() {
        float oldX;

        @Override
        public void onUpdateProgress(ITransitionController controller, View target, float progress) {
            float x = 0;
            if (progress <= 0) {
                x = target.getWidth() * -progress;
                if (oldX == x) {
                    return;
                }
                oldX = x;
                target.setTranslationX(x);
            } else {
                target.setTranslationX(0);
            }
            if (TransitionConfig.isDebug()) {
                controller.getTransitionStateHolder().append(controller.getId(), this, "CUSTOM updateProgress=" + progress + ": \t[" + controller.getStart() + ".." + controller.getEnd() + "], translationX=" + x);
            }
        }

        @Override
        public void onUpdateTime(ITransitionController controller, View target, int time) {

        }
    };

    /**
     * Keeps the right fragment in the center of the screen
     */
    public static final ITransitionHandler RIGHT_IN_PLACE = new ITransitionHandler() {
        float oldX;

        @Override
        public void onUpdateProgress(ITransitionController controller, View target, float progress) {
            float x = 0;
            if (progress > 0 && progress <= 1) {
                x = target.getWidth() * -progress;
                if (oldX == x) {
                    return;
                }
                oldX = x;
                target.setTranslationX(x);
            } else {
                target.setTranslationX(0);
            }
            if (TransitionConfig.isDebug()) {
                controller.getTransitionStateHolder().append(controller.getId(), this, "CUSTOM updateProgress=" + progress + ": \t[" + controller.getStart() + ".." + controller.getEnd() + "], translationX=" + x);
            }
        }

        @Override
        public void onUpdateTime(ITransitionController controller, View target, int time) {

        }
    };

    @CheckResult
    public static OnPageChangeListenerAdapter bind(@NonNull ViewPager viewPager) {
        return bind(viewPager, false);
    }

    @CheckResult
    public static OnPageChangeListenerAdapter bind(@NonNull ViewPager viewPager, boolean reverseDrawingOrder) {
        OnPageChangeListenerAdapter adapter = new OnPageChangeListenerAdapter(viewPager);
        adapter.init(reverseDrawingOrder);
        return adapter;
    }

    public static OnPageChangeListenerAdapter bindWithRotationYTransition(@NonNull ViewPager viewPager) {
        return bindWithRotationYTransition(bind(viewPager));
    }

    public static OnPageChangeListenerAdapter bindWithRotationYTransition(@NonNull OnPageChangeListenerAdapter adapter) {
        adapter.addTransition(ViewTransitionBuilder.transit().range(CENTER, RIGHT_OF_CENTER).rotationY(0, -40).alpha(1f, 0.25f));
        adapter.addTransition(ViewTransitionBuilder.transit().range(CENTER, LEFT_OF_CENTER).rotationY(0, 40).alpha(1f, 0.25f));
        return adapter;
    }

    public static OnPageChangeListenerAdapter bindWithZoomOutTransition(@NonNull ViewPager viewPager) {
        return bindWithZoomOutTransition(bind(viewPager));
    }

    public static OnPageChangeListenerAdapter bindWithZoomOutTransition(@NonNull OnPageChangeListenerAdapter adapter) {
        return adapter.addAndSetTransition(ViewTransitionBuilder.transit().scale(1f, 0.85f).alpha(1f, 0.5f), CENTER, RIGHT_OF_CENTER * 0.15f);
    }

    public static OnPageChangeListenerAdapter bindWithDepthTransition(@NonNull ViewPager viewPager) {
        return bindWithDepthTransition(bind(viewPager, true));
    }

    public static OnPageChangeListenerAdapter bindWithDepthTransition(@NonNull OnPageChangeListenerAdapter adapter) {
        adapter.addTransition(ViewTransitionBuilder.transit().range(CENTER, RIGHT_OF_CENTER * 0.25f).scale(1f, 0.75f).id("RIGHT_1"));
        adapter.addTransition(ViewTransitionBuilder.transit().range(CENTER, RIGHT_OF_CENTER).alpha(1f, 0.5f).id("RIGHT_2").addTransitionHandler(RIGHT_IN_PLACE));
        return adapter;
    }

    public static OnPageChangeListenerAdapter bindWithRotate(@NonNull OnPageChangeListenerAdapter adapter) {
        adapter.addTransition(ViewTransitionBuilder.transit().range(CENTER, LEFT_OF_CENTER).id("LEFT_CENTER").addTransitionHandler(LEFT_IN_PLACE));
        adapter.addTransition(ViewTransitionBuilder.transit().range(CENTER, LEFT_OF_CENTER * 0.5f).rotationY(0, -90).scale(1f, 0.5f).id("LEFT"));
        adapter.addTransition(ViewTransitionBuilder.transit().range(CENTER, RIGHT_OF_CENTER).id("RIGHT_CENTER").addTransitionHandler(RIGHT_IN_PLACE));
        adapter.addTransition(ViewTransitionBuilder.transit().range(CENTER, RIGHT_OF_CENTER * 0.5f).rotationY(0, 90).scale(1f, 0.5f).id("RIGHT"));
        return adapter;
    }

    private static TransitionStateLogger getTransitionStateHolder(@NonNull View view) {
        return (TransitionStateLogger) view.getTag(R.id.debug_id);
    }

    private final ViewPager mViewPager;
    private final WeakHashMap<View, PageHolder> mAnimationListMap = new WeakHashMap<>();

    public OnPageChangeListenerAdapter(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    public void init(boolean reverseDrawingOrder) {
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setPageTransformer(reverseDrawingOrder, this);
    }

    public OnPageChangeListenerAdapter addAndSetTransition(@NonNull ViewTransitionBuilder builder) {
        return addAndSetTransition(builder, CENTER, LEFT_OF_CENTER);
    }

    public OnPageChangeListenerAdapter addAndSetTransition(@NonNull ViewTransitionBuilder builder, float start, float end) {
        ViewTransition vt = builder.range(start, end).id("LEFT").build();
        mTransitionList.put(vt.getId(), vt);
        vt = builder.clone().range(-start, -end).id("RIGHT").build();
        mTransitionList.put(vt.getId(), vt);
        return this;
    }

    public boolean startTransition() {
        throw new UnsupportedOperationException();
    }

    public boolean startTransition(float progress) {
        throw new UnsupportedOperationException();
    }

    private boolean startTransition(@NonNull View page) {
        if (getAdapterState().isTransiting()) {
            return false;
        }

        getAdapterState().setTransiting(true);
        notifyStartTransition();

        PageHolder holder = mAnimationListMap.get(page);
        if (holder == null) {
            holder = new PageHolder(page, mTransitionList);
            mAnimationListMap.put(page, holder);

            for (ITransition trans : holder.mAnimationList.values()) {
                trans.setUpdateStateAfterUpdateProgress(true);
                trans.setTarget(page);
                trans.startTransition();
            }
        }

        return true;
    }

    public void updateProgress(float value) {
        throw new UnsupportedOperationException();
    }

    protected void updateProgress(@NonNull View page, float value) {
        PageHolder holder = mAnimationListMap.get(page);
        if (holder == null) {
            if(TransitionConfig.isDebug()) {
                Log.e(getClass().getSimpleName(), "updateProgress: NULL");
            }
            return;
        }
        for (ITransition trans : holder.mAnimationList.values()) {
            trans.updateProgress(value);
        }
    }

    public void stopTransition() {
        getAdapterState().setTransiting(false);
        notifyStopTransition();

        for (PageHolder holder : mAnimationListMap.values()) {
            for (ITransition trans : holder.mAnimationList.values()) {
                trans.stopTransition();
            }
        }
        mAnimationListMap.clear();
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
//        if(!page.getTag(R.mId.debug_id).equals("1")) {
//            return;
//        }
        startTransition(page);
        updateProgress(page, position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                stopTransition();
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
            case ViewPager.SCROLL_STATE_SETTLING:
                break;
        }
    }

    private static class PageHolder {
        final Map<String, ITransition> mAnimationList = new HashMap<>();

        public PageHolder(@NonNull View page, @NonNull Map<String, ITransition> animationList) {
            for (ITransition trans : animationList.values()) {
                trans = trans.clone();
                trans.setTarget(page);
                mAnimationList.put(trans.getId(), trans);
            }
        }
    }
}
