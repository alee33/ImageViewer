package com.example.imageshow.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * My database helper
 * @author user
 *
 */
public class DbHelper extends SQLiteOpenHelper {
    private static DbHelper instance;
    private static final int DB_VERSION = 3; // версия

    public DbHelper(Context context) {
        super(context, "myDImageViewer", null, DB_VERSION);
    }

    public static DbHelper getHelper(Context context) {
        if (instance == null) {
            instance = new DbHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SettingsTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SettingsTable.onUpgrade(db, oldVersion, newVersion);

    }
}
