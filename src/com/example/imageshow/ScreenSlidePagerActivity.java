package com.example.imageshow;

import com.example.imageshow.TouchHandlerContainer.OnInterceptTouchEventListener;
import com.example.imageshow.adapters.CursorPagerAdapter;
import com.example.imageshow.db.SettingsDataSource;
import com.example.imageshow.recivers.FinishBroadcastReciver;
import com.example.imageshow.rest.AlbumsMapper.FotoMapper;
import com.example.imageshow.settings.ProfileItemFragment;
import com.example.imageshow.settings.SettingsActivity;
import com.example.imageshow.settings.SourceFragment;
import com.example.imageshow.transformation.Transformation;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.view.ViewPager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

public class ScreenSlidePagerActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnInterceptTouchEventListener {
    private ViewPager mPager;
    private CursorPagerAdapter mPagerAdapter;
    String[] projection = { MediaStore.Images.Thumbnails._ID };

    public static final int INNER_SOURCE = 0;
    public static final int OUTER_SOURCE = 1;
    public static final int VK_FOTO = 2;
    protected static final int REQUEST_ENABLE = 0;

    private final String TAG = getClass().getSimpleName();
    private static final String SOURCE = "source_id";
    private int sourceId = 0;
    private int currentPage = 0;
    private Runnable mRunnable;
    private Handler handler;
    private SettingsDataSource settingsDataSource;
    private BroadcastReceiver finishReciver;
    private boolean isPlayed = false;
    private boolean isTupped = false;
    private int uiVisibility = 0;

    public void restartLoader(int id) {
        getSupportLoaderManager().restartLoader(id, null, (LoaderManager.LoaderCallbacks<Cursor>) this);

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    private void play() {
        if (!isPlayed) {
            currentPage = mPagerAdapter.getLastPosition();
            handler = new Handler();
            final int DELAY = ProfileItemFragment.getDelay(this);
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
        isTupped = false;
        getActionBar().hide();
        hideStatusAndNavigationBar();
    }

    private void stop() {
        if (handler != null) {
            handler.removeCallbacks(mRunnable);
            isPlayed = false;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater li = LayoutInflater.from(this);
        TouchHandlerContainer myRoot = new TouchHandlerContainer(this, this);

        li.inflate(R.layout.main_, myRoot, true);
        setContentView(myRoot);

        settingsDataSource = new SettingsDataSource(getApplicationContext());
        settingsDataSource.open();

        mPagerAdapter = new CursorPagerAdapter(this.getSupportFragmentManager(), ImageViewFragment.class, (Cursor) null);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);

        sourceId = SourceFragment.getSourceId(getApplicationContext());

        getSupportLoaderManager().initLoader(SourceFragment.getSourceId(this), null, (LoaderCallbacks<Cursor>) this);

        // initButtons();
        finishReciver = new FinishBroadcastReciver(this);// создаем фильтр для BroadcastReceiver
        registerReceiver(finishReciver, new IntentFilter("example.imageshow.br001"));

        // this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);

        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                Log.d(TAG, "cahange visibility" + visibility);
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    getActionBar().show();
                    if (isPlayed) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideStatusAndNavigationBar();
                            }
                        }, 2000);
                    }
                } else {
                    getActionBar().hide();

                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Log.d(TAG, "source id =" + id);
        Uri uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
        switch (id) {
        case INNER_SOURCE:
            uri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
            break;
        case OUTER_SOURCE:
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            break;
        case VK_FOTO:
            String[] pr = { FotoMapper._ID, FotoMapper.ALBUM_ID, FotoMapper.PHOTO_URL };
            return new CursorLoader(getApplicationContext(), FotoMapper.CONTENT_URI, pr, null, null, FotoMapper._ID);
        }
        return new CursorLoader(getApplicationContext(), uri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPagerAdapter.swapCursor(data, loader.getId());

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPagerAdapter.swapCursor(null, -1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem playMenu = menu.add(0, R.id.ID_PLAY, 0, R.string.play); // акшн со статусами водил
        playMenu.setIcon(android.R.drawable.ic_media_play).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        playMenu.setOnMenuItemClickListener(new android.view.MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                play();
                return true;
            }

        });

        MenuItem pauseMenu = menu.add(0, R.id.ID_PAUSE, 0, R.string.pause); // акшн со статусами водил
        pauseMenu.setIcon(android.R.drawable.ic_media_pause).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        pauseMenu.setOnMenuItemClickListener(new android.view.MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                stop();
                return true;
            }

        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.settings:
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * @Override
     * protected void onSaveInstanceState(Bundle state) {
     * super.onSaveInstanceState(state);
     * state.putInt(SOURCE, sourceId);
     * }
     * 
     * @Override
     * protected void onRestoreInstanceState(Bundle savedInstanceState) {
     * super.onRestoreInstanceState(savedInstanceState);
     * sourceId = savedInstanceState.getInt(SOURCE);
     * System.out.println("restore");
     * if (sourceId != SourceFragment.getSourceId(getApplicationContext())) {
     * sourceId = SourceFragment.getSourceId(getApplicationContext());
     * restartLoader(sourceId);
     * }
     * }
     */

    @Override
    protected void onResume() {
        super.onResume();
        int id = SourceFragment.getSourceId(getApplicationContext());

        if (sourceId != id) {
            sourceId = id;
            restartLoader(id);
            mPager.setCurrentItem(0, true);
        }
        settingsDataSource.open();

        hideStatusAndNavigationBar();
        mPager.setPageTransformer(true, Transformation.getInstance().getTransformer(ProfileItemFragment.getTransformation(getApplicationContext()))); // set transformation on every resume activity if
                                                                                                                                                      // its changed
    }

    private void hideStatusAndNavigationBar() {

        if (Build.VERSION.SDK_INT < 16) { // ye olde method
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else { // Jellybean and up, new hotness
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

    }

    @Override
    public void onPause() {
        settingsDataSource.close();
        super.onPause();
    }

    /**
     * При выходе из приложения
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(finishReciver);
    }

    @Override
    public boolean onInterceptTouchEvent() { // catch touch view events
        if (isPlayed && !isTupped) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onMultyTap() { // catch double click to reset blocking
        Log.d(TAG, "catch multy click");
        isTupped = true;
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { // catch key pressed
        return onInterceptTouchEvent();

    }

    @Override
    public void onActionClick() {
        Log.d(TAG, "catch one click");
        if (!isPlayed || Build.VERSION.SDK_INT < 16) {
            if (getActionBar().isShowing()) {
                getActionBar().hide();
            } else {
                getActionBar().show();
                if (isPlayed) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActionBar().hide();
                        }
                    }, 2000);
                }
            }
        } else {

        }

    }

}
