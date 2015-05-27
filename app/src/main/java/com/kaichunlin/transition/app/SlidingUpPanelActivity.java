package com.kaichunlin.transition.app;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.kaichunlin.transition.CustomTransitionController;
import com.kaichunlin.transition.ViewTransitionBuilder;
import com.kaichunlin.transition.adapter.SlidingUpPanelLayoutAdapter;
import com.kaichunlin.transition.util.ViewUtil;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import kaichunlin.transition.app.R;


public class SlidingUpPanelActivity extends AppCompatActivity implements View.OnClickListener {

    private SlidingUpPanelLayoutAdapter mSlidingUpPanelLayoutAdapter;
    private Toolbar mToolbar;
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private View mLastSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_slideup_actionbar);
        findViewById(R.id.rotate_slide).setOnClickListener(this);
        findViewById(R.id.sliding_actionbar_view).setOnClickListener(this);
        findViewById(R.id.change_actionbar_color).setOnClickListener(this);
        findViewById(R.id.change_actionbar_color_hsv).setOnClickListener(this);
        findViewById(R.id.fading_actionbar).setOnClickListener(this);
        findViewById(R.id.rotating_actionbar).setOnClickListener(this);
        findViewById(R.id.grayscale_bg).setOnClickListener(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        SlidingUpPanelLayout supl = ((SlidingUpPanelLayout) findViewById(R.id.sliding_layout));

        //set up the adapter
        mSlidingUpPanelLayoutAdapter = new SlidingUpPanelLayoutAdapter();
        supl.setPanelSlideListener(mSlidingUpPanelLayoutAdapter);

        //this is required since some transition requires the width/height/position of a view, which is not yet properly initialized until layout is complete
        //in this example, another way of achieving correct behavior without using ViewUtil.executeOnGlobalLayout() would be to change all
        // translationYAsFractionOfHeight() calls to delayTranslationYAsFractionOfHeight() which would defer the calculation until the transition is just about to start
        ViewUtil.executeOnGlobalLayout(findViewById(R.id.rotate_slide), new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                updateTransition(findViewById(R.id.rotate_slide));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sliding_up_panel, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        super.onOptionsItemSelected(menu);

        switch (menu.getItemId()) {
            case R.id.menu_default:
                mInterpolator = new AccelerateDecelerateInterpolator();
                break;
            case R.id.menu_linear:
                mInterpolator = new LinearInterpolator();
                break;
            case R.id.menu_accelerate:
                mInterpolator = new AccelerateInterpolator();
                break;
            case R.id.menu_decelerate:
                mInterpolator = new DecelerateInterpolator();
                break;
            case R.id.menu_linearin:
                mInterpolator = new FastOutLinearInInterpolator();
                break;
            case R.id.menu_anticipate:
                mInterpolator = new AnticipateInterpolator();
                break;
        }
        updateTransition(mLastSelection);

        return true;
    }

    @Override
    public void onClick(View v) {
        updateTransition(v);
    }

    public void updateTransition(View v) {
        mSlidingUpPanelLayoutAdapter.clearTransition();
        mLastSelection = v;

        //configure a builder with properties shared by all instances and just clone it for future use
        //since adapter(ITransitionAdapter) is set, simply call build() would add the resultant ViewTransition to the adapter
        ViewTransitionBuilder baseBuilder = ViewTransitionBuilder.transit(mToolbar).interpolator(mInterpolator).adapter(mSlidingUpPanelLayoutAdapter);
        ViewTransitionBuilder builder;
        ((ImageView) findViewById(R.id.content_bg)).getDrawable().setColorFilter(null);
        boolean setHalfHeight = false;
        switch (v.getId()) {
            //TODO visual artifact on Android 5.1 when rotationX is ~45, why???
            case R.id.rotate_slide:
                builder = baseBuilder.clone();
                builder.scale(0.8f).translationYAsFractionOfHeight(-1f).build();

                builder = baseBuilder.clone().target(findViewById(R.id.content_bg)).rotationX(42f).scale(0.8f).translationYAsFractionOfHeight(-0.5f);
                builder.build();
                builder.target(findViewById(R.id.content)).build();

                builder = baseBuilder.clone().target(findViewById(R.id.content));
                builder.transitViewGroup(new ViewTransitionBuilder.ViewGroupTransition() {
                    @Override
                    public void transit(ViewTransitionBuilder builder, ViewGroup viewGroup, View view, int index, int total) {
                        builder.range((total - 1 - index) * 0.1f, 1f).translationYAsFractionOfHeight(viewGroup, 1f).build();
                    }
                });
                break;
            case R.id.sliding_actionbar_view:
                baseBuilder.clone().translationYAsFractionOfHeight(-1f).build();
                builder = baseBuilder.clone().target(findViewById(R.id.content)).translationYAsFractionOfHeight(-0.5f);
                builder.build();
                //apply the exact same transition to another view
                builder.target(findViewById(R.id.content_bg2)).build();
                break;
            case R.id.change_actionbar_color:
                baseBuilder.clone().backgroundColorResource(getResources(), R.color.primary, R.color.accent).build();
                break;
            case R.id.change_actionbar_color_hsv:
                baseBuilder.clone().backgroundColorResourceHSV(getResources(), R.color.primary, R.color.drawer_opened).build();
                break;
            case R.id.fading_actionbar:
                baseBuilder.clone().alpha(1f, 0f).build();
                break;
            case R.id.rotating_actionbar:
                baseBuilder.clone().delayTranslationYAsFractionOfHeight(-0.5f).delayRotationX(90f).scale(0.8f).build();
                break;
            case R.id.grayscale_bg:
                //Uses a CustomTransitionController that applies a ColorMatrixColorFilter to the background view
                baseBuilder.clone().addTransitionController(new CustomTransitionController() {
                    ColorMatrix matrix = new ColorMatrix();

                    @Override
                    public void updateProgress(float progress) {
                        matrix.setSaturation(1 - progress);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        ((ImageView) findViewById(R.id.content_bg)).getDrawable().setColorFilter(filter);
                    }
                }).build();
                setHalfHeight = true;
                break;
        }

        setSlidingUpPanelHeight(setHalfHeight);
    }

    private void setSlidingUpPanelHeight(boolean halfHeight) {
        SlidingUpPanelLayout supl = ((SlidingUpPanelLayout) findViewById(R.id.sliding_layout));
        View panel=supl.getChildAt(1);
        ViewGroup.LayoutParams params = panel.getLayoutParams();
        Rect rectangle= new Rect();
        Window window= getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int height= rectangle.bottom - rectangle.top;
        params.height = halfHeight ? height / 2 : height;
        supl.requestLayout();
    }
}
