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
     String meta_data="meta_data";
     String value="value";
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

     String order_key="order_key";
     String vendor_id="vendor_id";
     String name="name";
     String user_id="user_id";
     String email_vendor="email";
     String vendor_image_url="image_url";

     String product_list="product_list";
     String line_items="line_items";
     String product_id="product_id";
}
