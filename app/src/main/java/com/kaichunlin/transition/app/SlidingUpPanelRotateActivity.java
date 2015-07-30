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
        findViewById(R.id.content_bg).setVisibility(View.VISIBLE);
        findViewById(R.id.content_bg2).setVisibility(View.VISIBLE);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SlidingUpPanelLayout supl=((SlidingUpPanelLayout)findViewById(R.id.sliding_layout));

        //code to transit view
        mSlidingUpPanelLayoutAdapter = new SlidingUpPanelLayoutAdapter();
        ViewTransitionBuilder.transit(findViewById(R.id.content_bg)).rotationY(90).endRange(0.25f).id("BG").buildFor(mSlidingUpPanelLayoutAdapter);
        ViewTransitionBuilder.transit(findViewById(R.id.content_bg2)).rotationY(270, 360).range(0.25f, 0.5f).id("BG_2").buildFor(mSlidingUpPanelLayoutAdapter);
        ViewTransitionBuilder.transit(findViewById(R.id.toolbar)).alpha(0f).buildFor(mSlidingUpPanelLayoutAdapter);
        supl.setPanelSlideListener(mSlidingUpPanelLayoutAdapter);

        mSlidingUpPanelLayoutAdapter.setPanelSlideListener(new DialogPanelSlideListener(this));
    }
}
