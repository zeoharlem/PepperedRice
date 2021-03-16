package com.zeoharlem.gads.pepperedrice.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zeoharlem.gads.pepperedrice.models.CartItem;
import com.zeoharlem.gads.pepperedrice.models.MenuFoodItem;

import java.util.ArrayList;
import java.util.List;

public class CartRepo {

    private static CartRepo instance;
    private final MutableLiveData<List<CartItem>> mMutableCartItemLiveData  = new MutableLiveData<>();
    private MutableLiveData<Double> mMutableTotalLiveData                   = new MutableLiveData<>();

    public static CartRepo getInstance(){
        if(instance == null){
            instance    = new CartRepo();
        }
        return instance;
    }

    public LiveData<List<CartItem>> getCartItemList(){
        if(mMutableCartItemLiveData.getValue() == null){
            initCart();
        }
        return mMutableCartItemLiveData;
    }

    public void initCart() {
        mMutableCartItemLiveData.setValue(new ArrayList<CartItem>());
        calculateCartTotal();
    }

    public boolean addItemToCart(MenuFoodItem menuFoodItem){
        if(mMutableCartItemLiveData.getValue() == null){
            initCart();
        }
        List<CartItem> cartItemList = new ArrayList<>(mMutableCartItemLiveData.getValue());
        for(CartItem cartItem : cartItemList){
            if(cartItem.getMenuFoodItem().getId() == menuFoodItem.getId()){

                if (cartItem.getQuantity() == 10) {
                    return false;
                }

                int index   = cartItemList.indexOf(cartItem);
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItemList.set(index, cartItem);

                mMutableCartItemLiveData.setValue(cartItemList);
                calculateCartTotal();
                return true;
            }
        }

        CartItem cartItem           = new CartItem(menuFoodItem, 1);
        cartItemList.add(cartItem);

        mMutableCartItemLiveData.setValue(cartItemList);
        calculateCartTotal();
        return true;
    }

    public void removeItemFromCart(CartItem cartItem){
        if(mMutableCartItemLiveData.getValue() == null){
            return;
        }
        List<CartItem> cartItemList    = new ArrayList<>(mMutableCartItemLiveData.getValue());
        cartItemList.remove(cartItem);
        mMutableCartItemLiveData.setValue(cartItemList);
        calculateCartTotal();
    }

    public void changeQuantity(CartItem cartItem, int quantity){
        if(mMutableCartItemLiveData.getValue() == null){
            return;
        }
        List<CartItem> cartItemList = new ArrayList<>(mMutableCartItemLiveData.getValue());
        CartItem updateCartItem     = new CartItem(cartItem.getMenuFoodItem(), quantity);
        cartItemList.set(cartItemList.indexOf(cartItem), updateCartItem);
        mMutableCartItemLiveData.setValue(cartItemList);
        calculateCartTotal();
    }

    public LiveData<Double> getCartTotalPrice(){
        if(mMutableTotalLiveData.getValue() == null){
            mMutableTotalLiveData.setValue(0.0);
        }
        return mMutableTotalLiveData;
    }

    private void calculateCartTotal(){
        if(mMutableCartItemLiveData.getValue() == null) return;
        double total    = 0.0;
        List<CartItem> cartItemList = mMutableCartItemLiveData.getValue();
        for(CartItem cartItem : cartItemList){
            total   += cartItem.getMenuFoodItem().getPrice() * cartItem.getQuantity();
        }
        mMutableTotalLiveData.setValue(total);
    }
}
