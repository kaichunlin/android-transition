# android-transition
=================
Android-Transition allows the easy creation of view transitions that reacts to user inputs. The library is designed to be general enough such that the same transition can be applied to
differnt UI components like [Drawer](https://developer.android.com/reference/android/support/v4/widget/DrawerLayout.html), [SlidingUpPanel](https://github.com/umano/AndroidSlidingUpPanel), [ViewPager](https://developer.android.com/reference/android/support/v4/view/ViewPager.html), [ObeservableScrollView](https://github.com/ksoichiro/Android-ObservableScrollView) (work in progress), etc., as long as appropriate adapter is used.

![](/github/slideup_default.gif)

Features
--------
**View and MenuItem Transition**

![](/github/slideup_default.gif) view transition

![](/github/menu_shrink_and_fade.gif) MenuItem transition


**Interpolator**

It's very easy to apply standard Android Interpolator to the transition:

([AccelerateDecelerateInterpolator](http://developer.android.com/reference/android/view/animation/AccelerateDecelerateInterpolator.html) v.s. [AnticipateInterpolator](http://developer.android.com/reference/android/view/animation/AnticipateInterpolator.html))

![](/github/slideup_default.gif)
![](/github/slideup_anticipate.gif)


**Custom Adapters**
* [Drawer](https://developer.android.com/reference/android/support/v4/widget/DrawerLayout.html)
* [SlidingUpPanel](https://github.com/umano/AndroidSlidingUpPanel)
* [ViewPager](https://developer.android.com/reference/android/support/v4/view/ViewPager.html)
* [ObeservableScrollView](https://github.com/ksoichiro/Android-ObservableScrollView) (work in progress)
* Write one yourself!
