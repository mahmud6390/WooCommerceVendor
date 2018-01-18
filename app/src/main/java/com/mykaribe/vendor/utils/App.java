package com.mykaribe.vendor.utils;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.mykaribe.vendor.controller.ProductIdController;
import com.mykaribe.vendor.listener.IProductIdListCallback;

import java.util.ArrayList;

/**
 * Created by USER on 14/4/2016.
 */
public class App extends Application implements IProductIdListCallback{
    private static Context sContext;
    private static ArrayList<Integer> sProductIdList;
    @Override
    public void onCreate() {
        super.onCreate();
        sContext=getApplicationContext();
        sProductIdList=new ArrayList<>();
        int vendorId=PreferenceHelper.getInt(Constant.VENDOR_ID,0);
        String userName=PreferenceHelper.getString(Constant.USER_NAME,"");
        String password=PreferenceHelper.getString(Constant.PASSWORD,"");
        Logger.debugLog("App","onCreate>>>vendorId:"+vendorId+":userName:"+userName+":password:"+password);
        if(vendorId!=0 && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)){
            new ProductIdController().getProductIdList(userName,password,this);
        }
    }
    public static Context getContext(){
        return sContext;
    }
    public static ArrayList<Integer> getsProductIdList(){
        return sProductIdList;
    }

    @Override
    public void onSuccessList(ArrayList<Integer> productId) {
        Logger.debugLog("APP","onSuccessList>>>>productId:"+productId);
        sProductIdList.addAll(productId);

    }

    @Override
    public void onFailed() {
        Logger.debugLog("APP","onFailed>>>>");
        PreferenceHelper.removeAllData();

    }
}
