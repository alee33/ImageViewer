package com.example.imageshow;

import com.example.imageshow.R;
import com.example.imageshow.adapters.CursorPagerAdapter;
import com.example.imageshow.rest.AlbumsMapper.FotoMapper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Fragment for view images
 * @author user
 *
 */
public class ImageViewFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.gallery_item, null);

        image = (ImageView) v.findViewById(R.id.gallery_item);

        ProgressBar progress = (ProgressBar) v.findViewById(R.id.loading);
        Bundle bundle = this.getArguments();
        switch (bundle.getInt(CursorPagerAdapter.LOADER)) {
        
        case ScreenSlidePagerActivity.INNER_SOURCE: //get images from inner source
            String id = bundle.getString(MediaStore.Images.Media._ID);
            image.setImageBitmap(decodeUri(Uri.withAppendedPath(MediaStore.Images.Media.INTERNAL_CONTENT_URI, id)));
            break;
            
        case ScreenSlidePagerActivity.OUTER_SOURCE: //get images from sdcard
            id = bundle.getString(MediaStore.Images.Media._ID);
            //image.setImageURI(Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id));
             image.setImageBitmap(decodeUri(Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)));
            break;
            
        case ScreenSlidePagerActivity.VK_FOTO: //get images from VK.com user wall
            String url = bundle.getString(FotoMapper.PHOTO_URL);
            new DownloadImageTask(image, progress).execute(url);
            break;
        }

        return v;

    }

    /*
     * @Override
     * public void onDestroyView() {
     * super.onDestroyView();
     * 
     * Drawable toRecycle = image.getDrawable();
     * if (toRecycle != null) {
     * ((BitmapDrawable) toRecycle).getBitmap().recycle();
     * }
     * 
     * }
     */
    
    /**
     * Resize image to avoid onMemoryErrors
     * @param selectedImage
     * @return
     */
    private Bitmap decodeUri(Uri selectedImage) {
        // Decode image size
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            // o.inScaled=true;
            BitmapFactory.decodeStream(getActivity().getApplicationContext().getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 100;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getActivity().getApplicationContext().getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
}
