package com.zeoharlem.gads.pepperedrice.Network;

import com.zeoharlem.gads.pepperedrice.models.MenuFoodItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("search?q=pizza")
    Call<List<MenuFoodItem>> getMenuFoodItems(@Query("q") String q);
}
