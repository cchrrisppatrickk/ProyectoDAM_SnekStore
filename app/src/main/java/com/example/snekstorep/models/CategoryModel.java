package com.example.snekstorep.models;

public class CategoryModel {

    private String picUrl;
    private String title;
    private Integer id;     // Ahora Integer (puede ser nulo)

    public CategoryModel() {}

    public CategoryModel(String picUrl, String title, Integer id) {
        this.picUrl = picUrl;
        this.title = title;
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}