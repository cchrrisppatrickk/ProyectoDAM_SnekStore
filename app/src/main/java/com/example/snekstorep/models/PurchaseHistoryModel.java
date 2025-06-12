package com.example.snekstorep.models;

import java.util.Date;
import java.util.List;

public class PurchaseHistoryModel {

    private String purchaseId;
    private String userId;
    private String purchaseDate;
    private double totalAmount;
    private String saleStatus; // "completado", "cancelado"
    private String shippingStatus; // "en empaquetado", "en ruta", "entregado"
    private List<MyCartModel> items;



    // Constructor con parámetros
    public PurchaseHistoryModel(String purchaseId, String userId, String purchaseDate,
                                double totalAmount, List<MyCartModel> items) {
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.purchaseDate = purchaseDate;
        this.totalAmount = totalAmount;
        this.items = items;
        this.saleStatus = "completado";
        this.shippingStatus = "en empaquetado";
    }


    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public List<MyCartModel> getItems() {
        return items;
    }

    public void setItems(List<MyCartModel> items) {
        this.items = items;
    }
}