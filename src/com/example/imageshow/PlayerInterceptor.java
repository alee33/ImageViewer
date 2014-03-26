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

/**
 * Slide show player
 * 
 * @author user
 * 
 */
public class PlayerInterceptor implements OnInterceptTouchEventListener {

    private Activity context; // current activity
    private CursorPagerAdapter<?> mPagerAdapter; // adapter with items
    private boolean isPlayed = false;
    private int currentPage = 0; // current shown page
    private static final int hideTimout = 2000; // default slide delay timout

    private Runnable mRunnable;
    private Handler handler;
    private ViewPager mPager;

    private Runnable hideBar;
    private Handler barHandler;

    private MenuItem play,stop;
    
    public PlayerInterceptor(Activity context, ViewPager mPager) {

        this.mPagerAdapter = (CursorPagerAdapter<?>) mPager.getAdapter();
        this.context = context;
        this.mPager = mPager;

        initUIListener();

        initHideBarRunnable();
    }

    /**
     * Init hide action bar process
     */
    private void initHideBarRunnable() {
        hideBar = new Runnable() {
            public void run() {
                context.getActionBar().hide();
            }
        };

        barHandler = new Handler();
    }

    /**
     * Create menu with play and stop button
     * 
     * @param menu
     */
    public void onCreateOptionsMenu(Menu menu) {
        play= menu.add(0, R.id.ID_PLAY, 0, R.string.play).setIcon(android.R.drawable.ic_media_play);
        play.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        
        stop=menu.add(0, R.id.ID_PAUSE, 0, R.string.pause).setIcon(android.R.drawable.ic_media_pause);
        stop.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        stop.setEnabled(false);
        
        LockHandler.instance().addItemVisualizator(menu); // create lock vizualization to menu
    }

    /**
     * On item menu click handler
     * 
     * @param item
     * @return
     */
    public Boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.ID_PAUSE:
            stop(); // stop slide show
            return true;
        case R.id.ID_PLAY:
            play(); // start slide show
            return true;
        }
        return false;
    }

    /**
     * Need call on activity resume to hide navigation bar
     */
    public void onResume() {
        hideStatusAndNavigationBar();
    }

    /**
     * init UI listener to hide navigation and status bars
     */
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

    /**
     * Start slide show
     */
    public void play() {
        if (!isPlayed) {
            currentPage = mPagerAdapter.getLastPosition();
            handler = new Handler();
            final int delay = SettingsFragment.getDelay(context.getApplicationContext()); // get delay timeout
            mRunnable = new Runnable() {
                public void run() {
                    if (currentPage == mPagerAdapter.getCount()) { // if its last page, then go to first
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                    handler.postDelayed(mRunnable, delay);
                }
            };
            mRunnable.run();
            isPlayed = true;
            play.setEnabled(false);
            stop.setEnabled(true);
        }
        LockHandler.instance().lock(); // lock screen
        context.getActionBar().hide(); // hide action bar
        hideStatusAndNavigationBar(); // hide navigation bar
    }

    /**
     * Stop slide show
     */
    private void stop() {
        if (handler != null) {
            handler.removeCallbacks(mRunnable);
            isPlayed = false;
            play.setEnabled(true);
            stop.setEnabled(false);
        }

    }

    /**
     * Hide status and navigation bar if need
     */
    private void hideStatusAndNavigationBar() {

        if (Build.VERSION.SDK_INT < 16) { // ye olde method
            context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else { // Jellybean and up, new hotness
            View decorView = context.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

    }

    /**
     * If played and locked, then events not perfomed
     */
    @Override
    public boolean onInterceptTouchEvent() { // catch touch view events
        return isPlayed && LockHandler.instance().isLocked();
    }

    /**
     * Unlock on multytap click
     */
    @Override
    public boolean onMultyTap() { // catch double click to reset blocking
        LockHandler.instance().unlock();
        return false;
    }

    /**
     * Perform single tap
     */
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
