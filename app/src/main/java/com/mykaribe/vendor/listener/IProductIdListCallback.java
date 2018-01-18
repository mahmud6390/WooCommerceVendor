package com.mykaribe.vendor.listener;

import java.util.ArrayList;

/**
 * Created by USER on 18/1/2018.
 */
public interface IProductIdListCallback {
    void onSuccessList(ArrayList<Integer> productId);
    void onFailed();
}
