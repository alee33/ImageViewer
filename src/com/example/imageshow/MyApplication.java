package com.example.imageshow;

import com.example.imageshow.db.orm.DatabaseManager;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseManager.getInstance().init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DatabaseManager.getInstance().release();
    }

}