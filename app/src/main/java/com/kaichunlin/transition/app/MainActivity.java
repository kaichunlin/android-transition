package com.kaichunlin.transition.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

        //TODO work in progress
        findViewById(R.id.observable).setOnClickListener(this);
        findViewById(R.id.observable).setVisibility(View.GONE);

//        TransitionConfig.setDebug(true);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch(v.getId()) {
            case R.id.slideup_actionbar:
                i=new Intent(this, SlidingUpPanelActivity.class);
                startActivity(i);
                break;
            case R.id.slideup_rotate_image:
                i=new Intent(this, SlidingUpPanelRotateActivity.class);
                startActivity(i);
                break;
            case R.id.gradient_actionbar:
                i=new Intent(this, DrawerGradientActivity.class);
                startActivity(i);
                break;
            case R.id.menuitem:
                i=new Intent(this, DrawerMenuItemActivity.class);
                startActivity(i);
                break;
            case R.id.drawer:
                i=new Intent(this, DrawerViewActivity.class);
                startActivity(i);
                break;
            case R.id.view_pager:
                i=new Intent(this, ViewPagerActivity.class);
                startActivity(i);
                break;
            case R.id.observable:
                //TODO
                break;
        }
    }
}
