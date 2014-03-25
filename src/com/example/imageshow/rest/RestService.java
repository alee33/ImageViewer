package com.example.imageshow.rest;

import android.os.Bundle;

import com.example.imageshow.rest.operations.GetFotoListOperation;
import com.example.imageshow.rest.operations.GetUserInfo;
import com.foxykeep.datadroid.exception.CustomRequestException;
import com.foxykeep.datadroid.requestmanager.Request;
import com.foxykeep.datadroid.service.RequestService;

/**
 * Service for perform requests in backgraund
 * 
 * @author user
 * 
 */
public class RestService extends RequestService {

    
    public static final String SERVER_ERROR_MESSAGE="error_msg";
    
    @Override
    public void onCreate() {
        super.onCreate();
        setIntentRedelivery(true);
    }
    
    @Override
    public Operation getOperationForType(int requestType) {
        switch (requestType) {
        case RequestFactory.REQUEST_PHOTO_LIST:
            return new GetFotoListOperation();
        case RequestFactory.REQUEST_USER_DATA:
            return new GetUserInfo();   
        default:
            return null;
        }
    }
   
    public static class ServerError extends CustomRequestException{

        private static final long serialVersionUID = 6014661783008611248L;
        
        public ServerError(String message){
            super(message);
        }
        
    }
   
    @Override
    protected Bundle onCustomRequestException(Request request, CustomRequestException exception) {
        Bundle b= new Bundle();
        b.putString(SERVER_ERROR_MESSAGE, exception.getMessage());
        return b;
    }
    

}
