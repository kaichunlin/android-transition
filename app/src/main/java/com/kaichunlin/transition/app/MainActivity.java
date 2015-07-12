package com.kaichunlin.transition.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kaichunlin.transition.ViewTransitionBuilder;

import kaichunlin.transition.app.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.slideup_actionbar).setOnClickListener(this);

        //TODO currently broken
        findViewById(R.id.slideup_rotate_image).setOnClickListener(this);
        findViewById(R.id.slideup_rotate_image).setVisibility(View.GONE);

        findViewById(R.id.gradient_actionbar).setOnClickListener(this);
        findViewById(R.id.menuitem).setOnClickListener(this);
        findViewById(R.id.drawer).setOnClickListener(this);
        findViewById(R.id.view_pager).setOnClickListener(this);

        //TODO not yet implemented
        findViewById(R.id.observable).setOnClickListener(this);
        findViewById(R.id.observable).setVisibility(View.GONE);

        ViewTransitionBuilder.transit(findViewById(R.id.toolbar)).animator(this, R.anim.animator_set).buildAnimation().startAnimation(1200);

//        TransitionConfig.setDebug(true);
    }

    @Override
    public void onClick(View v) {
        Intent i = null;
        boolean overridePendingTransition=false;
        switch (v.getId()) {
            case R.id.slideup_actionbar:
                i = new Intent(this, SlidingUpPanelActivity.class);
//                overridePendingTransition=true;
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
                overridePendingTransition=true;
                break;
            case R.id.view_pager:
                i = new Intent(this, ViewPagerActivity.class);
                break;
            case R.id.observable:
                //incomplete
                break;
        }
        startActivity(i);
        if(overridePendingTransition) {
            overridePendingTransition(0, 0);
        }
    }
}
