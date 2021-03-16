package com.zeoharlem.gads.pepperedrice.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class MenuFoodItem implements Parcelable {
    private int id;
    private String title;
    private String phone;
    private double price;
    private String descr;
    private String imageUrl;
    private Float ratings;
    private boolean isAvailable;
    private static Bitmap mBitmapHolder;
    //private byte[] mBytes;


    public MenuFoodItem() {
    }

    public MenuFoodItem(int id, String title, String descr, double price, String imageUrl) {
        this.id     = id;
        this.title  = title;
        this.descr  = descr;
        this.price  = price;
        this.imageUrl   = imageUrl;
    }

    public MenuFoodItem(int id, String title, String phone, String descr, double price, String imageUrl, Float ratings) {
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
        price       = in.readDouble();
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public Bitmap getBitmapHolder() {
        return mBitmapHolder;
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
        parcel.writeDouble(price);
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
                getPrice() == that.getPrice() &&
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

    public static DiffUtil.ItemCallback<MenuFoodItem> itemCallback  = new DiffUtil.ItemCallback<MenuFoodItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull MenuFoodItem oldItem, @NonNull MenuFoodItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MenuFoodItem oldItem, @NonNull MenuFoodItem newItem) {
            return oldItem.equals(newItem);
        }
    };

    @BindingAdapter("android:productImage")
    public static void loadImage(CircularImageView imageView, String imageUrl){
        Glide.with(imageView).asBitmap().load(imageUrl).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imageView.setImageBitmap(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    @BindingAdapter("android:imageViewUri")
    public static void imageViewUri(ImageView imageView, String imageUrl){
        Glide.with(imageView).asBitmap().load(imageUrl).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imageView.setImageBitmap(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    public static Bitmap getBitmapResource(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] mBytesArray = byteArrayOutputStream.toByteArray();
        return BitmapFactory.decodeByteArray(mBytesArray, 0, mBytesArray.length);
    }
}
