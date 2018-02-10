package com.mykaribe.vendor.controller;

import com.mykaribe.vendor.communication.IServerRequestCallback;
import com.mykaribe.vendor.communication.ServerRequester;
import com.mykaribe.vendor.listener.ILoginUiCallback;
import com.mykaribe.vendor.listener.InputParam;
import com.mykaribe.vendor.model.RequestModel;
import com.mykaribe.vendor.model.Vendor;
import com.mykaribe.vendor.utils.Constant;
import com.mykaribe.vendor.utils.Logger;
import com.mykaribe.vendor.utils.MethodType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by USER on 17/1/2018.
 */
public class LoginController implements IServerRequestCallback,InputParam {
    private ILoginUiCallback uiCallback;
    private String userName,password;
    public void sendLoginInfo(String userName,String password,ILoginUiCallback uiCallback){
        this.uiCallback=uiCallback;
        this.userName=userName;
        this.password=password;

        try {
            String url=Constant.LOGIN_URL+"username="+URLEncoder.encode(userName,"UTF-8")+"&password="+URLEncoder.encode(password,"UTF-8");
            Logger.debugLog("LoginController","URLEncoder>>>"+url);
            RequestModel requestModel=new RequestModel(url,this, MethodType.GET.toString(),Constant.ORDER_TYPE_LOGIN);
            requestModel.setNoAuth(true);
            ServerRequester.getInstance().processUrl(requestModel);
        } catch (UnsupportedEncodingException e) {
            uiCallback.onLoginFailed();
        }


    }
    @Override
    public void onRequestSuccess(String response, int orderType) {
        if(orderType==Constant.ORDER_TYPE_LOGIN){
          Vendor vendor=  parseResponse(response);
            if(vendor!=null){
                uiCallback.onLoginSuccess(vendor);
            }else{
                uiCallback.onLoginFailed();
            }
        }

    }

    @Override
    public void onRequestFail(String cause, int orderType) {
        uiCallback.onLoginFailed();

    }
    private Vendor parseResponse(String response){
        Vendor vendor=null;
        try {
           String jsonpart=response.substring(response.indexOf("{"),response.lastIndexOf("}")+1);
            Logger.debugLog("LOGIN","parseResponse>>>jsonpart:"+jsonpart);
            JSONObject jsonObject=new JSONObject(jsonpart);
            vendor=new Vendor();
            vendor.setId(jsonObject.optInt(vendor_id,0));
            vendor.setNickName(jsonObject.optString(name,""));
            vendor.setEmail(jsonObject.optString(email_vendor));
            vendor.setImageUrl(jsonObject.optString(vendor_image_url));
            vendor.setUserName(userName);
            vendor.setPassword(password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vendor;


    }
}
