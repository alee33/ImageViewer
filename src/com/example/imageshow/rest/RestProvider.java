package com.example.imageshow.rest;

import com.example.imageshow.rest.AlbumsMapper.FotoMapper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Data provider for foto rest service
 * @author user
 *
 */
public class RestProvider extends ContentProvider {

    final String TAG = getClass().getSimpleName();
    private static final String TABLE_FOTOS = "fotos";
    private static final String DB_NAME = TABLE_FOTOS + ".db";
    private static final int DB_VERSION = 1;

    private static UriMatcher sUriMatcher;

    private static final int PATH_ROOT = 0;
    private static final int PATH_FOTOS = 1;

    static {
        sUriMatcher = new UriMatcher(PATH_ROOT);
        sUriMatcher.addURI(AlbumsMapper.AUTHORITY, AlbumsMapper.FotoMapper.CONTENT_PATH, PATH_FOTOS);
    }

    private DatabaseHeloper mDatabaseHelper;

    class DatabaseHeloper extends SQLiteOpenHelper {

        public DatabaseHeloper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "create table " + TABLE_FOTOS + " (" + FotoMapper._ID + " integer primary key autoincrement, " + FotoMapper.ALBUM_ID + " text, " + FotoMapper.PHOTO_URL + " text " + ")";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
        case PATH_FOTOS:
            return mDatabaseHelper.getWritableDatabase().delete(TABLE_FOTOS, selection, selectionArgs);
        default:
            return 0;
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
        case PATH_FOTOS:
            return FotoMapper.CONTENT_TYPE;
        default:
            return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)) {
        case PATH_FOTOS: {
            mDatabaseHelper.getWritableDatabase().insert(TABLE_FOTOS, null, values);
            getContext().getContentResolver().notifyChange(FotoMapper.CONTENT_URI, null);
        }
        default:
            return null;
        }
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHeloper(getContext(), DB_NAME, null, DB_VERSION);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (sUriMatcher.match(uri)) {
        case PATH_FOTOS: {
            Cursor cursor = mDatabaseHelper.getReadableDatabase().query(TABLE_FOTOS, projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), FotoMapper.CONTENT_URI);
            return cursor;
        }
        default:
            return null;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
        case PATH_FOTOS:
            return mDatabaseHelper.getWritableDatabase().update(TABLE_FOTOS, values, selection, selectionArgs);
        default:
            return 0;
        }
    }

}
