package com.mykaribe.vendor.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.mykaribe.vendor.controller.ProductIdController;
import com.mykaribe.vendor.listener.IProductIdListCallback;
import com.mykaribe.vendor.view.LoginActivity;

import java.util.ArrayList;

/**
 * Created by USER on 14/4/2016.
 */
public class App extends Application implements IProductIdListCallback{
    private static Context sContext;
    private static ArrayList<Integer> sProductIdList;
    public static final int REQUEST_CODE_CAMERA = 5;
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
    public static void setProductIdList(ArrayList<Integer> list){
        sProductIdList.addAll(list);
    }

    @Override
    public void onSuccessList(ArrayList<Integer> productId) {
        Logger.debugLog("APP","onSuccessList>>>>productId:"+productId);
        sProductIdList.addAll(productId);

    }
    public static boolean isCameraPermissionOk(Activity activity){

        if (!App.check_camera_Permission(activity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_CAMERA);
                return false;
            }
            return true;
        }
        return true;
    }

    public static boolean check_camera_Permission(Activity activity) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            return true;
        }

        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onFailed() {
        Logger.debugLog("APP","onFailed>>>>");
        PreferenceHelper.removeAllData();
        Intent intent=new Intent(getContext(),LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
}
