package com.kaichunlin.transition.adapter;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.kaichunlin.transition.DefaultTransitionManager;
import com.kaichunlin.transition.R;
import com.kaichunlin.transition.Transition;
import com.kaichunlin.transition.TransitionConfig;
import com.kaichunlin.transition.TransitionHandler;
import com.kaichunlin.transition.TransitionManager;
import com.kaichunlin.transition.ViewTransition;
import com.kaichunlin.transition.ViewTransitionBuilder;
import com.kaichunlin.transition.internal.TransitionController;
import com.kaichunlin.transition.util.TransitionStateLogger;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Adapter for PageView
 * <p>
 * Created by Kai-Chun Lin on 2015/4/18.
 */
public class OnPageChangeListenerAdapter extends AbstractAdapter implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {
    public static final float LEFT_OF_CENTER = -1f;
    public static final float CENTER = 0f;
    public static final float RIGHT_OF_CENTER = 1f;

    /**
     * Keeps the left fragment in the center of the screen
     */
    public static final TransitionHandler LEFT_IN_PLACE = new TransitionHandler() {
        float oldX;

        @Override
        public void onUpdateProgress(TransitionController controller, View target, float progress) {
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
                controller.getTransitionStateHolder().append(controller.getId() + "->" + target, this, "CUSTOM updateProgress=" + progress + ": \t[" + controller.getStart() + ".." + controller.getEnd() + "], translationX=" + x);
            }
        }
    };

    /**
     * Keeps the right fragment in the center of the screen
     */
    public static final TransitionHandler RIGHT_IN_PLACE = new TransitionHandler() {
        float oldX;

        @Override
        public void onUpdateProgress(TransitionController controller, View target, float progress) {
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
                controller.getTransitionStateHolder().append(controller.getId() + "->" + target, this, "CUSTOM updateProgress=" + progress + ": \t[" + controller.getStart() + ".." + controller.getEnd() + "], translationX=" + x);
            }
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
    private final WeakHashMap<View, PageHolder> mTransitionListMap = new WeakHashMap<>();

    public OnPageChangeListenerAdapter(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    public void init(boolean reverseDrawingOrder) {
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setPageTransformer(reverseDrawingOrder, this);
    }

    public OnPageChangeListenerAdapter addAndSetTransition(@NonNull ViewTransitionBuilder builder) {
        return addAndSetTransition(builder, CENTER, LEFT_OF_CENTER);
    }

    public OnPageChangeListenerAdapter addAndSetTransition(@NonNull ViewTransitionBuilder builder, float start, float end) {
        ViewTransition vt = builder.range(start, end).id("LEFT").build();
        getTransitionManager().addTransition(vt);
        vt = builder.clone().range(-start, -end).id("RIGHT").build();
        getTransitionManager().addTransition(vt);
        return this;
    }

    public boolean startTransition() {
        throw new UnsupportedOperationException();
    }

    public boolean startTransition(float progress) {
        throw new UnsupportedOperationException();
    }

    private boolean startTransition(@NonNull View page) {
        if (!getAdapterState().isTransiting()) {
            notifyTransitionStart();
        }

        PageHolder holder = mTransitionListMap.get(page);
        if (holder == null) {
            holder = new PageHolder(page, getTransitionManager().getTransitions());
            mTransitionListMap.put(page, holder);

            List<Transition> transitionList = holder.mTransitionManager.getTransitions();
            final int size = transitionList.size();
            Transition transition;
            for (int i = 0; i < size; i++) {
                transition = transitionList.get(i);
                transition.setUpdateStateAfterUpdateProgress(true);
                transition.setTarget(page);
                transition.startTransition();
            }
        }

        return true;
    }

    public void updateProgress(float value) {
        throw new UnsupportedOperationException();
    }

    protected void updateProgress(@NonNull View page, float value) {
        PageHolder holder = mTransitionListMap.get(page);
        if (holder == null) {
//            if (TransitionConfig.isDebug()) {
//                Log.e(getClass().getSimpleName(), "updateProgress: NULL");
//            }
            return;
        }
        holder.mTransitionManager.updateProgress(value);
    }

    public void stopTransition() {
        getAdapterState().setTransiting(false);
        notifyTransitionEnd();

        for (PageHolder holder : mTransitionListMap.values()) {
            holder.mTransitionManager.stopTransition();
        }
        mTransitionListMap.clear();
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        if (getAdapterState().isTransiting()) {
            startTransition(page);
            updateProgress(page, position);
        }
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
                getAdapterState().setTransiting(true);
                break;
        }
    }

    private static class PageHolder {
        final TransitionManager mTransitionManager = new DefaultTransitionManager();

        public PageHolder(@NonNull View page, @NonNull List<Transition> transitionsList) {
            final int size = transitionsList.size();
            for (int i = 0; i < size; i++) {
                mTransitionManager.addTransition(transitionsList.get(i).clone());
            }
        }
    }
}
