package com.example.imageshow;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

/**
 * Container to intercept user input
 * @author user
 *
 */
class TouchHandlerContainer extends RelativeLayout {

    private OnInterceptTouchEventListener listener = null;
    
    private long lastTouchTime = -1; 
    private int touchCount = 1;
    private static final int TOUCH_COUNT = 2; // more then 2
    private int previous;
    public TouchHandlerContainer(Context context, OnInterceptTouchEventListener listener) {
        super(context);
        this.listener = listener;

    }

    public TouchHandlerContainer(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (listener != null) { //if listener set
            if (ev.getAction() == MotionEvent.ACTION_DOWN) { //look curr action? if it page down
                long thisTime = System.currentTimeMillis();// rememre curr time
                if (thisTime - lastTouchTime < ViewConfiguration.getDoubleTapTimeout()) { //compare with previous time
                    if (++touchCount > TOUCH_COUNT) { //if it quick click - increase counter and compare with minimum click count
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
            //if ve havnt fragment(no images) then we get only ACTION_DOWN, no ACTION_UP rize
            if((ev.getAction() == MotionEvent.ACTION_UP||ev.getAction()  == MotionEvent.ACTION_DOWN)&& previous == MotionEvent.ACTION_DOWN && touchCount==1){ //this not move action or multiclick action
                listener.onActionClick();
            }
            previous=ev.getAction();
            return listener.onInterceptTouchEvent();
        }
        return super.onInterceptTouchEvent(ev);
    }

    public interface OnInterceptTouchEventListener {
        boolean onInterceptTouchEvent();

        boolean onMultyTap();
        
        void onActionClick();
    }
}
