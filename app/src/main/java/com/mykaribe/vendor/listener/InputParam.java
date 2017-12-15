package com.mykaribe.vendor.listener;

import com.mykaribe.vendor.model.Billing;
import com.mykaribe.vendor.model.Shipping;
import com.mykaribe.vendor.model.ShippingLine;

/**
 * Created by USER on 16/12/2017.
 */
public interface InputParam {
     String id="id";
     String status="status";
     String billing="billing";
     String shipping="shipping";
     String payment_method="payment_method";
     String payment_method_title="payment_method_title";
     String total="total";
     String currency="currency";
     String shipping_lines="shipping_lines";
     String date_created="date_created";

     String first_name="first_name";
     String last_name="last_name";
     String company="company";
     String address_1="address_1";
     String address_2="address_2";
     String city="city";
     String state="state";
     String postcode="postcode";
     String country="country";
     String email="email";
     String phone="phone";
     String method_title="method_title";
}
