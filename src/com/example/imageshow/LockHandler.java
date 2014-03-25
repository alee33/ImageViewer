package com.example.imageshow;

import android.view.Menu;
import android.view.MenuItem;

public class LockHandler {
    private MenuItem item;
    private static LockHandler sInstance;
    private boolean isLocked = false;

    private LockHandler() {
    }

    public static LockHandler instance() {
        if (sInstance == null) {
            sInstance = new LockHandler();
        }
        return sInstance;
    }

    public void addItemVisualizator(Menu menu) {
        item=menu.add(0, R.id.ID_LOCK, 0, R.string.lock);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        changeState();
    }

    private void changeState() {
        if (item != null) {
            if (isLocked()) {
                item.setIcon(R.drawable.ic_lock);
            } else {
                item.setIcon(R.drawable.ic_unlock);
            }

        }
    }

    public synchronized void lock() {
        isLocked = true;
        changeState();
    }

    public synchronized void unlock() {
        isLocked = false;
        changeState();
    }

    public boolean isLocked() {
        return isLocked;
    }

}
