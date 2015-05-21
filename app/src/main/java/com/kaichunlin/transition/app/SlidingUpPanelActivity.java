package com.kaichunlin.transition.app;

import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.kaichunlin.transition.MenuItemTransitionBuilder;
import com.kaichunlin.transition.ViewTransitionBuilder;
import com.kaichunlin.transition.adapter.MenuOptionConfiguration;
import com.kaichunlin.transition.adapter.SlidingUpPanelLayoutAdapter;
import com.kaichunlin.transition.util.ViewUtil;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import kaichunlin.transition.app.R;


public class SlidingUpPanelActivity extends AppCompatActivity implements View.OnClickListener {

    private SlidingUpPanelLayoutAdapter mSlidingUpPanelLayoutAdapter;
    private Toolbar mToolbar;
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private View mLastSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_slideup_actionbar);
        findViewById(R.id.rotate_slide).setOnClickListener(this);
        findViewById(R.id.sliding_actionbar_view).setOnClickListener(this);
        findViewById(R.id.change_actionbar_color).setOnClickListener(this);
        findViewById(R.id.change_actionbar_color_hsv).setOnClickListener(this);
        findViewById(R.id.fading_actionbar).setOnClickListener(this);
        findViewById(R.id.rotating_actionbar).setOnClickListener(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        SlidingUpPanelLayout supl = ((SlidingUpPanelLayout) findViewById(R.id.sliding_layout));

        mSlidingUpPanelLayoutAdapter = new SlidingUpPanelLayoutAdapter();
        supl.setPanelSlideListener(mSlidingUpPanelLayoutAdapter);

        //this is required since some transition requires the width/height/position of a view, which is not yet properly initialized until layout is complete
        //in this example, another way of achieving correct behavior without using ViewUtil.executeOnGlobalLayout() would be to change all
        // translationYAsFractionOfHeight() calls to delayTranslationYAsFractionOfHeight() which would defer the calculation until the transition is just about to start
        ViewUtil.executeOnGlobalLayout(findViewById(R.id.rotate_slide), new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                onClick(findViewById(R.id.rotate_slide));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sliding_up_panel, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        super.onOptionsItemSelected(menu);

        switch (menu.getItemId()) {
            case R.id.menu_default:
                mInterpolator = new AccelerateDecelerateInterpolator();
                break;
            case R.id.menu_linear:
                mInterpolator = new LinearInterpolator();
                break;
            case R.id.menu_accelerate:
                mInterpolator = new AccelerateInterpolator();
                break;
            case R.id.menu_decelerate:
                mInterpolator = new DecelerateInterpolator();
                break;
            case R.id.menu_linearin:
                mInterpolator = new FastOutLinearInInterpolator();
                break;
            case R.id.menu_anticipate:
                mInterpolator = new AnticipateInterpolator();
                break;
        }
        onClick(mLastSelection);

        return true;
    }

    @Override
    public void onClick(View v) {
        mSlidingUpPanelLayoutAdapter.clearTransition();
        mLastSelection=v;

        ViewTransitionBuilder builder;
        switch (v.getId()) {
            //TODO visual artifact on Android 5.1, why???
            case R.id.rotate_slide:
                builder = ViewTransitionBuilder.transit(findViewById(R.id.content_bg)).interpolator(mInterpolator).rotationX(45f).scale(0.8f).translationYAsFractionOfHeight(-0.5f).adapter(mSlidingUpPanelLayoutAdapter);
                builder.build();
                builder.target(findViewById(R.id.content)).build();
                builder.target(mToolbar).scale(0.8f).translationYAsFractionOfHeight(-1f).build();

                builder = ViewTransitionBuilder.transit(findViewById(R.id.content)).interpolator(mInterpolator).adapter(mSlidingUpPanelLayoutAdapter);
                builder.transitViewGroup(new ViewTransitionBuilder.ViewGroupTransition() {
                    @Override
                    public void transit(ViewTransitionBuilder builder, ViewGroup viewGroup, View view, int index, int total) {
                        builder.range((total - 1 - index) * 0.1f, 1f).translationYAsFractionOfHeight(viewGroup, 1f).build();
                        Log.e(getClass().getSimpleName(), "transit: "+((total - 1 - index) * 0.1f));
                    }
                });
                break;
            case R.id.sliding_actionbar_view:
                mSlidingUpPanelLayoutAdapter.addTransition(ViewTransitionBuilder.transit(mToolbar).interpolator(mInterpolator).translationYAsFractionOfHeight(-1f));
                builder=ViewTransitionBuilder.transit(findViewById(R.id.content)).interpolator(mInterpolator).translationYAsFractionOfHeight(-0.5f).adapter(mSlidingUpPanelLayoutAdapter);
                builder.build();
                builder.target(findViewById(R.id.content_bg2)).build();
                break;
            case R.id.change_actionbar_color:
                mSlidingUpPanelLayoutAdapter.addTransition(ViewTransitionBuilder.transit(mToolbar).interpolator(mInterpolator).backgroundColorResource(getResources(), R.color.primary, R.color.accent));
                break;
            case R.id.change_actionbar_color_hsv:
                mSlidingUpPanelLayoutAdapter.addTransition( ViewTransitionBuilder.transit(mToolbar).interpolator(mInterpolator).backgroundColorResourceHSV(getResources(), R.color.primary, R.color.drawer_opened));
                break;
            case R.id.fading_actionbar:
                mSlidingUpPanelLayoutAdapter.addTransition(ViewTransitionBuilder.transit(mToolbar).interpolator(mInterpolator).alpha(1f, 0f));

                //TODO this should work, but doesn't
//                MenuItemTransitionBuilder menuBuilder=MenuItemTransitionBuilder.transit(mToolbar).scaleY(1f, 0f).visibleOnStartAnimation(true).invalidateOptionOnStopTransition(this, true);
//                mSlidingUpPanelLayoutAdapter.setupOption(this, new MenuOptionConfiguration(menuBuilder.build(), R.menu.main));

                MenuItemTransitionBuilder menuBuilder=MenuItemTransitionBuilder.transit(mToolbar).interpolator(mInterpolator).scaleY(1f, 0f).visibleOnStartAnimation(true).invalidateOptionOnStopTransition(this, true);
                mSlidingUpPanelLayoutAdapter.setupCloseOption(this, new MenuOptionConfiguration(menuBuilder.reverse().build(), R.menu.main));
                break;
            case R.id.rotating_actionbar:
                mSlidingUpPanelLayoutAdapter.addTransition(
                        ViewTransitionBuilder.transit(mToolbar).interpolator(mInterpolator).delayTranslationYAsFractionOfHeight(-0.5f).delayRotationX(90f).scale(0.8f));
                break;
        }
    }
}
