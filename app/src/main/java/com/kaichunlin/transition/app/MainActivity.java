package com.kaichunlin.transition.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;

import com.kaichunlin.transition.TransitionConfig;
import com.kaichunlin.transition.ViewTransitionBuilder;
import com.kaichunlin.transition.animation.AnimationManager;
import com.kaichunlin.transition.util.TransitionUtil;

import kaichunlin.transition.app.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.slideup_actionbar).setOnClickListener(this);
        findViewById(R.id.slideup_rotate_image).setOnClickListener(this);
        findViewById(R.id.gradient_actionbar).setOnClickListener(this);
        findViewById(R.id.menuitem).setOnClickListener(this);
        findViewById(R.id.drawer).setOnClickListener(this);
        findViewById(R.id.view_pager).setOnClickListener(this);

        //TODO not yet implemented
        findViewById(R.id.observable).setOnClickListener(this);
        findViewById(R.id.observable).setVisibility(View.GONE);

        ViewTransitionBuilder.transit(findViewById(R.id.toolbar)).animator(this, R.anim.animator_set).buildAnimation().startAnimation(1000);

        TransitionUtil.executeOnGlobalLayout(this, new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final AnimationManager am = new AnimationManager();
                ViewTransitionBuilder builder = ViewTransitionBuilder.transit(findViewById(R.id.main_btns));
                ViewTransitionBuilder.Cascade cascade = new ViewTransitionBuilder.Cascade(0.6f);
                builder.transitViewGroup(new ViewTransitionBuilder.ViewGroupTransition() {
                    @Override
                    public void transit(ViewTransitionBuilder builder, ViewTransitionBuilder.ViewGroupTransitionConfig config) {
                        float start = builder.getStartRange();
                        float end = builder.getEndRange();
                        float middleRange = start + builder.getRange() / 2;
                        builder.scale(1f, 1.15f).range(start, middleRange).buildAnimationFor(am);
                        builder.scale(1.15f, 1f).range(middleRange, end).buildAnimationFor(am);
                    }
                }, cascade);
                am.startAnimation(1000);
            }
        });

        TransitionConfig.setDebug(false);
    }

    @Override
    public void onClick(View v) {
        Intent i = null;
        boolean overridePendingTransition = false;
        switch (v.getId()) {
            case R.id.slideup_actionbar:
                i = new Intent(this, SlidingUpPanelActivity.class);
                break;
            case R.id.slideup_rotate_image:
                i = new Intent(this, SlidingUpPanelRotateActivity.class);
                break;
            case R.id.gradient_actionbar:
                i = new Intent(this, DrawerGradientActivity.class);
                break;
            case R.id.menuitem:
                i = new Intent(this, DrawerMenuItemActivity.class);
                break;
            case R.id.drawer:
                i = new Intent(this, DrawerViewActivity.class);
                overridePendingTransition = true;
                break;
            case R.id.view_pager:
                i = new Intent(this, ViewPagerActivity.class);
                break;
            case R.id.observable:
                //incomplete
                break;
        }
        AnimationManager am = new AnimationManager();
        ViewTransitionBuilder.transit(v).scale(1f, 1.2f).range(0f, 0.5f).buildAnimationFor(am);
        ViewTransitionBuilder.transit(v).scale(1.2f, 1f).range(0.5f, 1f).buildAnimationFor(am);
        am.startAnimation(300);

        final Intent i2 = i;
        final boolean overridePendingTransition2 = overridePendingTransition;
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i2);
                if (overridePendingTransition2) {
                    overridePendingTransition(0, 0);
                }
            }
        }, 300);
    }
}
