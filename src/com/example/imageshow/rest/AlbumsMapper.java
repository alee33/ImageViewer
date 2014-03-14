package com.example.imageshow.rest;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Objects  for provier data
 * @author user
 *
 */
public class AlbumsMapper {
    public static final String AUTHORITY = "com.example.imageshow.rest";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public interface FotosCoulmns {
        public static final String ALBUM_ID = "album_id";
        public static final String PHOTO_URL = "url";
    }

    public static final class FotoMapper implements BaseColumns, FotosCoulmns {
        public static final String CONTENT_PATH = "fotomapper";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CONTENT_PATH;
    }

}
