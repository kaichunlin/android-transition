# android-transition
=================
Android-Transition allows the easy creation of view transitions that reacts to user inputs. The library is designed to be general enough such that the same transition can be applied to
differnt UI components like [Drawer](https://developer.android.com/reference/android/support/v4/widget/DrawerLayout.html), [SlidingUpPanel](https://github.com/umano/AndroidSlidingUpPanel), [ViewPager](https://developer.android.com/reference/android/support/v4/view/ViewPager.html), [ObeservableScrollView](https://github.com/ksoichiro/Android-ObservableScrollView) (work in progress), etc., as long as appropriate adapter is used.

Features
--------
+ **View and MenuItem Transition**

  ![](/github/slideup_default.gif) view transition

  ![](/github/menu_shrink_and_fade.gif) MenuItem transition


+ **Interpolator**

  It's very easy to apply standard Android Interpolator to the transition:

  ([AccelerateDecelerateInterpolator](http://developer.android.com/reference/android/view/animation/AccelerateDecelerateInterpolator.html) v.s. [AnticipateInterpolator](http://developer.android.com/reference/android/view/animation/AnticipateInterpolator.html))

  ![](/github/slideup_default.gif)
  ![](/github/slideup_anticipate.gif)


+ **Custom Adapters**
  * [Drawer](https://developer.android.com/reference/android/support/v4/widget/DrawerLayout.html)
  * [SlidingUpPanel](https://github.com/umano/AndroidSlidingUpPanel)
  * [ViewPager](https://developer.android.com/reference/android/support/v4/view/ViewPager.html)
  * [ObeservableScrollView](https://github.com/ksoichiro/Android-ObservableScrollView) (work in progress)
  * Write one yourself!

Usage
--------
The app/ folder is a sample app containing dozens of examples.


#### Two steps are required to apply transition to any View ([sample code](https://github.com/kaichunlin/android-transition/blob/master/app/src/main/java/com/kaichunlin/transition/app/DrawerViewActivity.java)):

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
  
#### Three steps are required to apply transition to a MenuItem ([sample code](https://github.com/kaichunlin/android-transition/blob/master/app/src/main/java/com/kaichunlin/transition/app/DrawerMenuItemActivity.java)):

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
  
