package com.mykaribe.vendor.model;

import com.mykaribe.vendor.communication.IServerRequestCallback;

/**
 * Created by USER on 15/12/2017.
 */
public class RequestModel {
    public RequestModel(String url,IServerRequestCallback callback,String methodType,int orderType){
        setUrl(url);
        setOrderType(orderType);
        setListener(callback);
        setMethodType(methodType);
    }
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

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    private String url;
    private IServerRequestCallback listener;
    private String methodType;

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    private int orderType;

    public boolean isNoAuth() {
        return isNoAuth;
    }

    public void setNoAuth(boolean noAuth) {
        isNoAuth = noAuth;
    }

    private boolean isNoAuth=false;
}
