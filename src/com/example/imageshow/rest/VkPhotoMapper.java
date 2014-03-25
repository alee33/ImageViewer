package com.example.imageshow.rest;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Objects  for provier data
 * @author user
 *
 */
public class VkPhotoMapper {
    public static final String AUTHORITY = "com.example.imageshow.rest";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public interface FotosCoulmns {
        public static final String PHOTO_URL = "url";
        public static final String PHOTO_BITMAP = "bitmap";
    }
    
    public static final class FotoMapper implements BaseColumns, FotosCoulmns {
        public static final String CONTENT_PATH = "fotos";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH);
        public static final Uri CONTENT_URI_ITEM = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH+"/#" );
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CONTENT_PATH;
        public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + CONTENT_PATH;
    }
   

}
