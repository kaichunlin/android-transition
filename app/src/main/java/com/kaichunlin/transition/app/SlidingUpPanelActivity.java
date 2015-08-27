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

import com.kaichunlin.transition.ViewTransitionBuilder;
import com.kaichunlin.transition.adapter.SlidingUpPanelLayoutAdapter;
import com.kaichunlin.transition.adapter.UnifiedAdapter;
import com.kaichunlin.transition.animation.Animation;
import com.kaichunlin.transition.animation.AnimationListener;
import com.kaichunlin.transition.ScaledTransitionHandler;
import com.kaichunlin.transition.internal.TransitionController;
import com.kaichunlin.transition.internal.debug.TraceAnimationListener;
import com.kaichunlin.transition.internal.debug.TraceTransitionListener;
import com.kaichunlin.transition.util.TransitionUtil;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import kaichunlin.transition.app.R;


public class SlidingUpPanelActivity extends AppCompatActivity implements View.OnClickListener {

    private UnifiedAdapter mUnifiedAdapter;
    private Toolbar mToolbar;
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private View mLastSelection;
    private Menu mMenu;

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
        SlidingUpPanelLayoutAdapter mSlidingUpPanelLayoutAdapter = new SlidingUpPanelLayoutAdapter();
        supl.setPanelSlideListener(mSlidingUpPanelLayoutAdapter);
        //the adapter accepts another SlidingUpPanelLayout.PanelSlideListener so other customizations can be performed
        mSlidingUpPanelLayoutAdapter.setPanelSlideListener(new DialogPanelSlideListener(this));

