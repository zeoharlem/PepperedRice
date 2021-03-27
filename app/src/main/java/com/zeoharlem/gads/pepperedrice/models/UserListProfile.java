package com.zeoharlem.gads.pepperedrice.models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Objects;

public class UserListProfile {
    private String uid, name, phoneNumber, profileImage, userType, token;

    public UserListProfile(){

    }

    public UserListProfile(String uid, String name, String phoneNumber, String profileImage) {
        this.uid            = uid;
        this.name           = name;
        this.phoneNumber    = phoneNumber;
        this.profileImage   = profileImage;
        this.userType       = userType;
    }

    public UserListProfile(String uid, String name, String phoneNumber, String profileImage, String userType) {
        this(uid, name, phoneNumber, profileImage);
        this.userType       = userType;
    }

    public UserListProfile(String uid, String name, String phoneNumber, String profileImage, String userType, String token) {
        this(uid, name, phoneNumber, profileImage, userType);
        this.token  = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "UserListProfile{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }

    @BindingAdapter("android:usersImageUrl")
    public static void loadImage(CircularImageView imageView, String profileImage){
        Glide.with(imageView).asBitmap().load(profileImage).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imageView.setImageBitmap(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserListProfile that = (UserListProfile) o;
        return getUid().equals(that.getUid()) &&
                getName().equals(that.getName()) &&
                getPhoneNumber().equals(that.getPhoneNumber()) &&
                getProfileImage().equals(that.getProfileImage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUid(), getName(), getPhoneNumber(), getProfileImage());
    }

    public static DiffUtil.ItemCallback<UserListProfile> userListProfileItemCallback    = new DiffUtil.ItemCallback<UserListProfile>() {
        @Override
        public boolean areItemsTheSame(@NonNull UserListProfile oldItem, @NonNull UserListProfile newItem) {
            return oldItem.getUid().equals(newItem.getUid());
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserListProfile oldItem, @NonNull UserListProfile newItem) {
            return oldItem.equals(newItem);
        }
    };
}
