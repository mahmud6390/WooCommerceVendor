package com.mykaribe.vendor.listener;

import com.mykaribe.vendor.model.Vendor;

/**
 * Created by USER on 17/1/2018.
 */
public interface ILoginUiCallback {
    void onLoginSuccess(Vendor vendor);
    void onLoginFailed();
}
