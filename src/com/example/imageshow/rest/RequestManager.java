package com.example.imageshow.rest;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Request manager
 * @author user
 *
 */
public class RequestManager {

    private final static String TAG = "RequestManager";

    private static RequestManager sInstance;
    private Context context;

    private RequestManager(Context context) {
        this.context = context;
    }

    public static RequestManager from(Context context) {
        if (sInstance == null) {
            sInstance = new RequestManager(context);
        }
        return sInstance;
    }

    /**
     * Perform recuest by its type
     * @param operation -request type
     * @param parameters - request parameters
     */
    public void performRequest(Operations operation, String... parameters) {
        Log.d(TAG, " perform request " + operation);
        Intent intent = new Intent(context, RestService.class);
        intent.setData(operation.getUri());
        intent.putExtra(RestService.PARAMETERS, parameters);
        context.startService(intent);
    }

    public enum Operations {
        VK_FOTO(Uri.parse("VK:FOTO"));

        private Uri uri;

        private Operations(Uri uri) {
            this.uri = uri;
        }

        public Uri getUri() {
            return uri;
        }
    }
}
