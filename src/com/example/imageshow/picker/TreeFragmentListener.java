package com.example.imageshow.picker;

import com.example.imageshow.SourceActivity.SourceType;

public interface TreeFragmentListener {
    void onRefreshSubs(TreeItemImplementation current);
    boolean returnValue(TreeItemImplementation current, SourceType  type);
}
