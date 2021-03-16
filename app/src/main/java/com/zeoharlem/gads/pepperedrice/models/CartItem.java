package com.zeoharlem.gads.pepperedrice.models;

import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class CartItem {
    private MenuFoodItem mMenuFoodItem;
    private int quantity;

    public CartItem(MenuFoodItem menuFoodItem, int quantity) {
        mMenuFoodItem   = menuFoodItem;
        this.quantity   = quantity;
    }

    public MenuFoodItem getMenuFoodItem() {
        return mMenuFoodItem;
    }

    public void setMenuFoodItem(MenuFoodItem menuFoodItem) {
        mMenuFoodItem   = menuFoodItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity   = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return getQuantity() == cartItem.getQuantity() &&
                getMenuFoodItem().equals(cartItem.getMenuFoodItem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMenuFoodItem(), getQuantity());
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "mMenuFoodItem=" + mMenuFoodItem +
                ", quantity=" + quantity +
                '}';
    }

    @BindingAdapter("android:setVal")
    public static void getSelectedSpinnerValue(Spinner spinner, int quantity){
        spinner.setSelection(quantity - 1, true);
    }

    public static DiffUtil.ItemCallback<CartItem> itemCallback  = new DiffUtil.ItemCallback<CartItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            //return oldItem.getMenuFoodItem().equals(newItem.getMenuFoodItem());
            return oldItem.getQuantity() == newItem.getQuantity();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem.equals(newItem);
        }
    };
}
