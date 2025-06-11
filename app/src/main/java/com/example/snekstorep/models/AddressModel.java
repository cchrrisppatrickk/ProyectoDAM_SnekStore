package com.example.snekstorep.models;

public class AddressModel {
    String userAddress;
    boolean isSelected;
    String documentId;


    public AddressModel() {
    }

    public AddressModel(String userAddress, boolean isSelected, String documentId) {
        this.userAddress = userAddress;
        this.isSelected = isSelected;
        this.documentId = documentId;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}

