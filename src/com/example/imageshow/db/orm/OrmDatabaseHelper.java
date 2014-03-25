package com.example.imageshow.db.orm;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class OrmDatabaseHelper extends OrmLiteSqliteOpenHelper {
    // name of the database file for your application
    private static final String DATABASE_NAME = "db_photos.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 4;

    private Dao<Source, Long> sourceDao = null;
    private Dao<Detail, Long> detailDao = null;
    private Dao<Settings, Long> settingsDao = null;
    
    public OrmDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
        try {
            Log.i(OrmDatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Source.class);
            TableUtils.createTable(connectionSource, Detail.class);
            TableUtils.createTable(connectionSource, Settings.class);
        } catch (SQLException e) {
            Log.e(OrmDatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource arg1, int arg2, int arg3) {
        try {
            Log.i(OrmDatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Source.class, true);
            TableUtils.dropTable(connectionSource, Detail.class, true);
            TableUtils.dropTable(connectionSource, Settings.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(OrmDatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }

    }

    public Dao<Source, Long> getViolationSourceDao() throws SQLException {
        if (sourceDao == null) {
            sourceDao = getDao(Source.class);
        }
        return sourceDao;
    }
    
    public Dao<Detail, Long> getViolationDeatilDao() throws SQLException {
        if (detailDao == null) {
            detailDao = getDao(Detail.class);
        }
        return detailDao;
    }
    
    public Dao<Settings, Long> getViolationSettingsDao() throws SQLException {
        if (settingsDao == null) {
            settingsDao = getDao(Settings.class);
        }
        return settingsDao;
    }
    @Override
    public void close() {
        super.close();
        sourceDao = null;
        detailDao=null;
    }

}
