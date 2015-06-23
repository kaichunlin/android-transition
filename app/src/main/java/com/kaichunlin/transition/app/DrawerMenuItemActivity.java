package com.kaichunlin.transition.app;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.kaichunlin.transition.MenuItemTransition;
import com.kaichunlin.transition.MenuItemTransitionBuilder;
import com.kaichunlin.transition.adapter.DrawerListenerAdapter;
import com.kaichunlin.transition.adapter.MenuOptionConfiguration;

import kaichunlin.transition.app.R;


public class DrawerMenuItemActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private DrawerListenerAdapter mDrawerListenerAdapter;
    MenuItemTransition mFlipOpen;
    MenuItemTransition mFlipClose;
    MenuItemTransition mShrinkOpen;
    MenuItemTransition mShrinkClose;
    MenuItemTransition mRotateOpen;
    MenuItemTransition mRotateClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drawer_menuitem);
        findViewById(R.id.flip_fade).setOnClickListener(this);
        findViewById(R.id.shrink_fade).setOnClickListener(this);
        findViewById(R.id.rotate).setOnClickListener(this);

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

        //set up the transition
        //Creates a shared configuration that: applies alpha, the transition effect is applied in a cascading manner
        // (v.s. simultaneously), MenuItem will reset to enabled when transiting, and invalidates menu on transition
        // completion
        MenuItemTransitionBuilder builder = MenuItemTransitionBuilder.transit("Flip", toolbar).alpha(1f, 0.5f).scaleX(1f, 0f).translationX(0, 30).cascade(0.3f).visibleOnStartAnimation(true).invalidateOptionOnStopTransition(this, true);
        mFlipOpen = builder.build();
        mFlipClose = builder.reverse().translationX(0, -30).build();
        //overrides some transition while reusing the rest, so clone it
        mShrinkClose = builder.id("Shrink").scale(1f, 0f).build();
        mShrinkOpen = builder.reverse().translationX(0, 30).build();
        builder = MenuItemTransitionBuilder.transit("Rotate", toolbar).rotation(0f, 180f).scale(1f, 0f).cascade(0.15f).visibleOnStartAnimation(true).invalidateOptionOnStopTransition(this, true);
        mRotateOpen = builder.build();
        mRotateClose = builder.reverse().build();

        //set up the adapter
        mDrawerListenerAdapter = new DrawerListenerAdapter(mDrawerToggle, R.id.drawerList);
        mDrawerListenerAdapter.setDrawerLayout(mDrawerLayout);
        mDrawerListenerAdapter.setDrawerListener(new DialogDrawerListener(this));

        //set the initial options
        onClick(findViewById(R.id.flip_fade));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mDrawerListenerAdapter.onCreateOptionsMenu(this, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flip_fade:
                mDrawerListenerAdapter.setupOptions(this, new MenuOptionConfiguration(mFlipOpen, R.menu.drawer), new MenuOptionConfiguration(mFlipClose, R.menu.main));
                break;
            case R.id.shrink_fade:
                mDrawerListenerAdapter.setupOptions(this, new MenuOptionConfiguration(mShrinkOpen, R.menu.drawer), new MenuOptionConfiguration(mShrinkClose, R.menu.main));
                break;
            case R.id.rotate:
                mDrawerListenerAdapter.setupCloseOption(this, new MenuOptionConfiguration(mRotateClose, R.menu.main));
                break;
        }
    }
}
