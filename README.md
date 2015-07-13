# android-transition

Android-Transition allows the easy creation of view transitions that reacts to user inputs. The library is designed to be general enough such that the same transition can be applied to
differnt UI components like [Drawer](https://developer.android.com/reference/android/support/v4/widget/DrawerLayout.html), [SlidingUpPanel](https://github.com/umano/AndroidSlidingUpPanel), [ViewPager](https://developer.android.com/reference/android/support/v4/view/ViewPager.html), [ObservableScrollView](https://github.com/ksoichiro/Android-ObservableScrollView) (work in progress), etc., as long as appropriate adapter is used.

## Changelog

**0.9.0-beta**

**Please note that due to change in scope and direction, the code has been overhauled in 0.9.0 and is incompatible with 0.8.x.** A few changes are planned that, while not as expansive as those made in 0.9.0-beta, may still break some APIs.

On the other hand now it only takes __8 lines of code__ to achieve the effect below that includes both non-interactive animation and interactive transition (drawer dragging):

![](/github/animation_transition.gif)

On Android Studio update Gradle dependency to:

    compile 'com.github.kaichunlin.transition:core:0.9.0-beta'

To add the corresponding slidinguppanel module:

    compile 'com.github.kaichunlin.transition:slidinguppanel:0.9.0-beta'

**0.8.3**

[Android Support Annotations](http://tools.android.com/tech-docs/support-annotations) are applied across the codebase which should help catching incorrect usage early. On Android Studio update Gradle dependency to:

    compile 'com.github.kaichunlin.transition:core:0.8.3'

Note that while many annotations such as @NonNull and @Nullable work on [SDK Build Tools](https://developer.android.com/tools/revisions/build-tools.html) 22.0.x, some annotations like @IntRange and @FlatRange only work when preview version (23.0.0 rc2) is used. 

--------
### Download from Google Play

[![Get it on Google Play](https://developer.android.com/images/brand/en_generic_rgb_wo_45.png)](https://play.google.com/store/apps/details?id=kaichunlin.transition.app)

The app on Google Play may not be the latest version.

Features
--------
+ **View and MenuItem Transition**

  view transition
  
  ![](/github/slideup_default.gif) 
  
  MenuItem transition

  ![](/github/menu_shrink_and_fade.gif) 

+ **Integrated Transition & Animation**

  See Changelog for 0.9.0-beta above for an exmaple, and the "Usage" section below for sample code.

+ **Interpolator**

  It's very easy to apply standard Android Interpolator to the transition:

  ([AccelerateDecelerateInterpolator](http://developer.android.com/reference/android/view/animation/AccelerateDecelerateInterpolator.html) v.s. [AnticipateInterpolator](http://developer.android.com/reference/android/view/animation/AnticipateInterpolator.html))

  ![](/github/slideup_default.gif)
  ![](/github/slideup_anticipate.gif)


+ **Custom Adapters**
  * [Drawer](https://developer.android.com/reference/android/support/v4/widget/DrawerLayout.html)
  * [SlidingUpPanel](https://github.com/umano/AndroidSlidingUpPanel)
  * [ViewPager](https://developer.android.com/reference/android/support/v4/view/ViewPager.html)
  * [ObservableScrollView](https://github.com/ksoichiro/Android-ObservableScrollView) (work in progress)
  * Write one yourself!

Integration
--------
The simplest way to integrate Android-Transition is to grab them from Maven Central or jCenter. On Android Studio, add the code below to Gradle dependencies:

    compile 'com.github.kaichunlin.transition:core:0.9.0-beta'

Adapters that adapts to UI components not found in Android framework or Android Support Library are provided as their own libraries, the table below is the list of libraries:

| Library       | Function           | Description in build.gradle  |
|:-------------|:-------------|:-----|
| core | Provides core transition function and adapters (beta version) | com.github.kaichunlin.transition:core:0.9.0-beta |
| slidinguppanel | [AndroidSlidingUpPanel](https://github.com/umano/AndroidSlidingUpPanel) Adapter (beta version) | com.github.kaichunlin.transition:slidinguppanel:0.9.0-beta|
| core | Provides core transition function and adapters (stable version) | com.github.kaichunlin.transition:core:0.8.3 |
| slidinguppanel | [AndroidSlidingUpPanel](https://github.com/umano/AndroidSlidingUpPanel) Adapter (stable version) | com.github.kaichunlin.transition:slidinguppanel:0.8.3|

As an example, if an app requires both the core & slidinguppanel libraries, then build.gradle will look like below:

     dependencies {
       //other dependencies
       ...

       compile 'com.github.kaichunlin.transition:core:0.9.0-beta'
       compile 'com.github.kaichunlin.transition:slidinguppanel:0.9.0-beta'
     }

Usage
--------
The app/ folder is a sample app containing dozens of examples.


#### Two steps to apply transition to any View ([sample code](https://github.com/kaichunlin/android-transition/blob/master/app/src/main/java/com/kaichunlin/transition/app/DrawerViewActivity.java)):

  1. Use the appropriate adapter:
  
  ```java
  DrawerListenerAdapter mDrawerListenerAdapter = new DrawerListenerAdapter(mDrawerToggle, R.id.drawerList);
  mDrawerListenerAdapter.setDrawerLayout(mDrawerLayout);
  ```
  2. Add desired transition to the adapter, [ViewTransitionBuilder](https://github.com/kaichunlin/android-transition/blob/master/transition/src/main/java/com/kaichunlin/transition/ViewTransitionBuilder.java) is used to build the transition:
  
  ```java
  mDrawerListenerAdapter.addTransition(
  ViewTransitionBuilder.transit(findViewById(R.id.big_icon)).rotation(0f, 360f).scaleX(1f, 0.2f).scaleY(1f, 0f).translationX(200f));
  ```

---
  
#### Three steps to apply transition to a MenuItem ([sample code](https://github.com/kaichunlin/android-transition/blob/master/app/src/main/java/com/kaichunlin/transition/app/DrawerMenuItemActivity.java)):

1. Use the appropriate adapter that extends [MenuBaseAdapter](https://github.com/kaichunlin/android-transition/blob/master/transition/src/main/java/com/kaichunlin/transition/adapter/MenuBaseAdapter.java):

  ```java
    DrawerListenerAdapter mDrawerListenerAdapter = new DrawerListenerAdapter(mDrawerToggle, R.id.drawerList);
    mDrawerListenerAdapter.setDrawerLayout(mDrawerLayout);
  ```
2. Let the adapter manage the creation of options menu:

  ```java
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mDrawerListenerAdapter.onCreateOptionsMenu(this, menu);

        return super.onCreateOptionsMenu(menu);
    }
  ```
3. Add desired transition to the adapter, [MenuItemTransitionBuilder](https://github.com/kaichunlin/android-transition/blob/master/transition/src/main/java/com/kaichunlin/transition/MenuItemTransitionBuilder.java) is used to build the transition:

  ```java
    //shared configuration
    MenuItemTransitionBuilder builder = MenuItemTransitionBuilder.transit(toolbar).alpha(1f, 0.5f).scale(1f, 0f).cascade(0.3f).visibleOnStartAnimation(true).invalidateOptionOnStopTransition(this, true);
    // create a transition to be used when the drawer transits from the closed state to the opened state 
    // notice that in most situations clone() should be used, i.e. builder.clone(), to prevent builder picking up effects that should only apply to a single transition 
    MenuItemTransition mShrinkClose = builder.translationX(0, 30).build();
    // create a reverse transition to be used when the drawer transits from the opened state to the closed state 
    MenuItemTransition mShrinkOpen = builder.reverse().translationX(0, 30).build();
    //tells adapter the transition and the menu options for both the opened and closed states
    mDrawerListenerAdapter.setupOptions(this, new MenuOptionConfiguration(mShrinkOpen, R.menu.drawer), new MenuOptionConfiguration(mShrinkClose, R.menu.main));
  ```

---

#### To apply both animation & transition:
  
  This can be achieved with the code:
    
  ```java

        //Create an adapter that listens for ActionBarDrawerToggle state change and update transition states
        DrawerListenerAdapter mDrawerListenerAdapter = new DrawerListenerAdapter(mDrawerToggle, R.id.drawerList);
        mDrawerListenerAdapter.setDrawerLayout(mDrawerLayout);

        //this builder is used to build both transition & animation effect
        ViewTransitionBuilder mRotateEffectBuilder = ViewTransitionBuilder.transit(findViewById(R.id.big_icon)).rotation(0f, 360f).scaleX(1f, 0.2f).scaleY(1f, 0f).translationX(200f);
        //build the desired transition and adds to the adapter
        ViewTransition transition = mRotateEffectBuilder.build();
        mDrawerListenerAdapter.addTransition(transition);

        //since the start animation is the reverse of the transition, set the current view state to transition's final state
        transition.setProgress(1f);
        //init an animation and add a delay to prevent stutter, needs to be higher if animation is enabled
        final IAnimation animation = mRotateEffectBuilder.reverse().buildAnimation();
        animation.startAnimationDelayed(600, 32);
  ```
See source of [DrawerViewActivity.java](https://github.com/kaichunlin/android-transition/blob/827c6f0aa7782400c542c365ae9b9c0f1e172d9c/app/src/main/java/com/kaichunlin/transition/app/DrawerViewActivity.java#L50-L66) for example.

---

#### Misc
+ To clear all transitions from an adapter:

  ```java
    mDrawerListenerAdapter.clearTransition();
  ```

+ Share a common builder ([sample code](https://github.com/kaichunlin/android-transition/blob/master/app/src/main/java/com/kaichunlin/transition/app/SlidingUpPanelActivity.java)):

  ```java
  //calling adapter(mSlidingUpPanelLayoutAdapter) means that when builder.build() is called, the resultant transition will automatically be added to mSlidingUpPanelLayoutAdapter
  ViewTransitionBuilder baseBuilder = ViewTransitionBuilder.transit().interpolator(mInterpolator).adapter(mSlidingUpPanelLayoutAdapter).rotationX(42f).scale(0.8f).translationYAsFractionOfHeight(-0.5f);
  
  //calls clone() so any modification will not be propagated to other transitions build from the same builder
  //adds a transition to view R.id.content_bg
  baseBuilder.clone().target(findViewById(R.id.content_bg)).build();
  
  //apply the same transition effect to a different view (R.id.content)
  baseBuilder.clone().target(findViewById(R.id.content)).build();
  ```

+ Delay transition evaluation until layout is complete, this is required if a view's position/dimension is used in the evaluation, in such a case [ViewUtil](https://github.com/kaichunlin/android-transition/blob/master/transition/src/main/java/com/kaichunlin/transition/util/ViewUtil.java) provides a helper function ([sample code](https://github.com/kaichunlin/android-transition/blob/master/app/src/main/java/com/kaichunlin/transition/app/SlidingUpPanelActivity.java)):

  ```java
    ViewUtil.executeOnGlobalLayout(findViewById(R.id.rotate_slide), new ViewTreeObserver.OnGlobalLayoutListener() {
      public void onGlobalLayout() {
        //create ViewTransitionBuilder here
      }
    });
  ```
  
