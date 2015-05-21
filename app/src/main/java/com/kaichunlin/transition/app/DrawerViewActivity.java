package com.kaichunlin.transition.app;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ListView;

import com.kaichunlin.transition.ViewTransitionBuilder;
import com.kaichunlin.transition.adapter.DrawerListenerAdapter;

import kaichunlin.transition.app.R;


public class DrawerViewActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private DrawerListenerAdapter mDrawerListenerAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drawer_view);
        findViewById(R.id.rotate_android).setOnClickListener(this);
        findViewById(R.id.slide_subviews).setOnClickListener(this);
        findViewById(R.id.slide_bg).setOnClickListener(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,              /* host Activity */
                mDrawerLayout,    /* DrawerLayout object */
                toolbar,
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        );

        mListView= (ListView) findViewById(R.id.drawerList);

        //code to transit view
        mDrawerListenerAdapter = new DrawerListenerAdapter(mDrawerToggle, R.id.drawerList);
        mDrawerListenerAdapter.setDrawerLayout(mDrawerLayout);

        onClick(findViewById(R.id.rotate_android));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public void onClick(View v) {
        mDrawerListenerAdapter.clearTransition();

        switch (v.getId()) {
            case R.id.rotate_android:
                mDrawerListenerAdapter.addTransition(
                        ViewTransitionBuilder.transit(findViewById(R.id.big_icon)).rotation(0f, 360f).scaleX(1f, 0.2f).scaleY(1f, 0f).translationX(200f));
                break;
            case R.id.slide_subviews:
                ViewTransitionBuilder.transit(findViewById(R.id.lay_buttons)).interpolator(new AccelerateInterpolator()).adapter(mDrawerListenerAdapter).transitViewGroup(new ViewTransitionBuilder.ViewGroupTransition() {
                    @Override
                    public void transit(ViewTransitionBuilder builder, ViewGroup viewGroup, View view, int index, int total) {
                        builder.range((total - 1 - index) * 0.2f, 1f).translationX(viewGroup.getRight()).build();
                    }
                });
                break;
            case R.id.slide_bg:
                mDrawerListenerAdapter.addTransition(ViewTransitionBuilder.transit(findViewById(R.id.bg)).interpolator(new LinearInterpolator()).translationXAsFractionOfWidth(0.25f));
                break;
        }
    }
}
