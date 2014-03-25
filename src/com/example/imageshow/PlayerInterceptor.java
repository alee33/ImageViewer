package com.example.imageshow;

import com.example.imageshow.TouchHandlerContainer.OnInterceptTouchEventListener;
import com.example.imageshow.adapters.CursorPagerAdapter;
import com.example.imageshow.settings.SettingsFragment;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

public class PlayerInterceptor implements OnInterceptTouchEventListener {

    private Activity context;
    private CursorPagerAdapter<?> mPagerAdapter;
    private boolean isPlayed = false;
    private int currentPage = 0;
    private static final int hideTimout = 2000;
    private Runnable mRunnable;
    private Handler handler;
    private ViewPager mPager;

    private Runnable hideBar;
    private Handler barHandler;

    public PlayerInterceptor(Activity context, ViewPager mPager) {

        this.mPagerAdapter = (CursorPagerAdapter<?>) mPager.getAdapter();
        this.context = context;
        this.mPager = mPager;

        initUIListener();

        initHideBarRunnable();
    }

    private void initHideBarRunnable() {
        hideBar = new Runnable() {
            public void run() {
                context.getActionBar().hide();
            }
        };

        barHandler = new Handler();
    }

    public void onCreateOptionsMenu(Menu menu) {
        menu.add(0, R.id.ID_PLAY, 0, R.string.play).setIcon(android.R.drawable.ic_media_play).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, R.id.ID_PAUSE, 0, R.string.pause).setIcon(android.R.drawable.ic_media_pause).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        LockHandler.instance().addItemVisualizator(menu); // create vizualization to menu
    }

    public Boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.ID_PAUSE:
            stop();
            return true;
        case R.id.ID_PLAY:
            play();
            return true;
        }
        return false;
    }

    public void onResume() {
        hideStatusAndNavigationBar();
    }

    private void initUIListener() {
        View decorView = context.getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    context.getActionBar().show();
                    if (isPlayed) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideStatusAndNavigationBar();
                            }
                        }, hideTimout);
                    }
                } else {
                    context.getActionBar().hide();

                }
            }
        });
    }

    public void play() {
        if (!isPlayed) {
            currentPage = mPagerAdapter.getLastPosition();
            handler = new Handler();
            final int DELAY = SettingsFragment.getDelay(context.getApplicationContext());
            mRunnable = new Runnable() {
                public void run() {
                    if (currentPage == mPagerAdapter.getCount()) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                    handler.postDelayed(mRunnable, DELAY);
                }
            };
            mRunnable.run();
            isPlayed = true;

        }
        LockHandler.instance().lock();
        context.getActionBar().hide();
        hideStatusAndNavigationBar();
    }

    private void stop() {
        if (handler != null) {
            handler.removeCallbacks(mRunnable);
            isPlayed = false;
        }

    }

    private void hideStatusAndNavigationBar() {

        if (Build.VERSION.SDK_INT < 16) { // ye olde method
            context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else { // Jellybean and up, new hotness
            View decorView = context.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

    }

    @Override
    public boolean onInterceptTouchEvent() { // catch touch view events
        return isPlayed && LockHandler.instance().isLocked();
    }

    @Override
    public boolean onMultyTap() { // catch double click to reset blocking
        LockHandler.instance().unlock();
        return false;
    }

    @Override
    public void onSingleTap() {
        if (!isPlayed || Build.VERSION.SDK_INT < 16) {
            if (context.getActionBar().isShowing()) {
                context.getActionBar().hide();
                barHandler.removeCallbacks(hideBar);
            } else {
                context.getActionBar().show();
                if (isPlayed) {
                    barHandler.postDelayed(hideBar, 2000);
                }
            }
        } else {

        }

    }
}
