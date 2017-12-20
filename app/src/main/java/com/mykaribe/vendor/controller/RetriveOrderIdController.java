package com.mykaribe.vendor.controller;

import android.util.Log;

import com.mykaribe.vendor.communication.IServerRequestCallback;
import com.mykaribe.vendor.communication.ServerRequester;
import com.mykaribe.vendor.listener.IOrderListUiCallback;
import com.mykaribe.vendor.listener.InputParam;
import com.mykaribe.vendor.model.Billing;
import com.mykaribe.vendor.model.Order;
import com.mykaribe.vendor.model.RequestModel;
import com.mykaribe.vendor.model.Shipping;
import com.mykaribe.vendor.model.ShippingLine;
import com.mykaribe.vendor.utils.Constant;
import com.mykaribe.vendor.utils.Logger;
import com.mykaribe.vendor.utils.MethodType;
import com.mykaribe.vendor.utils.OrderStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 15/12/2017.
 */
public class RetriveOrderIdController implements IServerRequestCallback,InputParam {

    private static final String TAG ="OrderGetController" ;
    private IOrderListUiCallback uiCallback;
    private String barcode;


    public void getOrderId(String barcode, IOrderListUiCallback uiCallback){
        this.uiCallback=uiCallback;
        this.barcode=barcode;
        RequestModel requestModel=new RequestModel(Constant.GET_ORDER_LIST_URL+"per_page="+Constant.GET_ORDER_LIMIT+"&status="+ OrderStatus.PROCESSING.toString(),this, MethodType.GET.toString(),Constant.ORDER_TYPE_ID);
        ServerRequester.getInstance().processUrl(requestModel);
    }
    public void updateOrder(int orderId){
        RequestModel requestModel=new RequestModel(Constant.ORDER_STATUS_CHANGE_URL+orderId+"/",this, MethodType.PUT.toString(),Constant.ORDER_TYPE_UPDATE);
        ServerRequester.getInstance().processUrl(requestModel);
    }


    @Override
    public void onRequestSuccess(String response, int orderType) {
        switch (orderType){
            case Constant.ORDER_TYPE_ID:
                try{
                    updateOrder(getOrderId(response));
                }catch (Exception e){

                }
                break;
            case Constant.ORDER_TYPE_UPDATE:
                try {
                    uiCallback.onOrderUpdate(getOrderStatus(response));
                }catch (Exception e){

                }
                break;
        }

    }

    @Override
    public void onRequestFail(String cause,int orderType) {
        uiCallback.onOrderUpdate(null);
    }

    private int getOrderId(String  response)throws Exception{
        int orderId=-1;
        JSONArray serverResponse=new JSONArray(response);
        for(int i=0;i<serverResponse.length();i++) {
            JSONObject jsonObj = serverResponse.getJSONObject(i);
            orderId=jsonObj.optInt(id);
            JSONArray array=jsonObj.getJSONArray(meta_data);
            for(int j=0;j<array.length();j++){
                String barcode=array.getJSONObject(i).optString(value);
                Logger.debugLog(TAG,"getOrderId>>>orderId:"+orderId+":barcode:"+barcode);
                if(this.barcode.equalsIgnoreCase(barcode)){
                    return orderId;
                }

            }

            Logger.debugLog(TAG,"getOrderId>>>orderId:"+orderId);

        }
        return orderId;

    }
    private Order getOrderStatus(String serverResponse)throws Exception{
        Order order=new Order();
        JSONObject jsonObj=new JSONObject(serverResponse);
        order.setId(jsonObj.optInt(id,-1));
        order.setStatus(jsonObj.optString(status));
        order.setOrderKey(jsonObj.optString(order_key));
        Logger.debugLog(TAG,"getOrderStatus>>>status:"+order.getStatus()+":key:"+order.getOrderKey()+":id:"+order.getId());


        return order;
    }


}
