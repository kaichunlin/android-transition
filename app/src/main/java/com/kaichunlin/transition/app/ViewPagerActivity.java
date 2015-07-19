package com.kaichunlin.transition.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.kaichunlin.transition.adapter.OnPageChangeListenerAdapter;
import com.kaichunlin.transition.internal.debug.TraceTransitionListener;

import kaichunlin.transition.app.R;


public class ViewPagerActivity extends AppCompatActivity {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    private DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    private ViewPager mViewPager;

    private OnPageChangeListenerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mDemoCollectionPagerAdapter =
                new DemoCollectionPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);

//        TransitionConfig.setPrintDebug(true);

        adapter = OnPageChangeListenerAdapter.bindWithRotationYTransition(mViewPager);

        //debug
        adapter.addTransitionListener(new TraceTransitionListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewpager, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        super.onOptionsItemSelected(menu);

        switch (menu.getItemId()) {
            case R.id.trans_y:
                adapter.removeAllTransitions();
                OnPageChangeListenerAdapter.bindWithRotationYTransition(adapter);
                break;
            case R.id.zoom:
                adapter.removeAllTransitions();
                OnPageChangeListenerAdapter.bindWithZoomOutTransition(adapter);
                break;
            case R.id.depth:
                //has to rebuild adapter since the drawing order needs to be reversed
                adapter = OnPageChangeListenerAdapter.bindWithDepthTransition(mViewPager);
                break;
            case R.id.rotate:
                OnPageChangeListenerAdapter.bindWithRotate(adapter);
                break;
        }

        return true;
    }

    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new ViewPagerActivityFragment();
            Bundle args = new Bundle();
            int id;
            switch (i) {
                case 0:
                    id = R.drawable.bg;
                    break;
                case 1:
                    id = R.drawable.bg2;
                    break;
                case 2:
                    id = R.drawable.bg3;
                    break;
                case 3:
                    id = R.drawable.bg4;
                    break;
                default:
                    id = 0;
                    break;
            }
            args.putInt(ViewPagerActivityFragment.ID, i);
            args.putInt(ViewPagerActivityFragment.RES_ID, id);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Image " + (position + 1);
        }
    }
}
