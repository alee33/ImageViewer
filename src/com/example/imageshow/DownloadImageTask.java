package com.example.imageshow;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
/**
 * 
 * @author user
 * Download web recourse in background thread
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    //image reference
    private final WeakReference<ImageView> imageViewReference;
    //progress bar reference
    private final WeakReference<ProgressBar> progressViewReference;
    
    public DownloadImageTask(ImageView bmImage, ProgressBar progressBar ) {
        imageViewReference = new WeakReference<ImageView>(bmImage);
        progressViewReference = new WeakReference<ProgressBar>(progressBar);
        //show progressbar
        progressBar.setVisibility(View.VISIBLE);
        //hide image
        bmImage.setVisibility(View.GONE);
    }

    /**
     * Download image in background
     */
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
       
        Bitmap icon = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            icon = BitmapFactory.decodeStream(in);
            
        } catch (Exception e) {
            Log.e("Error", e.toString());
            e.printStackTrace();
        }
      
        return icon;
    }

   /**
    * After process
    */
    protected void onPostExecute(Bitmap result) {
        //show image if exists
        final ImageView imageView = imageViewReference.get();
        if (imageView != null && result != null) {
                imageView.setImageBitmap(result);
                imageView.setVisibility(View.VISIBLE);
        }
        //hide progress bar if exists
        final ProgressBar progressBar = progressViewReference.get();
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

    }
}
