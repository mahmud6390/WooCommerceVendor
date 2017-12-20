package com.mykaribe.vendor.model;

/**
 * Created by USER on 15/12/2017.
 */
public class Order {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    private String orderKey;

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPayment_method_title() {
        return payment_method_title;
    }

    public void setPayment_method_title(String payment_method_title) {
        this.payment_method_title = payment_method_title;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ShippingLine getShipping_lines() {
        return shipping_lines;
    }

    public void setShipping_lines(ShippingLine shipping_lines) {
        this.shipping_lines = shipping_lines;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    private int id;
    private String status;
    private Billing billing;
    private Shipping shipping;
    private String payment_method;
    private String payment_method_title;
    private String total;
    private String currency;
    private ShippingLine shipping_lines;
    private String date_created;

    @Override
    public String toString() {
        return "id:"+id+":status:"+status+":total:"+total+":currency:"+currency+"billing:"+billing.toString();
    }
}
