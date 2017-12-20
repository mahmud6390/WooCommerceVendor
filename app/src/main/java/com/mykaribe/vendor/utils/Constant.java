package com.mykaribe.vendor.utils;

/**
 * Created by USER on 15/12/2017.
 */
public class Constant {
    public static final String CONSUMER_KEY="ck_64f3f18c95b1b650d1979aee26dc11b7f2ee26a1";
    public static final String CONSUMER_SECRET="cs_5e4303f149f82fdf5a431dc828d414edde1d711b";
    public static final String FIREBASE_SENDER_ID="888081972066";
    public static final String BASE_URL="https://dev.mykaribe.com";
    public static final String ORDER_STATUS_CHANGE_URL=BASE_URL+"/wp-json/wc/v2/orders/";
    public static final String GET_ORDER_LIST_URL=BASE_URL+"/wp-json/wc/v2/orders?";

    public static final int ORDER_TYPE_ADD=1;
    public static final int ORDER_TYPE_LIST=2;
    public static final int ORDER_TYPE_UPDATE=3;
    public static final int ORDER_TYPE_DELETE=4;
    public static final int ORDER_TYPE_ID=5;

    public static final int GET_ORDER_LIMIT=50;


}
