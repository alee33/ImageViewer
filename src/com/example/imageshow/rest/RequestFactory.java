package com.example.imageshow.rest;

import com.foxykeep.datadroid.requestmanager.Request;

public class RequestFactory {
    public static final int REQUEST_PHOTO_LIST = 1;
    public static final int REQUEST_USER_DATA = 3;
    
    public static Request getPhotoListRequest(String owner_id,boolean delete) {
        Request request = new Request(REQUEST_PHOTO_LIST);
        request.put("owner_id", owner_id);
        request.put("delete", delete);
        return request;
    }

   
    public static Request getDetailData(long requestId){
        Request request = new Request(REQUEST_USER_DATA );
        request.put("request_id", requestId);
        return request;
    }
    
    private RequestFactory() {
    }
}
