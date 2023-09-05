package com.example.btl_android.models;

public class MyOrderModel {
    String productName,currentDate,currentTime,productPrice,totalQuantity;
    int totalPrice ;
    String documentId;


    public MyOrderModel(String productName, String currentDate, String currentTime, String productPrice, String totalQuantity, int totalPrice) {
        this.productName = productName;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.productPrice = productPrice;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
    }

    public MyOrderModel(String productName, String currentDate, String currentTime, String productPrice, String totalQuantity, int totalPrice, String documentId) {
        this.productName = productName;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.productPrice = productPrice;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.documentId = documentId;
    }

    public MyOrderModel() {
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
