package com.mykaribe.vendor.model;

import com.mykaribe.vendor.communication.IServerRequestCallback;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by USER on 15/12/2017.
 */
public class ResponseModel {
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public IServerRequestCallback getListener() {
        return listener;
    }

    public void setListener(IServerRequestCallback listener) {
        this.listener = listener;
    }

    private String url;
    private IServerRequestCallback listener;

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonObject(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    private JSONArray jsonArray;

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    private int orderType;
}
