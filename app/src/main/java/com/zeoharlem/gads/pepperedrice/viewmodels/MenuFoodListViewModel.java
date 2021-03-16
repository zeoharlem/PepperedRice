package com.zeoharlem.gads.pepperedrice.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zeoharlem.gads.pepperedrice.models.CartItem;
import com.zeoharlem.gads.pepperedrice.models.MenuFoodItem;
import com.zeoharlem.gads.pepperedrice.repositories.CartRepo;
import com.zeoharlem.gads.pepperedrice.repositories.MenuFoodListRepo;

import java.util.ArrayList;
import java.util.List;

public class MenuFoodListViewModel extends ViewModel {

    private MutableLiveData<ArrayList<MenuFoodItem>> mListMutableLiveData;
    private final MutableLiveData<MenuFoodItem> mItemMutableLiveData    = new MutableLiveData<>();

    private MenuFoodItem mMenuFoodItem  = new MenuFoodItem();

    private final CartRepo mCartRepository    = CartRepo.getInstance();

    public LiveData<ArrayList<MenuFoodItem>> getListMutableLiveData() {
        return mListMutableLiveData;
    }

    public void setItemMutableLiveData(MenuFoodItem menuFoodItem){
        mItemMutableLiveData.setValue(menuFoodItem);
    }

    public LiveData<MenuFoodItem> getItemMutableLiveData() {
        return mItemMutableLiveData;
    }

    public LiveData<List<CartItem>> getCartItemList(){
        return mCartRepository.getCartItemList();
    }

    public boolean addItemToCart(MenuFoodItem menuFoodItem){
        mMenuFoodItem   = menuFoodItem;
        return mCartRepository.addItemToCart(menuFoodItem);
    }

    public void removeItemFromCart(CartItem cartItem){
        mCartRepository.removeItemFromCart(cartItem);
    }

    public void changeQuantity(CartItem cartItem, int quantity){
        mCartRepository.changeQuantity(cartItem, quantity);
    }

    public LiveData<Double> getCartTotalPrice(){
        return mCartRepository.getCartTotalPrice();
    }

    public void resetCart(){
        mCartRepository.initCart();
    }

    public void init(Context context){
        mListMutableLiveData    = MenuFoodListRepo.getInstance(context).getMenuFoodItem();
    }

    public MenuFoodItem getMenuFoodItem() {
        return mMenuFoodItem;
    }
}
