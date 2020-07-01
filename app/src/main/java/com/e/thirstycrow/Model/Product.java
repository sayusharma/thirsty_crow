package com.e.thirstycrow.Model;

public class Product {
    private String qty;
    private String price;
    public Product(){}
    public Product(String qty, String price) {
        this.qty = qty;
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
