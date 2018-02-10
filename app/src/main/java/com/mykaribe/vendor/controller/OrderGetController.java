package com.mykaribe.vendor.controller;

import com.mykaribe.vendor.communication.IServerRequestCallback;
import com.mykaribe.vendor.communication.ServerRequester;
import com.mykaribe.vendor.listener.IOrderListUiCallback;
import com.mykaribe.vendor.listener.InputParam;
import com.mykaribe.vendor.model.Billing;
import com.mykaribe.vendor.model.Order;
import com.mykaribe.vendor.model.RequestModel;
import com.mykaribe.vendor.model.Shipping;
import com.mykaribe.vendor.model.ShippingLine;
import com.mykaribe.vendor.utils.App;
import com.mykaribe.vendor.utils.Constant;
import com.mykaribe.vendor.utils.Logger;
import com.mykaribe.vendor.utils.MethodType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 15/12/2017.
 */
public class OrderGetController implements IServerRequestCallback,InputParam {

    private static final String TAG ="OrderGetController" ;
    private IOrderListUiCallback uiCallback;
    private ArrayList<Integer> getsProductIdList;


    public void getOrderList(ArrayList<Integer> productIds,IOrderListUiCallback uiCallback){
        this.uiCallback=uiCallback;
        getsProductIdList=new ArrayList<>();
        this.getsProductIdList=productIds;
        RequestModel requestModel=new RequestModel(Constant.GET_ORDER_LIST_URL,this, MethodType.GET.toString(),Constant.ORDER_TYPE_LIST);
        ServerRequester.getInstance().processUrl(requestModel);
    }


    @Override
    public void onRequestSuccess(String serverResponse, int orderType) {
        if(orderType== Constant.ORDER_TYPE_LIST){
            Logger.debugLog(TAG,"onRequestSuccess>>>");
            try {
                uiCallback.onOrderListUpdate(getOrderListFromResponse(serverResponse));
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onRequestFail(String cause,int orderType) {
        uiCallback.onOrderListUpdate(new ArrayList<Order>());
    }

    private List<Order> getOrderListFromResponse(String response) throws Exception{
        List<Order> orders=new ArrayList<>();
        JSONArray serverResponse=new JSONArray(response);
        for(int i=0;i<serverResponse.length();i++){
            JSONObject jsonObj=serverResponse.getJSONObject(i);
            if(!isExistProductId(jsonObj)){
               continue;
            }
            Order order=new Order();
            order.setId(jsonObj.optInt(id));
            order.setTotal(jsonObj.optString(total));
            order.setCurrency(jsonObj.optString(currency));
            order.setDate_created(jsonObj.optString(date_created));
            order.setPayment_method(jsonObj.optString(payment_method));
            order.setPayment_method_title(jsonObj.optString(payment_method_title));
            order.setStatus(jsonObj.optString(status));
            order.setTotal(jsonObj.optString(total));
            order.setCurrency(jsonObj.optString(currency));
            order.setPayment_method_title(jsonObj.optString(payment_method_title));
            JSONObject billingObj=serverResponse.getJSONObject(i).getJSONObject(billing);
            Billing billing=new Billing();
            billing.setFirst_name(billingObj.optString(first_name));
            billing.setLast_name(billingObj.optString(last_name));
            billing.setEmail(billingObj.optString(email));
            order.setBilling(billing);
            Shipping ship=new Shipping();
            JSONObject shippingObj=serverResponse.getJSONObject(i).getJSONObject(shipping);
            ship.setFirst_name(shippingObj.optString(first_name));
            ship.setLast_name(shippingObj.optString(last_name));
            ship.setAddress_1(shippingObj.optString(address_1));
            ship.setCompany(shippingObj.optString(company));
            ship.setCity(shippingObj.optString(city));
            ship.setPostcode(shippingObj.optString(postcode));
            ship.setState(shippingObj.optString(state));
            ship.setCountry(shippingObj.optString(country));
            order.setShipping(ship);
            //JSONArray shippingLine=serverResponse.getJSONObject(i).getJSONArray(shipping_lines);
//            ShippingLine line=new ShippingLine();
//            String shippingMethod=shippingLine.getJSONObject(0).optString(method_title);
//            line.setMethod_title(shippingMethod);
//            order.setShipping_lines(line);
            orders.add(order);
            Logger.debugLog(TAG,"getOrderListFromResponse>>>order:"+order.toString());
        }


        return orders;
    }
    private boolean isExistProductId(JSONObject jsonObj) throws JSONException{
        boolean isExist=false;
        JSONArray array=jsonObj.getJSONArray(line_items);
        for(int i=0;i<array.length();i++){
            int productId=array.getJSONObject(i).optInt(product_id);
            Logger.debugLog(TAG,"isExistProductId>>>productId:"+productId+":App.getsProductIdList():"+getsProductIdList);
            if(getsProductIdList.contains(productId)){
                Logger.debugLog(TAG,"isExistProductId>>>exist");
                return true;
            }
        }
        return isExist;

    }


}
