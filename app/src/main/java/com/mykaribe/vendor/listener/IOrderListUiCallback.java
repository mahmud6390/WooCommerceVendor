package com.mykaribe.vendor.listener;

import com.mykaribe.vendor.model.Order;

import java.util.List;

/**
 * Created by USER on 15/12/2017.
 */
public interface IOrderListUiCallback {
    void onOrderListUpdate(List<Order> orders);
    void onOrderUpdate(Order order);
}
