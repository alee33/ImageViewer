package com.example.imageshow.db.orm;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import android.content.Context;

/**
 * Database manager
 * 
 * @author user
 * 
 */
public class DatabaseManager {
    private static volatile DatabaseManager instance;
    private OrmDatabaseHelper helper;

    private DatabaseManager() {
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        if (helper == null)
            helper = OpenHelperManager.getHelper(context, OrmDatabaseHelper.class);
    }

    public void release() {
        if (helper != null)
            OpenHelperManager.releaseHelper();
    }

    public OrmDatabaseHelper getHelper() {
        return helper;
    }
}
