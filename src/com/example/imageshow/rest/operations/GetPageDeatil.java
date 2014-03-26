package com.example.imageshow.rest.operations;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;


import com.example.imageshow.db.orm.DatabaseManager;
import com.example.imageshow.db.orm.Detail;
import com.example.imageshow.db.orm.Source;
import com.example.imageshow.rest.RestService.ServerError;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import com.j256.ormlite.dao.Dao;

public class GetPageDeatil implements Operation {

    public static final String HTTP = "https://api.vk.com/method/users.get"; // call rest operation
    private static final String TAG = GetPageDeatil.class.getSimpleName();
    public static final String PAGE_INFO = "page_info";

    @Override
    public Bundle execute(Context context, Request request) throws ConnectionException, DataException, CustomRequestException {
        long requestId = request.getLong("request_id");

        try {
            Dao<Source, Long> sourceDao = DatabaseManager.getInstance().getHelper().getViolationSourceDao();
            Dao<Detail, Long> detailDao = DatabaseManager.getInstance().getHelper().getViolationDeatilDao();

            Source s = (Source) sourceDao.queryForId(requestId);
            detailDao.delete(s.getDetails());
            
            Result rUsers= getUsersInfo(context, detailDao, s);
            Result rGroups= getGroupInfo(context, detailDao, s);
            Log.d(TAG, "request done");
            
            if(rUsers.isErrors && rGroups.isErrors){
                throw new ServerError(rUsers.error +" "+rGroups.error );
            }
            
             Log.d(TAG, "downloaded" );
        } catch (JsonParseException e) {
            throw new DataException(e.getMessage());
        } catch (IOException e) {
            throw new DataException(e.getMessage());
        } catch (SQLException e) {
            throw new DataException(e.getMessage());
        }

        return null;
    }

    /**
     * Get info if page is userPage
     * @param context
     * @param detailDao
     * @param s
     * @return
     * @throws ConnectionException
     * @throws IOException
     * @throws JsonParseException
     * @throws JsonProcessingException
     * @throws SQLException
     * @throws ServerError
     */
    private Result getUsersInfo(Context context, Dao<Detail, Long> detailDao, Source s) throws ConnectionException, IOException, JsonParseException, JsonProcessingException, SQLException, ServerError {
        Log.d(TAG, "perform user request");
        NetworkConnection connection = new NetworkConnection(context, "https://api.vk.com/method/users.get");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name_case", "Nom");
        params.put("v", "5.16");
        params.put("user_ids", s.getText());

        connection.setParameters(params);
        ConnectionResult result = connection.execute();
        Log.d(TAG,  result.body);
        
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final JsonFactory factory = mapper.getFactory();
        JsonParser jp = factory.createParser(result.body);
        JsonNode rootNode = mapper.readTree(jp);

        JsonNode resonseNode = rootNode.path("response");
        if (!resonseNode.isNull() && resonseNode.isArray()) {
            for (final JsonNode fotoNode : resonseNode) {
                Detail d = new Detail();
                d.setTitle(fotoNode.path("first_name").textValue() + " " + fotoNode.path("last_name").textValue());
                d.setPageId(fotoNode.path("id").longValue());
                d.itsGroup(false);
                d.setSource(s);
                detailDao.create(d);
            }
            return new Result();
        } else {
            JsonNode errorNode = rootNode.path("error").path("error_msg");
            return new Result(errorNode.textValue());
            
        }
    }
  
    /**
     * Get info if page is group page
     * @param context
     * @param detailDao
     * @param s
     * @return
     * @throws ConnectionException
     * @throws IOException
     * @throws JsonParseException
     * @throws JsonProcessingException
     * @throws SQLException
     * @throws ServerError
     */
    private Result getGroupInfo(Context context, Dao<Detail, Long> detailDao, Source s) throws ConnectionException, IOException, JsonParseException, JsonProcessingException, SQLException, ServerError {
        Log.d(TAG, "perform group request");
        NetworkConnection connection = new NetworkConnection(context, "https://api.vk.com/method/groups.getById");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("v", "5.16");
        params.put("group_ids", s.getText());

        connection.setParameters(params);
        ConnectionResult result = connection.execute();
        Log.d(TAG,  result.body);
        
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final JsonFactory factory = mapper.getFactory();
        JsonParser jp = factory.createParser(result.body);
        JsonNode rootNode = mapper.readTree(jp);

        JsonNode resonseNode = rootNode.path("response");
        if (!resonseNode.isNull() && resonseNode.isArray()) {
            for (final JsonNode fotoNode : resonseNode) {
                Detail d = new Detail();
                d.setTitle(fotoNode.path("name").textValue());
                d.setPageId(fotoNode.path("id").longValue());
                d.itsGroup(true);
                d.setSource(s);
                detailDao.create(d);
            }
            return new Result();
        } else {
            JsonNode errorNode = rootNode.path("error").path("error_msg");
            return new Result(errorNode.textValue());
            
        }
    }
    
    static class Result{
        Boolean isErrors;
        String error;
        
        public  Result( String error){
            this.error=error;
            isErrors=true;
        }
        public Result(){
            isErrors=false; 
        }
    }
}
