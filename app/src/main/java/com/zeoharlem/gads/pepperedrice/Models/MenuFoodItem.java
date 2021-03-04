package com.zeoharlem.gads.pepperedrice.Models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class MenuFoodItem implements Parcelable {
    private int id;
    private String title;
    private String phone;
    private String price;
    private String descr;
    private String imageUrl;
    private Float ratings;
    private boolean isAvailable;
    //private byte[] mBytes;

    public MenuFoodItem(int id, String title, String descr, String price, String imageUrl) {
        this.id     = id;
        this.title  = title;
        this.descr  = descr;
        this.price  = price;
        this.imageUrl   = imageUrl;
    }

    public MenuFoodItem(int id, String title, String phone, String descr, String price, String imageUrl, Float ratings) {
        this.id         = id;
        this.title      = title;
        this.phone      = phone;
        this.price      = price;
        this.descr      = descr;
        this.imageUrl   = imageUrl;
        this.ratings    = ratings;
    }

    protected MenuFoodItem(Parcel in) {
        id          = in.readInt();
        title       = in.readString();
        phone       = in.readString();
        price       = in.readString();
        descr       = in.readString();
        imageUrl    = in.readString();
        isAvailable = in.readByte() != 0;

        if (in.readByte() == 0) {
            ratings = null;
        }
        else {
            ratings = in.readFloat();
        }
    }

    public static final Creator<MenuFoodItem> CREATOR = new Creator<MenuFoodItem>() {
        @Override
        public MenuFoodItem createFromParcel(Parcel in) {
            return new MenuFoodItem(in);
        }

        @Override
        public MenuFoodItem[] newArray(int size) {
            return new MenuFoodItem[size];
        }
    };

    public Float getRatings() {
        return ratings;
    }

    public void setRatings(Float ratings) {
        this.ratings = ratings;
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(phone);
        parcel.writeString(price);
        parcel.writeString(descr);
        parcel.writeString(imageUrl);
        parcel.writeByte((byte) (isAvailable ? 1 : 0));
        if (ratings == null) {
            parcel.writeByte((byte) 0);
        }
        else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(ratings);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuFoodItem that = (MenuFoodItem) o;
        return getId() == that.getId() &&
                isAvailable() == that.isAvailable() &&
                getTitle().equals(that.getTitle()) &&
                getPrice().equals(that.getPrice()) &&
                getImageUrl().equals(that.getImageUrl()) &&
                getRatings().equals(that.getRatings());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getPrice(), getImageUrl(), getRatings(), isAvailable());
    }

    @Override
    public String toString() {
        return "MenuFoodItem{" +
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

    public static DiffUtil.ItemCallback<MenuFoodItem> mItemCallback  = new DiffUtil.ItemCallback<MenuFoodItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull MenuFoodItem oldItem, @NonNull MenuFoodItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MenuFoodItem oldItem, @NonNull MenuFoodItem newItem) {
            return oldItem.equals(newItem);
        }
    };
}
