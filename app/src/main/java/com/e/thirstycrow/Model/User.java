package com.e.thirstycrow.Model;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String first;
    private String last;
    private String email;
    private String address;
    private Order orders;
    public User(){

    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public User(String first, String last, String email) {
        this.first = first;
        this.last = last;
        this.email = email;
    }

    public Order getOrders() {
        return orders;
    }

    public void setOrders(Order orders) {
        this.orders = orders;
    }
}

