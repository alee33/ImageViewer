package com.example.imageshow.rest;

import com.foxykeep.datadroid.requestmanager.Request;

/**
 * Rest service request factory
 * @author user
 *
 */
public class RequestFactory {
    public static final int REQUEST_PHOTO_LIST = 1; //get photo list
    public static final int REQUEST_DETAIL_DATA = 3; //get detail
    
    /**
     * Get photo list
     * @param owner_id -page id
     * @param delete - delete old stored items
     * @return Request
     */
    public static Request getPhotoListRequest(String owner_id,boolean delete) {
        Request request = new Request(REQUEST_PHOTO_LIST);
        request.put("owner_id", owner_id);
        request.put("delete", delete);
        return request;
    }

   /**
    * Get page detail as id and other
    * @param pageName
    * @return
    */
    public static Request getDetailData(long pageName){
        Request request = new Request(REQUEST_DETAIL_DATA );
        request.put("request_id", pageName);
        return request;
    }
    
    private RequestFactory() {
    }
}
