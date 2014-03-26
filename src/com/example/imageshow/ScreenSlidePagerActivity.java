package com.example.imageshow;

import com.example.imageshow.SourceActivity.SourceType;
import com.example.imageshow.adapters.CursorPagerAdapter;
import com.example.imageshow.recivers.FinishBroadcastReciver;
import com.example.imageshow.rest.VkPhotoMapper.FotoMapper;
import com.example.imageshow.settings.SettingsActivity;
import com.example.imageshow.settings.SettingsFragment;
import com.example.imageshow.transformation.Transformation;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

/**
 * Base activity whin photo pager
 * @author user
 *
 */
public class ScreenSlidePagerActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private ViewPager mPager;
    private CursorPagerAdapter<ImageViewFragment> mPagerAdapter;
    
    String[] projection = { MediaStore.Images.Media._ID , MediaStore.Images.Media.DATA}; //photo projection
    String[] vk_projection = { FotoMapper._ID, FotoMapper.PHOTO_BITMAP, FotoMapper.PHOTO_URL }; //vk photo projection
  
    protected static final int REQUEST_ENABLE = 0;

    private static final String TAG = ScreenSlidePagerActivity.class.getSimpleName();
    private int sourceId = 0;
    private String sourcePath;
    private BroadcastReceiver finishReciver;
    private PlayerInterceptor player;

    /**
     * Reset photo loader 
     * @param id - loader id
     */
    public void restartLoader(int id) {
        getSupportLoaderManager().restartLoader(id, null, (LoaderManager.LoaderCallbacks<Cursor>) this);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater li = LayoutInflater.from(this);
        
       //create touch event container to perform all click events as main view
        TouchHandlerContainer touchHandler = new TouchHandlerContainer(this); 
        li.inflate(R.layout.main_, touchHandler, true);
        setContentView(touchHandler);

        mPagerAdapter = new CursorPagerAdapter<ImageViewFragment>(this.getSupportFragmentManager(), ImageViewFragment.class, (Cursor) null);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);

        //create player interceptor/ Player perform all touch events on main view
        player = new PlayerInterceptor(this, mPager);
        touchHandler.setListener(player); 

        sourceId = PreferenceManager.getDefaultSharedPreferences(this).getInt(SourceActivity.SOURCE_KEY,0);
        sourcePath= PreferenceManager.getDefaultSharedPreferences(this).getString(SourceActivity.SOURCE_PATH, "");
        
        getSupportLoaderManager().initLoader(sourceId, null, (LoaderCallbacks<Cursor>) this);

        finishReciver = new FinishBroadcastReciver(this);// создаем фильтр для BroadcastReceiver
        registerReceiver(finishReciver, new IntentFilter("example.imageshow.br001"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Log.d(TAG, "source id =" + id);
        
        switch (SourceType.type(id)) {
        case INNER:
            return new CursorLoader(getApplicationContext(), MediaStore.Images.Media.INTERNAL_CONTENT_URI, projection, MediaStore.Images.Media.DATA+" like ? ", new String[]{sourcePath+"%"}, null);
        case OUTER:
            return new CursorLoader(getApplicationContext(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, MediaStore.Images.Media.DATA+" like ? ", new String[]{sourcePath+"%"}, null);
        case VK:
            return new CursorLoader(getApplicationContext(), FotoMapper.CONTENT_URI, vk_projection, null, null, FotoMapper._ID);
        }
        return null;
    }
   
    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPagerAdapter.swapCursor(data, loader.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPagerAdapter.swapCursor(null, -1);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        player.onCreateOptionsMenu(menu);
        return true;

    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(player.onInterceptTouchEvent()){
            return true;
        }
        switch (item.getItemId()) {
        case R.id.settings:
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        case R.id.source:
            startActivity(new Intent(getApplicationContext(), SourceActivity.class));
            return true;
        }
       
        if (player.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        super.onResume();
        String path= PreferenceManager.getDefaultSharedPreferences(this).getString(SourceActivity.SOURCE_PATH, "");
        int id = PreferenceManager.getDefaultSharedPreferences(this).getInt(SourceActivity.SOURCE_KEY,0);

        if (sourceId != id || !path.equals(sourcePath)) {
            sourceId = id;
            sourcePath=path;
            restartLoader(id);
            mPager.setCurrentItem(0, true);
        }
        player.onResume();
        mPager.setPageTransformer(true, Transformation.getInstance().getTransformer(SettingsFragment.getTransformation(getApplicationContext()))); // set transformation on every resume activity if
                                                                                                                                                   // its changed
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(finishReciver);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { // catch key pressed
        return player.onInterceptTouchEvent();

    }

}
