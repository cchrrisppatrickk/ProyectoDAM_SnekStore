package com.example.snekstorep.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductModel implements Serializable {

    String title;
    String description;
    String rating;
    int price;
    String img_url;

    private ArrayList<String> size;
    public ProductModel(){}

    public ProductModel(String title, String description, String rating, int price, String img_url, ArrayList<String> size) {
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.price = price;
        this.img_url = img_url;
        this.size = size;
    }

    public ArrayList<String> getSize() {
        return size;
    }

    public void setSize(ArrayList<String> size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
