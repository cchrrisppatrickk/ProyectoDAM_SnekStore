package com.example.snekstorep.models;

public class MyCartModel {
    String productName;
    String productImage;
    String productDate;
    String productTime;
    String productSize;
    Double productPrice;
    int totalQuantity;
    Double totalPrice;

    public MyCartModel() {}

    // Getters y Setters
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductImage() { return productImage; }
    public void setProductImage(String productImage) { this.productImage = productImage; }

    public String getProductDate() { return productDate; }
    public void setProductDate(String productDate) { this.productDate = productDate; }

    public String getProductTime() { return productTime; }
    public void setProductTime(String productTime) { this.productTime = productTime; }

    public String getProductSize() { return productSize; }
    public void setProductSize(String productSize) { this.productSize = productSize; }

    public Double getProductPrice() { return productPrice; }
    public void setProductPrice(Double productPrice) { this.productPrice = productPrice; }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
}