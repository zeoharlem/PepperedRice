package com.zeoharlem.gads.pepperedrice.Models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class Product {
    private int id;
    private String title;
    private String phone;
    private String price;
    private String descr;
    private String imageUrl;
    private Float ratings;
    private boolean isAvailable;

    public Product(int id, String title, String phone, String price, String descr, String imageUrl, Float ratings, boolean isAvailable) {
        this.id         = id;
        this.title      = title;
        this.phone      = phone;
        this.price      = price;
        this.descr      = descr;
        this.imageUrl   = imageUrl;
        this.ratings    = ratings;
        this.isAvailable = isAvailable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Float getRatings() {
        return ratings;
    }

    public void setRatings(Float ratings) {
        this.ratings = ratings;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", phone='" + phone + '\'' +
                ", price='" + price + '\'' +
                ", descr='" + descr + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", ratings=" + ratings +
                ", isAvailable=" + isAvailable +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return getId() == product.getId() &&
                isAvailable() == product.isAvailable() &&
                getTitle().equals(product.getTitle()) &&
                getPrice().equals(product.getPrice()) &&
                getDescr().equals(product.getDescr()) &&
                getImageUrl().equals(product.getImageUrl()) &&
                getRatings().equals(product.getRatings());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getPrice(), getDescr(), getImageUrl(), getRatings(), isAvailable());
    }

    public static DiffUtil.ItemCallback<Product> itemCallback   = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return  oldItem.equals(newItem);
        }
    };
}
