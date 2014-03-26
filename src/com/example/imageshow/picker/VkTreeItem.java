package com.example.imageshow.picker;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.example.imageshow.R;
import com.example.imageshow.SourceActivity.SourceType;
import com.example.imageshow.db.orm.DatabaseManager;
import com.example.imageshow.db.orm.Detail;
import com.example.imageshow.db.orm.Source;
import com.example.imageshow.rest.RequestFactory;
import com.example.imageshow.rest.RestRequestManager;
import com.example.imageshow.rest.RestService;
import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.requestmanager.RequestManager.RequestListener;
import com.j256.ormlite.dao.Dao;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

/**
 * Vk source item
 * 
 * @author user
 * 
 */
public class VkTreeItem implements TreeItemImplementation {

    protected String description;
    protected Context context;
    protected boolean root;
    protected Source source;
    private static final String TAG = VkTreeItem.class.getName();
    private final TreeItemImplementation item;
    private final Map<Request, RequestListener> mRequestListenerMap;

    public VkTreeItem(Source source, Context context) {
        this.source = source;
        this.context = context;
        item = this;
        mRequestListenerMap = new ConcurrentHashMap<Request, RequestListener>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<TreeItemImplementation> getInnerItems() {
        Dao<Source, Long> sourceDao;
        List<TreeItemImplementation> listFr = new ArrayList<TreeItemImplementation>();
        try {
            sourceDao = DatabaseManager.getInstance().getHelper().getViolationSourceDao();
            List<Source> list = sourceDao.queryForAll();
            for (int i = 0; i < list.size(); i++) {
                listFr.add(new VkTreeItem(list.get(i), context));
            }
            listFr.add(new VkTreeItemCreate(context));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // s.close();
        return listFr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreeItemImplementation getParentItem() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        return source.getText();
    }

    /**
     * Get source detail
     * 
     * @return
     */
    private String getDetails() {
        StringBuilder detail = new StringBuilder();
        for (Detail order : source.getDetails()) {
            detail.append(";").append(order.getTitle());
        }

        return detail.length() == 0 ? source.getText() : detail.toString().replaceFirst(";", "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getDetails();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getImage() {
        // TODO Auto-generated method stub
        return R.drawable.vk;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void click(final TreeFragmentListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.vk_id));

        // Set up the input
        final EditText input = new EditText(context);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(source.getText());
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                source.setText(input.getText().toString());
                try {
                    Dao<Source, Long> sourceDao = DatabaseManager.getInstance().getHelper().getViolationSourceDao();
                    sourceDao.update(source);

                    // perform request to VK
                    execute(RequestFactory.getDetailData(source.getId()), new RequestProgressHandler(listener));
                } catch (SQLException e) {
                    showError(e.getMessage());
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    /**
     * Execure request with listener
     * 
     * @param request -current request
     * @param listener -result listener
     */
    protected void execute(Request request, RequestProgressHandler listener) {
        mRequestListenerMap.put(request, listener);
        listener.showProgress(); // shoe progress bar while result wait
        RestRequestManager.from(context).execute(request, listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doubleClick(TreeFragmentListener listener) {

        boolean isDelete = true;
        // handler = new RequestPhotoListener(listener);
        for (Detail d : source.getDetails()) {
            Log.d(TAG, d.getPageId() + " - " + d.getTitle());

            execute(RequestFactory.getPhotoListRequest((d.isGroup() ? "-" : "") + d.getPageId(), isDelete), new RequestPhotoListener(listener));
            isDelete = false;
        }

    }

    /**
     * Show error if exists
     * 
     * @param error - error text
     */
    protected void showError(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(android.R.string.dialog_alert_title).setMessage(error == null ? context.getString(R.string.faled_to_load_data) : error).create().show();
    }

    /**
     * Request listener with progressbar
     * 
     * @author user
     * 
     */
    public class RequestProgressHandler implements RequestListener {

        protected WeakReference<TreeFragmentListener> listener;
        protected WeakReference<ProgressDialog> progress;

        public RequestProgressHandler(TreeFragmentListener listener) {
            super();
            this.listener = new WeakReference<TreeFragmentListener>(listener);
        }

        /**
         * Show progressbar
         */
        public void showProgress() {
            progress = new WeakReference<ProgressDialog>(ProgressDialog.show(context, context.getResources().getString(R.string.server_request),
                    context.getResources().getString(R.string.server_request_desc), true));
        }

        @Override
        public void onRequestFinished(Request request, Bundle resultData) {
            Log.d(TAG, "onRequestFinished");
            refreshListener(request);
        }

        @Override
        public void onRequestConnectionError(Request request, int statusCode) {
            showError(null);
            refreshListener(request);

        }

        @Override
        public void onRequestDataError(Request request) {
            showError(null);
            refreshListener(request);

        }

        @Override
        public void onRequestCustomError(Request request, Bundle resultData) {
            showError(resultData.getString(RestService.SERVER_ERROR_MESSAGE));
            refreshListener(request);
        }

        /**
         * Get response by request
         * 
         * @param request - request
         */
        private void refreshListener(Request request) {
            mRequestListenerMap.remove(request); // remove request listener from list
            ProgressDialog p;
            if ((p = progress.get()) != null) {
                p.dismiss(); // hide progressbar
            }
            TreeFragmentListener l;
            if ((l = listener.get()) != null) {
                l.onRefreshSubs(item); // refresh subs
            }
        }

        /**
         * Show error as dialog
         * 
         * @param error -error text
         */
        protected void showError(String error) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(android.R.string.dialog_alert_title).setMessage(error == null ? context.getString(R.string.faled_to_load_data) : error).create().show();
        }

    }

    /**
     * Photo request listener
     * 
     * @author user
     * 
     */
    public class RequestPhotoListener extends RequestProgressHandler {

        public RequestPhotoListener(TreeFragmentListener listener) {
            super(listener);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void showProgress() {
            progress = new WeakReference<ProgressDialog>(ProgressDialog.show(context, context.getResources().getString(R.string.server_request_photo),
                    context.getResources().getString(R.string.server_request_desc), true));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onRequestFinished(Request request, Bundle resultData) {

            TreeFragmentListener l;
            if ((l = listener.get()) != null) {
                l.returnValue(item, SourceType.VK); // return source value
            }
            mRequestListenerMap.remove(request);
            ProgressDialog p;
            if ((p = progress.get()) != null) {
                p.dismiss(); // hide preogressbar
            }
        }
    }

}
