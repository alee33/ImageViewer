package com.example.imageshow.rest;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.example.imageshow.rest.AlbumsMapper.FotoMapper;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

/**
 * Get foto data from vk.com
 * @author user
 *
 */
public class GetFotoOperation {

    public static final String HTTP = "https://api.vk.com/method/photos.get"; //call rest operation
    private static final String[] MAIN_PARAMETERS = { "rev", "0", "extended", "0", "album_id", "wall" }; //base parameters
    private final String TAG = getClass().getSimpleName();

    Object[] concat(Object[] A, Object[] B) {
        int aLen = A.length;
        int bLen = B.length;
        Object[] C = new Object[aLen + bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }

    public void execute(Context context, Object... parameters) throws DataException {
        HttpRequest request = HttpRequest.get(HTTP, true, concat(parameters, MAIN_PARAMETERS));
        Log.d(TAG, request.toString());
        if (request.ok()) {
            Log.d(TAG, "request OK " + request.toString());

            List<ContentValues> fotos = new ArrayList<ContentValues>();

            try {
                final Reader reader = request.reader();
                final ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                final JsonFactory factory = mapper.getFactory();
                JsonParser jp = factory.createParser(reader);
                JsonNode rootNode = mapper.readTree(jp);

                JsonNode resonseNode = rootNode.path("response");
                if (resonseNode.isArray()) {
                    for (final JsonNode fotoNode : resonseNode) {
                        ContentValues foto = new ContentValues();
                        foto.put(FotoMapper.PHOTO_URL, fotoNode.path("src_big").textValue());
                        fotos.add(foto);
                    }
                }

                Log.d(TAG, "downloaded " + fotos.size());
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                throw new DataException(e);
            }
            context.getContentResolver().delete(FotoMapper.CONTENT_URI, null, null);
            context.getContentResolver().bulkInsert(FotoMapper.CONTENT_URI, (ContentValues[]) fotos.toArray(new ContentValues[fotos.size()]));
        } else {
            Log.d(TAG, "request FALSE ");
        }

    }

    class DataException extends Exception {

        public DataException(Exception e) {
            super(e);
        }

        private static final long serialVersionUID = -2090791115045233809L;

    }
}
