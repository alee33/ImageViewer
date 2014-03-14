package com.example.imageshow.rest;

import com.example.imageshow.rest.GetFotoOperation.DataException;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * Service for perform requests in backgraund
 * @author user
 *
 */
public class RestService extends IntentService {

    private static final String NAME = "rest_service_image";
    private final String TAG = getClass().getSimpleName();
    public static final String PARAMETERS = "parameters";

    public RestService() {
        super(NAME);
    }

    public RestService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri action = intent.getData();
        Bundle extras = intent.getExtras();
        if (extras == null || action == null) {
            // Extras contain our ResultReceiver and data is our REST action.
            // So, without these components we can't do anything useful.
            Log.d(TAG, "You did not pass extras or data with the Intent.");
        }
        Log.d(TAG, action.getScheme() + " + " + action.getSchemeSpecificPart());
        if (action.getScheme().equals("VK") && action.getSchemeSpecificPart().equals("FOTO")) {
            Log.d(TAG, "get fotos from VK");
            try {
                new GetFotoOperation().execute(this, extras.getStringArray(PARAMETERS));
            } catch (DataException e) {
                Log.e(TAG, e.toString());
            }
        }
        return;
    }

}
