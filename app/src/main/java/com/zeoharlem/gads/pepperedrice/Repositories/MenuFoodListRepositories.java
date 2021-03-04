package com.zeoharlem.gads.pepperedrice.Repositories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.zeoharlem.gads.pepperedrice.Models.MenuFoodItem;
import com.zeoharlem.gads.pepperedrice.Network.MyVolleySingleton;
import com.zeoharlem.gads.pepperedrice.Utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuFoodListRepositories {
    private final Context mContext;
    private static MenuFoodListRepositories instance;
    private ArrayList<MenuFoodItem> mArrayList  = new ArrayList<>();

    private MenuFoodListRepositories(Context context){
        mContext    = context;
    }

    public static MenuFoodListRepositories getInstance(Context context){
        if(instance == null){
            instance    = new MenuFoodListRepositories(context);
        }
        return instance;
    }

    public MutableLiveData<ArrayList<MenuFoodItem>> getMenuFoodItem(){
        MutableLiveData<ArrayList<MenuFoodItem>> listMutableLiveData = new MutableLiveData<>();
        getMenuFoodItemResults(new iMenusVolleyCallback() {
            @Override
            public void onSuccess(ArrayList<MenuFoodItem> menuFoodItemArrayList) {
                listMutableLiveData.setValue(menuFoodItemArrayList);
            }
        });
        //listMutableLiveData.setValue(getMenuFoodItemResults());
        return listMutableLiveData;
    }

    /**
     * Volley Method
     * @param iMenusVolleyCallback
     */
    private void getMenuFoodItemResults(iMenusVolleyCallback iMenusVolleyCallback){
        String url  = "https://forkify-api.herokuapp.com/api/search?q=pizza";
        JsonObjectRequest jsonArrayRequest   = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray   = response.getJSONArray("recipes");
                    iMenusVolleyCallback.onSuccess(parseJson(jsonArray));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.l(mContext, error.getMessage());
            }
        });
        jsonArrayRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        MyVolleySingleton.getInstance(mContext).addToRequestQueue(jsonArrayRequest,"foodMenuList");
    }

    /**
     * without a callback method
     * @return
     */
    private ArrayList<MenuFoodItem> getMenuFoodItemResults(){
        String url  = "https://forkify-api.herokuapp.com/api/search?q=pizza";
        JsonObjectRequest jsonArrayRequest   = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("recipes");
                    mArrayList          = parseJson(jsonArray);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.l(mContext, error.getMessage());
            }
        });
        jsonArrayRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        MyVolleySingleton.getInstance(mContext).addToRequestQueue(jsonArrayRequest,"foodMenuList");
        return mArrayList;
    }

    /**
     * Parse JsonArray
     * @param response
     * @return
     */
    private ArrayList<MenuFoodItem> parseJson(JSONArray response){
        ArrayList<MenuFoodItem> menuFoodItemArrayList   = new ArrayList<>();
        if(response.length() > 0 && response != null){
            for(int i = 0; i < response.length(); i++){

                try {
                    JSONObject current  = response.getJSONObject(i);
                    MenuFoodItem menuFoodItem   = new MenuFoodItem(
                            i,
                            current.getString("title"),
                            current.getString("publisher"),
                            current.getString("publisher_url"),
                            "N1,200",
                            current.getString("image_url"),
                            (float)current.getDouble("social_rank")
                    );
                    if((i % 2) == 0){
                        menuFoodItem.setAvailable(true);
                    }
                    else{
                        menuFoodItem.setAvailable(false);
                    }
                    menuFoodItemArrayList.add(menuFoodItem);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    L.l(mContext, "JSON Exception: "+e.getMessage());
                }
            }
        }
        return menuFoodItemArrayList;
    }

    private interface iMenusVolleyCallback{
        void onSuccess(ArrayList<MenuFoodItem> menuFoodItemArrayList);
    }
}
