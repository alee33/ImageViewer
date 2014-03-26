package com.example.imageshow.picker;

import java.util.Collection;

/**
 * Source tree item
 * 
 * @author user
 * 
 */
public interface TreeItemImplementation {
    /**
     * Get inner items
     * 
     * @return collection items if exists
     */
    Collection<TreeItemImplementation> getInnerItems();

    /**
     * Get parent item
     * 
     * @return TreeItemImplementation parent item
     */
    TreeItemImplementation getParentItem();

    /**
     * Get source custom location
     * 
     * @return location string
     */
    String getPath();

    /**
     * Get item image resource
     * 
     * @return resource id
     */
    int getImage();

    /**
     * Perform item click
     * 
     * @param listener -click handler
     */
    void click(TreeFragmentListener listener);

    /**
     * Perform double click
     * 
     * @param listener -double click listener
     */
    void doubleClick(TreeFragmentListener listener);
}
