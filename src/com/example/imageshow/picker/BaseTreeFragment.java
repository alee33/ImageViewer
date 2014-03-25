package com.example.imageshow.picker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.example.imageshow.R;
import com.example.imageshow.SourceActivity.SourceType;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class BaseTreeFragment extends Fragment implements TreeFragmentListener, OnKeyListener {
    private static final String TAG = BaseTreeFragment.class.getSimpleName();
    private TreeItemImplementation selectedItem;
    private ArrayAdapter<TreeItemImplementation> listItemsAdapter;
    private ArrayList<TreeItemImplementation> listItems;
    private GridView itemsView;
    private TreeItemImplementation[] root;

    private SetSourceListener listenerSource;
    final TreeFragmentListener listenerTree = this;

    public BaseTreeFragment() {
    }

    public static BaseTreeFragment newInstance(SetSourceListener listener, TreeItemImplementation... root) {
        BaseTreeFragment fragment = new BaseTreeFragment();
        fragment.listenerSource = listener;
        fragment.root = root;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.picker_layout, container, false);
        setHasOptionsMenu(true);

        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        itemsView = (GridView) view.findViewById(R.id.directoryList);

        final GestureDetectorCompat gestureDectector = new GestureDetectorCompat(this.getActivity(), new GestureListener());
        itemsView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDectector.onTouchEvent(event);
                return true;
            }
        });

        listItems = new ArrayList<TreeItemImplementation>();
        listItemsAdapter = new ImageAdapterTree(getActivity(), R.layout.picker_item, listItems);
        itemsView.setAdapter(listItemsAdapter);

        listItems.addAll(Arrays.asList(root));

        return view;
    }

    /*
     * @Override
     * public void onCreateOptionsMenu(android.view.Menu menu, android.view.MenuInflater inflater) {
     * super.onCreateOptionsMenu(menu, inflater);
     * MenuItem add = menu.add("UP");
     * add.setIcon(R.drawable.navigation_up);
     * add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
     * add.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
     * 
     * @Override
     * public boolean onMenuItemClick(MenuItem item) {
     * back();
     * return false;
     * }
     * 
     * }
     * 
     * );
     * }
     */
    private boolean back() {
        if (selectedItem != null) {
            debug("Press Back. selected item %s", selectedItem.getPath());
            listItems.clear();
            selectedItem = selectedItem.getParentItem();
            if (selectedItem != null) {
                listItems.addAll(selectedItem.getInnerItems());
            } else {
                listItems.addAll(Arrays.asList(root));
            }
            listItemsAdapter.notifyDataSetChanged();
            return false;
        }
        return true;
    }

    private void debug(String message, Object... args) {
        Log.d(TAG, String.format(message, args));
    }

    @Override
    public void onRefreshSubs(TreeItemImplementation current) {

        Collection<TreeItemImplementation> c = current.getInnerItems();
        if (c != null) {
            listItems.clear();
            listItems.addAll(c);
            listItemsAdapter.notifyDataSetChanged();
        }

    }

    public interface SetSourceListener {
        String setPath(SourceType type, String path);
    }

    @Override
    public boolean returnValue(TreeItemImplementation current, SourceType type) {
        // TODO Auto-generated method stub
        debug("return value %s", current.getPath());
        listenerSource.setPath(type, current.getPath());
        return true;
    }

    public class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            int position = itemsView.pointToPosition((int) e.getX(), (int) e.getY());
            debug("Selected index: %d", position);
            if (!listItems.isEmpty() && position >= 0 && position < listItems.size()) {
                selectedItem = listItems.get(position);
                selectedItem.click(listenerTree);
            }
            return true;
        }

        public boolean onDoubleTap(MotionEvent e) {
            // final TreeFragmentListener listenerThis = this;
            int position = itemsView.pointToPosition((int) e.getX(), (int) e.getY());
            listItems.get(position).doubleClick(listenerTree);
            return true;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && !back()) {
            return true;
        } else {
            return false;
        }
    }
}
