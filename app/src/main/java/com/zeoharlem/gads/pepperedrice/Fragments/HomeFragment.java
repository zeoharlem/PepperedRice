package com.zeoharlem.gads.pepperedrice.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zeoharlem.gads.pepperedrice.Activities.MenusActivity;
import com.zeoharlem.gads.pepperedrice.Adapter.FoodItemAdapter;
import com.zeoharlem.gads.pepperedrice.Adapter.PopularVarietiesAdapter;
import com.zeoharlem.gads.pepperedrice.Models.FoodItem;
import com.zeoharlem.gads.pepperedrice.Models.PopularVarieties;
import com.zeoharlem.gads.pepperedrice.Network.MyVolleySingleton;
import com.zeoharlem.gads.pepperedrice.R;
import com.zeoharlem.gads.pepperedrice.Utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private ViewPager2 foodItemViewPager;
    private ArrayList<FoodItem> mFoodItemArrayList;
    private FoodItemAdapter mFoodItemAdapter;

    private ProgressBar mProgressBar;

    private PopularVarietiesAdapter mPopularVarietiesAdapter;
    private ArrayList<PopularVarieties> mPopularVarietiesArrayList;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;

    private ImageButton menuButton;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view   = inflater.inflate(R.layout.fragment_home, container, false);
        foodItemViewPager   = view.findViewById(R.id.foodPager2View);
        mProgressBar        = view.findViewById(R.id.progress_circular_home);
        mFoodItemArrayList  = new ArrayList<>();

        //set Images for food items
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getRestApiCallFoodItem(new iMenusVolleyCallback() {
                    @Override
                    public void onSuccess(ArrayList<FoodItem> menuFoodItemArrayList) {
                        //L.l(getContext(), String.valueOf(menuFoodItemArrayList));
                        mProgressBar.setVisibility(View.GONE);

                        mFoodItemArrayList  = menuFoodItemArrayList;
                        mFoodItemAdapter    = new FoodItemAdapter(mFoodItemArrayList, getContext());
                        foodItemViewPager.setAdapter(mFoodItemAdapter);
                        foodItemViewPager.setClipToPadding(false);
                        foodItemViewPager.setClipChildren(false);
                        foodItemViewPager.setOffscreenPageLimit(4);
                        foodItemViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                        foodItemViewPager.setPageTransformer(setCompositePageTransformer());
                    }
                });
            }
        }, 1200);

        //setAdapterforPopularVarieties(view);
        setButtonsAction(view);

        return view;
    }

    private void setButtonsAction(View view){
        menuButton  = view.findViewById(R.id.menuButton);
        menuButton.setOnClickListener(this);
    }

    //Set ArrayList Item for FoodItems
    private void setDetailsFoodItems(String imageUrl, String title, String price){
        FoodItem foodItem   = new FoodItem();
        foodItem.setImageUrl(imageUrl);
        foodItem.setTitle(title);
        foodItem.setPrice(price);
        mFoodItemArrayList.add(foodItem);
    }

    private CompositePageTransformer setCompositePageTransformer(){
        CompositePageTransformer compositePageTransformer   = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.95f + r * 0.05f);
            }
        });
        return compositePageTransformer;
    }

    private void getRestApiCallFoodItem(final iMenusVolleyCallback iMenusVolleyCallback){
        String url                          = "https://forkify-api.herokuapp.com/api/search?q=pizza";
        JsonObjectRequest jsonArrayRequest  = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //L.l(getApplicationContext(), response.toString());
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
                L.l(getContext(), error.getMessage());
            }
        });
        jsonArrayRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        MyVolleySingleton.getInstance(getContext()).addToRequestQueue(jsonArrayRequest,"foodItem");
    }

    private ArrayList<FoodItem> parseJson(JSONArray response){
        ArrayList<FoodItem> menuFoodItemArrayList   = new ArrayList<>();
        if(response.length() > 0 && response != null){
            for(int i = 0; i < 5; i++){

                try {
                    JSONObject current  = response.getJSONObject(i);
                    //JSONObject company  = current.getJSONObject("company");
                    //JSONObject address  = current.getJSONObject("address");
                    FoodItem menuFoodItem   = new FoodItem(
                            current.getString("image_url"),
                            current.getString("title"),
                            "N1,200",
                            i,
                            current.getString("publisher"),
                            current.getString("publisher_url"),
                            (float)current.getDouble("social_rank")
                    );
                    menuFoodItemArrayList.add(menuFoodItem);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    L.l(getContext(), "JSON Exception: "+e.getMessage());
                }
            }
        }
        return menuFoodItemArrayList;
    }

    private void setAdapterforPopularVarieties(View view){
//        mPopularVarietiesArrayList  = new ArrayList<>();
//        mPopularVarietiesAdapter    = new PopularVarietiesAdapter(mPopularVarietiesArrayList, getContext());
//
//        recyclerView        = view.findViewById(R.id.varietiesRecyclerView);
//        gridLayoutManager   = new GridLayoutManager(getContext(), 2);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setAdapter(mPopularVarietiesAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menuButton:
                Intent intent   = new Intent(getContext(), MenusActivity.class);
                startActivity(intent);
        }
    }

    private interface iMenusVolleyCallback{
        void onSuccess(ArrayList<FoodItem> menuFoodItemArrayList);
    }
}