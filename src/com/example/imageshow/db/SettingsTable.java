package com.example.imageshow.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Table for settings data
 * @author user
 *
 */
public class SettingsTable {
    public static final String TABLE_NAME = "settings";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_STARTUP = "start_time";
    public static final String COLUMN_SHUTDOWN = "stop_time";
    public static final String COLUMN_DELAY = "view_delay";
    public static final String COLUMN_AVTO_CHARGE = "avto_charge";
    public static final String COLUMN_AVTO_REBOOT = "avto_rebut";

    private static final String DATABASE_CREATE = "create table " + TABLE_NAME + " ( " + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_STARTUP + " integer, " + COLUMN_SHUTDOWN
            + " integer, " + COLUMN_DELAY + " integer, " + COLUMN_AVTO_CHARGE + " boolean, " + COLUMN_AVTO_REBOOT + " boolean, " + COLUMN_NAME + " text);";

    private static final String DATABASE_INIT = " insert into " + TABLE_NAME + " values (1,0,0,5,0,0,'default'); ";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        db.execSQL(DATABASE_INIT);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(SettingsTable.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}
