package com.mykaribe.vendor.controller;

import com.mykaribe.vendor.communication.IServerRequestCallback;
import com.mykaribe.vendor.communication.ServerRequester;
import com.mykaribe.vendor.listener.ILoginUiCallback;
import com.mykaribe.vendor.listener.IProductIdListCallback;
import com.mykaribe.vendor.listener.InputParam;
import com.mykaribe.vendor.model.RequestModel;
import com.mykaribe.vendor.utils.Constant;
import com.mykaribe.vendor.utils.Logger;
import com.mykaribe.vendor.utils.MethodType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by USER on 18/1/2018.
 */
public class ProductIdController implements IServerRequestCallback,InputParam {
    private IProductIdListCallback uiCallback;
    public void getProductIdList(String userName,String password,IProductIdListCallback uiCallback){
        this.uiCallback=uiCallback;
        try {
            String url=Constant.GET_PRODUCT_FETCH_URL+"username="+ URLEncoder.encode(userName,"UTF-8")+"&password="+URLEncoder.encode(password,"UTF-8");
            Logger.debugLog("ProductIdController","URLEncoder>>>"+url);
            RequestModel requestModel=new RequestModel(url,this, MethodType.GET.toString(),Constant.ORDER_TYPE_PRODUCT);
            requestModel.setNoAuth(true);
            ServerRequester.getInstance().processUrl(requestModel);
        } catch (UnsupportedEncodingException e) {
            uiCallback.onFailed();
        }
    }
    @Override
    public void onRequestSuccess(String response, int orderType) {
        if(orderType==Constant.ORDER_TYPE_PRODUCT){

            ArrayList<Integer> productIdList=parseProductList(response);
            if(productIdList!=null && productIdList.size()>0){
                uiCallback.onSuccessList(productIdList);
            }else {
                uiCallback.onFailed();
            }
        }

    }

    @Override
    public void onRequestFail(String cause, int orderType) {
        uiCallback.onFailed();
    }
    private ArrayList<Integer> parseProductList(String response){
        ArrayList<Integer> productIdList=null;
        try {
           // String jsonpart=response.substring(response.indexOf("{"),response.lastIndexOf("}")+1);
            JSONObject object=new JSONObject(response);
            JSONArray array=object.getJSONArray(product_list);
            productIdList=new ArrayList<>();
            for(int i=0;i<array.length();i++){
                JSONObject object1=array.getJSONObject(i);
                productIdList.add(object1.optInt(id));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return productIdList;
    }
}
