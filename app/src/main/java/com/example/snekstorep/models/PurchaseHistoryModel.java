package com.example.snekstorep.models;

import java.util.Date;
import java.util.List;

public class PurchaseHistoryModel {

    private String documentId; // Añade este campo
    private String purchaseId;       // ID único de la compra
    private String userId;          // ID del usuario
    private String date;           // Fecha en formato "dd-MM-yyyy"
    private double totalAmount;    // Total de la compra
    private List<MyCartModel> cartItems; // Lista de productos comprados
    private String saleStatus;     // Estado: "completado", "cancelado", etc.

    // Constructor vacío (requerido para Firestore)
    public PurchaseHistoryModel() {}

    public static final String STATUS_CANCELLED = "Cancelado";
    public static final String STATUS_DELIVERED = "Entregado";
    public static final String STATUS_COMPLETED = "Completado";

    // Constructor completo
    public PurchaseHistoryModel(String purchaseId, String userId, String date,
                                double totalAmount, List<MyCartModel> cartItems,
                                String saleStatus) {
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.date = date;
        this.totalAmount = totalAmount;
        this.cartItems = cartItems;
        this.saleStatus = saleStatus;
    }

    // Getters y Setters
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<MyCartModel> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<MyCartModel> cartItems) {
        this.cartItems = cartItems;
    }

    public String getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}