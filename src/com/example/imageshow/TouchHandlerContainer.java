package com.example.imageshow;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

/**
 * Container to intercept user touch event
 * 
 * @author user
 * 
 */
class TouchHandlerContainer extends RelativeLayout {

    private OnInterceptTouchEventListener listener = null;

    private long lastTouchTime = -1;
    private int touchCount = 1;
    private static final int TOUCH_COUNT = 2; // more then 2
    private GestureDetectorCompat mDetector;

    public TouchHandlerContainer(Context context) {
        super(context);
        mDetector = new GestureDetectorCompat(context, new OneClickListener());
    }

    public void setListener(OnInterceptTouchEventListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mDetector.onTouchEvent(ev);
        // System.out.println("listener "+ listener +" event "+ ev);
        if (listener != null) { // if listener set
            if (ev.getAction() == MotionEvent.ACTION_DOWN) { // look curr action? if it page down
                long thisTime = System.currentTimeMillis();// rememre curr time
                if (thisTime - lastTouchTime < ViewConfiguration.getDoubleTapTimeout()) { // compare with previous time
                    if (++touchCount > TOUCH_COUNT) { // if it quick click - increase counter and compare with minimum click count
                        listener.onMultyTap();
                        lastTouchTime = -1;
                        touchCount = 1;
                    } else {
                        lastTouchTime = thisTime;
                    }
                } else {
                    lastTouchTime = thisTime;
                    touchCount = 1;
                }
            }
            // previous=ev.getAction();
            return listener.onInterceptTouchEvent();
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * Touch event listener
     * 
     * @author user
     * 
     */
    public interface OnInterceptTouchEventListener {
        /**
         * Is need intercept current event
         * 
         * @return true- if current event intercept
         */
        boolean onInterceptTouchEvent();

        /**
         * Catch multy tap event
         * 
         * @return
         */
        boolean onMultyTap();

        /**
         * Catch single tap event
         */
        void onSingleTap();
    }

    class OneClickListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            listener.onSingleTap();
            return super.onSingleTapUp(e);
        }

    }
}
