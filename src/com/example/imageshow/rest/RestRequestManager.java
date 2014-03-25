package com.example.imageshow.rest;



import com.foxykeep.datadroid.requestmanager.RequestManager;
import android.content.Context;

/**
 * Request manager
 * @author user
 *
 */
public class RestRequestManager extends RequestManager {


    private static RestRequestManager sInstance;

    private RestRequestManager(Context context) {
        super(context, RestService.class);
    }

    public static RestRequestManager from(Context context) {
        if (sInstance == null) {
            sInstance = new RestRequestManager(context);
        }
        return sInstance;
    }

}
