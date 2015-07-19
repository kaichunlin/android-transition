package com.kaichunlin.transition.app;

import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.kaichunlin.transition.ViewTransitionBuilder;
import com.kaichunlin.transition.adapter.DrawerListenerAdapter;
import com.kaichunlin.transition.internal.debug.TraceTransitionListener;
import com.kaichunlin.transition.util.TransitionUtil;

import kaichunlin.transition.app.R;


public class DrawerGradientActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private DrawerListenerAdapter mDrawerListenerAdapter;
    private View mGradient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drawer_gradient);
        findViewById(R.id.interpolator_default).setOnClickListener(this);
        findViewById(R.id.interpolator_linear).setOnClickListener(this);
        findViewById(R.id.interpolator_accelerate).setOnClickListener(this);
        findViewById(R.id.interpolator_decelerate).setOnClickListener(this);
        findViewById(R.id.interpolator_fastout).setOnClickListener(this);
        findViewById(R.id.interpolator_anticipate).setOnClickListener(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,              /* host Activity */
                mDrawerLayout,    /* DrawerLayout object */
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        //code to transit view
        mDrawerListenerAdapter = new DrawerListenerAdapter(mDrawerToggle, R.id.drawerList);
        mDrawerListenerAdapter.setDrawerLayout(mDrawerLayout);
        mDrawerListenerAdapter.setDrawerListener(new DialogDrawerListener(this));

        //debug
        mDrawerListenerAdapter.addTransitionListener(new TraceTransitionListener());

        mGradient = findViewById(R.id.gradient);
        TransitionUtil.executeOnGlobalLayout(mGradient, new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!mDrawerListenerAdapter.isOpened(DrawerGradientActivity.this)) {
                    mGradient.setTranslationX(-mGradient.getWidth());
                }
                updateTransition(findViewById(R.id.interpolator_default));
            }
        });
    }

    @Override
    public void onClick(View v) {
        updateTransition(v);
    }

    public void updateTransition(View v) {
        mDrawerListenerAdapter.removeAllTransitions();

        ViewTransitionBuilder builder = ViewTransitionBuilder.transit(mGradient).translationX(-mGradient.getWidth(), 0);
        switch (v.getId()) {
            case R.id.interpolator_default:
                break;
            case R.id.interpolator_linear:
                builder.interpolator(new LinearInterpolator());
                break;
            case R.id.interpolator_accelerate:
                builder.interpolator(new AccelerateInterpolator());
                break;
            case R.id.interpolator_decelerate:
                builder.interpolator(new DecelerateInterpolator());
                break;
            case R.id.interpolator_fastout:
                builder.interpolator(new FastOutLinearInInterpolator());
                break;
            case R.id.interpolator_anticipate:
                builder.interpolator(new AnticipateInterpolator());
                break;
        }
        mDrawerListenerAdapter.addTransition(builder);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }
}
