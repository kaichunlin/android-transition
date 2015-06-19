package com.kaichunlin.transition.adapter;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.kaichunlin.transition.CustomTransitionController;
import com.kaichunlin.transition.ITransition;
import com.kaichunlin.transition.R;
import com.kaichunlin.transition.TransitionConfig;
import com.kaichunlin.transition.ViewTransition;
import com.kaichunlin.transition.ViewTransitionBuilder;
import com.kaichunlin.transition.util.TransitionStateHolder;

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
    public static final CustomTransitionController LEFT_IN_PLACE = new CustomTransitionController() {
        float oldX;

        @Override
        public void updateProgress(View target, float progress) {
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
                getTransitionStateHolder().append(getId(), this, "CUSTOM updateProgress=" + progress + ": \t[" + getStart() + ".." + getEnd() + "], translationX=" + x);
            }
        }
    };
    
    /**
     * Keeps the right fragment in the center of the screen
     */
    public static final CustomTransitionController RIGHT_IN_PLACE = new CustomTransitionController() {
        float oldX;

        @Override
        public void updateProgress(View target, float progress) {
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
                getTransitionStateHolder().append(getId(), this, "CUSTOM updateProgress=" + progress + ": \t[" + getStart() + ".." + getEnd() + "], translationX=" + x);
            }
        }
    };

    public static OnPageChangeListenerAdapter bind(ViewPager viewPager) {
        return bind(viewPager, false);
    }

    public static OnPageChangeListenerAdapter bind(ViewPager viewPager, boolean reverseDrawingOrder) {
        OnPageChangeListenerAdapter adapter = new OnPageChangeListenerAdapter(viewPager);
        adapter.init(reverseDrawingOrder);
        return adapter;
    }

    public static OnPageChangeListenerAdapter bindWithRotationYTransition(ViewPager viewPager) {
        return bindWithRotationYTransition(bind(viewPager));
    }

    public static OnPageChangeListenerAdapter bindWithRotationYTransition(OnPageChangeListenerAdapter adapter) {
        adapter.addTransition(ViewTransitionBuilder.transit().range(CENTER, RIGHT_OF_CENTER).rotationY(0, -40).alpha(1f, 0.25f));
        adapter.addTransition(ViewTransitionBuilder.transit().range(CENTER, LEFT_OF_CENTER).rotationY(0, 40).alpha(1f, 0.25f));
        return adapter;
    }

    public static OnPageChangeListenerAdapter bindWithZoomOutTransition(ViewPager viewPager) {
        return bindWithZoomOutTransition(bind(viewPager));
    }

    public static OnPageChangeListenerAdapter bindWithZoomOutTransition(OnPageChangeListenerAdapter adapter) {
        return adapter.addAndSetTransition(ViewTransitionBuilder.transit().scale(1f, 0.85f).alpha(1f, 0.5f), CENTER, RIGHT_OF_CENTER * 0.15f);
    }

    public static OnPageChangeListenerAdapter bindWithDepthTransition(ViewPager viewPager) {
        return bindWithDepthTransition(bind(viewPager, true));
    }

    public static OnPageChangeListenerAdapter bindWithDepthTransition(OnPageChangeListenerAdapter adapter) {
        adapter.addTransition(ViewTransitionBuilder.transit().range(CENTER, RIGHT_OF_CENTER * 0.25f).scale(1f, 0.75f).id("RIGHT_1"));
        adapter.addTransition(ViewTransitionBuilder.transit().range(CENTER, RIGHT_OF_CENTER).alpha(1f, 0.5f).id("RIGHT_2").addTransitionController(RIGHT_IN_PLACE));
        return adapter;
    }

    public static OnPageChangeListenerAdapter bindWithRotate(OnPageChangeListenerAdapter adapter) {
        adapter.addTransition(ViewTransitionBuilder.transit().range(CENTER, LEFT_OF_CENTER).id("LEFT_CENTER").addTransitionController(LEFT_IN_PLACE));
        adapter.addTransition(ViewTransitionBuilder.transit().range(CENTER, LEFT_OF_CENTER * 0.5f).rotationY(0, -90).scale(1f, 0.5f).id("LEFT"));
        adapter.addTransition(ViewTransitionBuilder.transit().range(CENTER, RIGHT_OF_CENTER).id("RIGHT_CENTER").addTransitionController(RIGHT_IN_PLACE));
        adapter.addTransition(ViewTransitionBuilder.transit().range(CENTER, RIGHT_OF_CENTER * 0.5f).rotationY(0, 90).scale(1f, 0.5f).id("RIGHT"));
        return adapter;
    }

    private static TransitionStateHolder getTransitionStateHolder(View view) {
        return (TransitionStateHolder) view.getTag(R.id.debug_id);
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

    public OnPageChangeListenerAdapter addAndSetTransition(ViewTransitionBuilder builder) {
        return addAndSetTransition(builder, CENTER, LEFT_OF_CENTER);
    }

    public OnPageChangeListenerAdapter addAndSetTransition(ViewTransitionBuilder builder, float start, float end) {
        ViewTransition vt = builder.range(start, end).id("LEFT").build();
        mTransitionList.put(vt.getId(), vt);
        vt = builder.clone().range(-start, -end).id("RIGHT").build();
        mTransitionList.put(vt.getId(), vt);
        return this;
    }

    protected void startTransition() {
        throw new UnsupportedOperationException();
    }

    protected boolean startTransition(float progress) {
        throw new UnsupportedOperationException();
    }

    private void startTransition(View page) {
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
    }

    protected void updateProgress(float value) {
        throw new UnsupportedOperationException();
    }

    protected void updateProgress(View page, float value) {
        PageHolder holder = mAnimationListMap.get(page);
        if (holder == null) {
            Log.e(getClass().getSimpleName(), "updateProgress: NULL");
            return;
        }
        for (ITransition trans : holder.mAnimationList.values()) {
            trans.updateProgress(value);
        }
    }

    public void stopTransition() {
        for (PageHolder holder : mAnimationListMap.values()) {
            for (ITransition trans : holder.mAnimationList.values()) {
                trans.stopTransition();
            }
        }
        mAnimationListMap.clear();
        mTransitioning = false;
    }

    @Override
    public void transformPage(View page, float position) {
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

        public PageHolder(View page, Map<String, ITransition> animationList) {
            for (ITransition trans : animationList.values()) {
                trans = trans.clone();
                trans.setTarget(page);
                mAnimationList.put(trans.getId(), trans);
            }
        }
    }
}
