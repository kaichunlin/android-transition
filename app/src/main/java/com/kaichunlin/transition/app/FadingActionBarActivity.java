package com.kaichunlin.transition.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kaichunlin.transition.ViewTransitionBuilder;
import com.kaichunlin.transition.adapter.SlidingUpPanelLayoutAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import kaichunlin.transition.app.R;


public class FadingActionBarActivity extends AppCompatActivity {

    private SlidingUpPanelLayoutAdapter mSlidingUpPanelLayoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_slideup_actionbar);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SlidingUpPanelLayout supl=((SlidingUpPanelLayout)findViewById(R.id.sliding_layout));

        //code to transit view
        mSlidingUpPanelLayoutAdapter = new SlidingUpPanelLayoutAdapter();
        mSlidingUpPanelLayoutAdapter.addTransition(
                ViewTransitionBuilder.transit(findViewById(R.id.toolbar)).alpha(1f, 0f));
        supl.setPanelSlideListener(mSlidingUpPanelLayoutAdapter);
    }
}