        mUnifiedAdapter = new UnifiedAdapter(mSlidingUpPanelLayoutAdapter);
        mUnifiedAdapter.setDuration(1000);
        mUnifiedAdapter.addAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animationManager) {
            }

            @Override
            public void onAnimationEnd(Animation animationManager) {
                mUnifiedAdapter.setReverseAnimation(!mUnifiedAdapter.isReverseAnimation());
            }

            @Override
            public void onAnimationCancel(Animation animationManager) {
                mUnifiedAdapter.setReverseAnimation(!mUnifiedAdapter.isReverseAnimation());
            }

            @Override
            public void onAnimationReset(Animation animationManager) {

            }
        });

        //debug
        mUnifiedAdapter.addTransitionListener(new TraceTransitionListener());
        mUnifiedAdapter.addAnimationListener(new TraceAnimationListener());

        //this is required since some transition requires the width/height/position of a view, which is not yet properly initialized until layout is complete
        //in this example, another way of achieving correct behavior without using ViewUtil.executeOnGlobalLayout() would be to change all
        // translationYAsFractionOfHeight() calls to delayTranslationYAsFractionOfHeight() which would defer the calculation until the transition is just about to start
        TransitionUtil.executeOnGlobalLayout(this, new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                updateTransition(findViewById(R.id.rotate_slide), false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sliding_up_panel, menu);

        mMenu = menu;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        super.onOptionsItemSelected(menu);

        boolean changeInterpolator = true;
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
            case R.id.menu_animate:
                changeInterpolator = false;
                mUnifiedAdapter.startAnimation(1000);
                break;
        }
        if (changeInterpolator) {
            updateTransition(mLastSelection, false);
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        updateTransition(v, mUnifiedAdapter.isReverseAnimation() || mUnifiedAdapter.isAnimating());
    }

    public void updateTransition(View v, boolean animate) {
        if (animate) {
            mUnifiedAdapter.setReverseAnimation(true);
            mUnifiedAdapter.startAnimation();
        }

        //TODO removeAllTransitions() has to be called *after* mAnimationAdapter.resetAnimation(), this subtle order of execution requirement should be removed
        mUnifiedAdapter.removeAllTransitions();
        mLastSelection = v;

        boolean enableAnimationMenu = true;

        //configure a builder with properties shared by all instances and just clone it for future use
        //since adapter(ITransitionAdapter) is set, simply call buildFor(mUnifiedAdapter) would add the resultant ViewTransition to the adapter
        ViewTransitionBuilder baseBuilder = ViewTransitionBuilder.transit(mToolbar).interpolator(mInterpolator);
        ViewTransitionBuilder builder;
        ((ImageView) findViewById(R.id.content_bg)).setColorFilter(null);
        boolean setHalfHeight = false;
        switch (v.getId()) {
            //TODO visual artifact on Android 5.1 (Nexus 7 2013) when rotationX is ~45, why???
            case R.id.rotate_slide:
                builder = baseBuilder.clone();
                builder.scale(0.8f).rotationX(40).translationYAsFractionOfHeight(-1f).buildFor(mUnifiedAdapter);

                builder = baseBuilder.clone().target(findViewById(R.id.content_bg)).rotationX(42f).scale(0.8f).translationYAsFractionOfHeight(-0.5f);
                builder.buildFor(mUnifiedAdapter);
                builder.target(findViewById(R.id.content)).buildFor(mUnifiedAdapter);

                builder = baseBuilder.clone().target(findViewById(R.id.content));
                ViewTransitionBuilder.Cascade cascade = new ViewTransitionBuilder.Cascade(0.6f);
                cascade.reverse = true;
                builder.transitViewGroup(new ViewTransitionBuilder.ViewGroupTransition() {
                    @Override
                    public void transit(ViewTransitionBuilder builder, ViewTransitionBuilder.ViewGroupTransitionConfig config) {
                        builder.translationYAsFractionOfHeight(config.parentViewGroup, 1f).buildFor(mUnifiedAdapter);
                    }
                }, cascade);

                enableAnimationMenu = false;
                break;
            case R.id.sliding_actionbar_view:
                baseBuilder.clone().translationYAsFractionOfHeight(-1f).buildFor(mUnifiedAdapter);
                builder = baseBuilder.clone().target(findViewById(R.id.content)).translationYAsFractionOfHeight(-0.5f);
                builder.buildFor(mUnifiedAdapter);
                //apply the exact same transition to another view
                builder.target(findViewById(R.id.content_bg2)).buildFor(mUnifiedAdapter);
                break;
            case R.id.change_actionbar_color:
                baseBuilder.clone().backgroundColorResource(getResources(), R.color.primary, R.color.accent).buildFor(mUnifiedAdapter);
                break;
            case R.id.change_actionbar_color_hsv:
                baseBuilder.clone().backgroundColorResourceHSV(getResources(), R.color.primary, R.color.drawer_opened).buildFor(mUnifiedAdapter);
                break;
            case R.id.fading_actionbar:
                baseBuilder.clone().alpha(1f, 0f).buildFor(mUnifiedAdapter);
                break;
            case R.id.rotating_actionbar:
                baseBuilder.clone().delayTranslationYAsFractionOfHeight(-0.5f).delayRotationX(90f).scale(0.8f).buildFor(mUnifiedAdapter);
                break;
            case R.id.grayscale_bg:
                //Uses a CustomTransitionController that applies a ColorMatrixColorFilter to the background view
                baseBuilder.clone().addTransitionHandler(new ScaledTransitionHandler() {
                    ColorMatrix matrix = new ColorMatrix();

                    @Override
                    public void onUpdateScaledProgress(TransitionController controller, View target, float progress) {
                        matrix.setSaturation(1 - progress);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        ((ImageView) findViewById(R.id.content_bg)).setColorFilter(filter);
                    }
                }).range(0f, 1f).buildFor(mUnifiedAdapter);
                setHalfHeight = true;
                break;
        }

        //only show animate MenuItem when it's not the first option
        if (mMenu != null) {
            mMenu.findItem(R.id.menu_animate).setEnabled(enableAnimationMenu);
        }

        setSlidingUpPanelHeight(setHalfHeight);
    }

    private void setSlidingUpPanelHeight(boolean halfHeight) {
        SlidingUpPanelLayout supl = ((SlidingUpPanelLayout) findViewById(R.id.sliding_layout));
        View panel = supl.getChildAt(1);
        ViewGroup.LayoutParams params = panel.getLayoutParams();
        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int height = rectangle.bottom - rectangle.top;
        params.height = halfHeight ? height / 2 : height;
        supl.requestLayout();
    }
}
