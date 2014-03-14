package com.example.imageshow.adapters;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Adapter for images
 * @author user
 *
 * @param <ImageViewFragment>
 */
public class CursorPagerAdapter<ImageViewFragment extends Fragment> extends FragmentStatePagerAdapter {
    private final Class<ImageViewFragment> fragmentClass;
    private Cursor cursor;
    private int loaderId; //loader for current cursor
    private int lastPosition = 0;

    public static final String LOADER = "loader_id";

    public CursorPagerAdapter(FragmentManager fragmentManager, Class<ImageViewFragment> fragmentClass, Cursor cursor) {
        super(fragmentManager);
        this.fragmentClass = fragmentClass;

        this.cursor = cursor;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public int getLastPosition() {
        return lastPosition;
    }

    @Override
    public ImageViewFragment getItem(int position) {
        if (cursor == null) // shouldn't happen
            return null;
        lastPosition = position;

        cursor.moveToPosition(position);
        ImageViewFragment frag;
        try {
            frag = fragmentClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        Bundle args = new Bundle();

        for (int i = 0; i < cursor.getColumnCount(); ++i) {
            args.putString(cursor.getColumnName(i), cursor.getString(i));
        }
        args.putInt(LOADER, loaderId); //send loader id to fragment for source choosing

        frag.setArguments(args);
        return frag;
    }

    @Override
    public int getCount() {
        if (cursor == null)
            return 0;
        else
            return cursor.getCount();
    }

    public void swapCursor(Cursor c, Integer id) {
        if (cursor == c)
            return;

        this.cursor = c;
        this.loaderId = id;
        if (c != null) {
            notifyDataSetChanged();
        }
    }

    public Cursor getCursor() {
        return cursor;
    }
}