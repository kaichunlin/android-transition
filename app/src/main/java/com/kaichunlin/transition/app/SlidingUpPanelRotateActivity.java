package com.kaichunlin.transition.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kaichunlin.transition.ViewTransitionBuilder;
import com.kaichunlin.transition.adapter.SlidingUpPanelLayoutAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import kaichunlin.transition.app.R;


public class SlidingUpPanelRotateActivity extends AppCompatActivity {

    private SlidingUpPanelLayoutAdapter mSlidingUpPanelLayoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_slideup_actionbar_transparent);
        findViewById(R.id.content_bg2).setVisibility(View.VISIBLE);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SlidingUpPanelLayout supl=((SlidingUpPanelLayout)findViewById(R.id.sliding_layout));

        //TODO currently broken
        //code to transit view
        mSlidingUpPanelLayoutAdapter = new SlidingUpPanelLayoutAdapter();
        mSlidingUpPanelLayoutAdapter.addTransition(
                ViewTransitionBuilder.transit(findViewById(R.id.content_bg)).rotationY(90).endRange(0.25f).id("BG"));
        mSlidingUpPanelLayoutAdapter.addTransition(
                ViewTransitionBuilder.transit(findViewById(R.id.content_bg2)).rotationY(270, 360).range(0.25f, 0.5f).id("BG_2"));
        supl.setPanelSlideListener(mSlidingUpPanelLayoutAdapter);
        mSlidingUpPanelLayoutAdapter.setPanelSlideListener(new DialogPanelSlideListener(this));
    }
}
