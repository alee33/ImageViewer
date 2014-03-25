package com.example.imageshow;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/**
 * Overide viewpager for smooth scrolling
 * @author user
 *
 */
public class ViewPager extends android.support.v4.view.ViewPager {
    //private final String TAG = getClass().getSimpleName();
    private static final int SPEED = 1000; // scroll speed ms

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMyScroller();
    }

    private void setMyScroller() {
        try {
            Class<?> viewpager = android.support.v4.view.ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext())); // smooth scroller
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyScroller extends Scroller {
        public MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, SPEED /* 1 secs */);
        }
    }
}
