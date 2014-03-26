package com.example.imageshow;

import android.view.Menu;
import android.view.MenuItem;

/**
 * Lock screen handler
 * @author user
 *
 */
public class LockHandler {
    private MenuItem item; //item show lock status
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

    /**
     * Add item visualizator to menu
     * @param menu
     */
    public void addItemVisualizator(Menu menu) {
        item=menu.add(0, R.id.ID_LOCK, 0, R.string.lock);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        changeState();
    }

    /**
     * Change current lock state
     */
    private void changeState() {
        if (item != null) {
            if (isLocked()) {
                item.setIcon(R.drawable.ic_lock);
            } else {
                item.setIcon(R.drawable.ic_unlock);
            }

        }
    }

    /**
     * Lock
     */
    public synchronized void lock() {
        isLocked = true;
        changeState();
    }

    /**
     * UnLock 
     */
    public synchronized void unlock() {
        isLocked = false;
        changeState();
    }

    /**
     * Check lock status
     * @return
     */
    public boolean isLocked() {
        return isLocked;
    }

}
