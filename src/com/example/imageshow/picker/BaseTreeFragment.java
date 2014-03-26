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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

/**
 * Source datat picker
 * 
 * @author user
 * 
 */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.picker_layout, container, false);
        setHasOptionsMenu(true);

        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        // grid view to shown sources
        itemsView = (GridView) view.findViewById(R.id.directoryList);

        // new detector to perform double click
        final GestureDetectorCompat gestureDectector = new GestureDetectorCompat(this.getActivity(), new GestureListener());
        itemsView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDectector.onTouchEvent(event);
            }
        });

        listItems = new ArrayList<TreeItemImplementation>();
        listItemsAdapter = new ImageAdapterTree(getActivity(), R.layout.picker_item, listItems);
        itemsView.setAdapter(listItemsAdapter);

        // add default source list items
        listItems.addAll(Arrays.asList(root));

        return view;
    }

    /**
     * Press back button handler
     * 
     * @return false if click performd here
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

    /**
     * Debug method
     * 
     * @param message -string message
     * @param args -parameters
     */
    private void debug(String message, Object... args) {
        Log.d(TAG, String.format(message, args));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRefreshSubs(TreeItemImplementation current) {

        Collection<TreeItemImplementation> c = current.getInnerItems();
        if (c != null) {
            listItems.clear();
            listItems.addAll(c);
            listItemsAdapter.notifyDataSetChanged();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean returnValue(TreeItemImplementation current, SourceType type) {
        // TODO Auto-generated method stub
        debug("return value %s", current.getPath());
        listenerSource.setSourceLocation(type, current.getPath());
        return true;
    }

    /**
     * Current detector to perform click and double click on items
     * 
     * @author user
     * 
     */
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

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && !back()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Set source location listener
     * 
     * @author user
     * 
     */
    public interface SetSourceListener {

        /**
         * Set source loaction
         * 
         * @param type -location type
         * @param path - inner path
         * @return
         */
        void setSourceLocation(SourceType type, String path);
    }

}
