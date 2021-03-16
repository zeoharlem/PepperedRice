package com.zeoharlem.gads.pepperedrice.models;

public class FoodItem {
    private String imageUrl;
    private String title;
    private double price;
    private int id;
    private String phone;
    private String descr;
    private Float ratings;

    public FoodItem() {
    }

    public FoodItem(String imageUrl, String title, double price, int id, String phone, String descr, Float ratings) {
        this.imageUrl   = imageUrl;
        this.title      = title;
        this.price      = price;
        this.id         = id;
        this.phone      = phone;
        this.descr      = descr;
        this.ratings    = ratings;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl   = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price  = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Float getRatings() {
        return ratings;
    }

    public void setRatings(Float ratings) {
        this.ratings = ratings;
    }
}
