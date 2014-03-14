package com.example.imageshow.db;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SettingsDataSource {

    private SQLiteDatabase database;
    private DbHelper dbHelper;

    public SettingsDataSource(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * Create object Settings from cursor
     * @param cursor
     * @return
     */
    private Settings cursorToSettings(Cursor cursor) {
        Settings settings = new Settings();
        settings.setAvtoChargeStart(cursor.getInt(4) == 1 ? true : false);
        settings.setAvtoResetStart(cursor.getInt(5) == 1 ? true : false);
        settings.setDelay(cursor.getInt(3));
        settings.setId(cursor.getLong(0));
        settings.setShutdownDelay(new Time(cursor.getInt(2)));
        settings.setStartupDelay(new Time(cursor.getInt(1)));
        settings.setName(cursor.getString(6));
        return settings;
    }


    /**
     * Get settings by UID
     * @param id
     * @return
     */
    public Settings getSettings(long id) {
        Cursor cursor = database.query(SettingsTable.TABLE_NAME, null, SettingsTable.COLUMN_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        cursor.moveToFirst();
        Settings settings = null;
        if (!cursor.isAfterLast()) {
            settings = cursorToSettings(cursor);
        }
        cursor.close();
        return settings;
    }

    /**
     * Get all settings from DB
     * @return
     */
    public List<Settings> getSettingsList() {
        List<Settings> settings = new ArrayList<Settings>();
        Cursor cursor = database.query(SettingsTable.TABLE_NAME, null, null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Settings filter = cursorToSettings(cursor);
            settings.add(filter);
            cursor.moveToNext();
        }
        cursor.close();
        return settings;
    }

    /**
     * Insert settings into DB
     * @param s
     */
    public void insertSettings(Settings s) {
        ContentValues values = new ContentValues();
        values.put(SettingsTable.COLUMN_AVTO_CHARGE, s.getAvtoChargeStart());
        values.put(SettingsTable.COLUMN_AVTO_REBOOT, s.getAvtoResetStart());
        values.put(SettingsTable.COLUMN_DELAY, s.getDelay());
        values.put(SettingsTable.COLUMN_SHUTDOWN, s.getShutdownDelay().getTime());
        values.put(SettingsTable.COLUMN_STARTUP, s.getStartupDelay().getTime());
        values.put(SettingsTable.COLUMN_NAME, s.getName());
        long insertId = database.insert(SettingsTable.TABLE_NAME, null, values);
    }
}
