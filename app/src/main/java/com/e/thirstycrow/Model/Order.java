package com.e.thirstycrow.Model;

public class Order {
    private String first;
    private String phone;
    private String address;
    private String payment_id;
    private String delivery_date;
    private String delivery_time;
    private Product productWith;
    private Product productWithout;
    private String total_amount;
    private String status;
    public Order(){}
    public Order(String first, String phone, String address, String payment_id, String delivery_date, String delivery_time, Product productWith, Product productWithout, String total_amount,String status) {
        this.first = first;
        this.phone = phone;
        this.address = address;
        this.payment_id = payment_id;
        this.delivery_date = delivery_date;
        this.delivery_time = delivery_time;
        this.productWith = productWith;
        this.productWithout = productWithout;
        this.total_amount = total_amount;
        this.status = status;
    }
    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public Product getProductWith() {
        return productWith;
    }

    public void setProductWith(Product productWith) {
        this.productWith = productWith;
    }

    public Product getProductWithout() {
        return productWithout;
    }

    public void setProductWithout(Product productWithout) {
        this.productWithout = productWithout;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }
}
