package com.zeoharlem.gads.pepperedrice.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zeoharlem.gads.pepperedrice.Models.MenuFoodItem;
import com.zeoharlem.gads.pepperedrice.Repositories.MenuFoodListRepositories;

import java.util.ArrayList;
import java.util.List;

public class MenuFoodListViewModel extends ViewModel {

    private MutableLiveData<ArrayList<MenuFoodItem>> mListMutableLiveData;
    private MenuFoodListRepositories mListRepositories;

    public LiveData<ArrayList<MenuFoodItem>> getListMutableLiveData() {
        return mListMutableLiveData;
    }

    public void init(Context context){
        mListMutableLiveData    = MenuFoodListRepositories.getInstance(context).getMenuFoodItem();
    }
}
