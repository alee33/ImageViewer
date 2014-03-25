package com.example.imageshow.picker;

import java.util.Collection;

public interface TreeItemImplementation {
     Collection<TreeItemImplementation > getInnerItems();  
     TreeItemImplementation getParentItem();  
     String getPath();
     int getImage();
     void click(TreeFragmentListener listener);
     void doubleClick(TreeFragmentListener listener); 
} 
