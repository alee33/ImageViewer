package com.example.imageshow.picker;

import com.example.imageshow.SourceActivity.SourceType;

/**
 * Listener items event
 * 
 * @author user
 * 
 */
public interface TreeFragmentListener {
    /**
     * Refresh current items list
     * 
     * @param current - current base item
     */
    void onRefreshSubs(TreeItemImplementation current);

    /**
     * Return source value
     * 
     * @param current -current item
     * @param type - source type
     * @return
     */
    boolean returnValue(TreeItemImplementation current, SourceType type);
}
