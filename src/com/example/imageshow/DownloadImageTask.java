package com.example.imageshow;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
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
    
    private final WeakReference<ImageView> imageViewReference;
    private final WeakReference<ProgressBar> progressViewReference;
    
    public DownloadImageTask(ImageView bmImage, ProgressBar progressBar ) {
        imageViewReference = new WeakReference<ImageView>(bmImage);
        progressViewReference = new WeakReference<ProgressBar>(progressBar);

        progressBar.setVisibility(View.VISIBLE);
        bmImage.setVisibility(View.GONE);
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
       
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
            
        } catch (Exception e) {
            Log.e("Error", e.toString());
            e.printStackTrace();
        }
      
        return mIcon11;
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
  }
    
    protected void onPostExecute(Bitmap result) {
        if (imageViewReference != null && result != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(result);
                imageView.setVisibility(View.VISIBLE);
            }
        }
        final ProgressBar progressBar = progressViewReference.get();
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

    }
}
