package com.zeoharlem.gads.pepperedrice.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    //public static String BASE_URL   = "https://forkify-api.herokuapp.com/api/";
    public static String BASE_URL   = "https://jsonplaceholder.typicode.com/";
    private static Retrofit retrofit;

    public static Retrofit getInstance(){
        if(retrofit == null){
            retrofit    = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
