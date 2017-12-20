package com.mykaribe.vendor.communication;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by USER on 15/12/2017.
 */
public interface IServerRequestCallback {
   void onRequestSuccess(String response, int orderType);
    void onRequestFail(String cause,int orderType);
}
