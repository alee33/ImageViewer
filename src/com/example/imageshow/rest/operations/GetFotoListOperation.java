package com.example.imageshow.rest.operations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.example.imageshow.rest.VkPhotoMapper.FotoMapper;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxykeep.datadroid.exception.ConnectionException;
import com.foxykeep.datadroid.exception.CustomRequestException;
import com.foxykeep.datadroid.exception.DataException;
import com.foxykeep.datadroid.network.NetworkConnection;
import com.foxykeep.datadroid.network.NetworkConnection.ConnectionResult;
import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.service.RequestService.Operation;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * Get foto data from vk.com
 * 
 * @author user
 * 
 */
public class GetFotoListOperation implements Operation {

    public static final String HTTP = "https://api.vk.com/method/photos.get"; // call rest operation
    // private static final String[] MAIN_PARAMETERS = { "rev", "0", "extended", "0", "album_id", "wall" }; //base parameters
    private final String TAG = getClass().getSimpleName();

    @Override
    public Bundle execute(Context context, Request request) throws ConnectionException, DataException, CustomRequestException {
        NetworkConnection connection = new NetworkConnection(context, HTTP);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("rev", "0");
        params.put("extended", "0");
        params.put("album_id", "wall");
        params.put("v", "3.0");
        params.put("owner_id", request.getString("owner_id"));
        
        Log.d(TAG, "owner _id "+ request.getString("owner_id"));
        
        connection.setParameters(params);
        ConnectionResult result = connection.execute();
        List<ContentValues> fotos = new ArrayList<ContentValues>();

        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            final JsonFactory factory = mapper.getFactory();
            JsonParser jp = factory.createParser(result.body);
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
        } catch (JsonParseException e) {
            throw new DataException(e.getMessage());
        } catch (IOException e) {
            throw new DataException(e.getMessage());
        }
        
        if(request.getBoolean("delete")){
            context.getContentResolver().delete(FotoMapper.CONTENT_URI, null, null);
        }
        context.getContentResolver().bulkInsert(FotoMapper.CONTENT_URI, (ContentValues[]) fotos.toArray(new ContentValues[fotos.size()]));
        return null;
    }
}
