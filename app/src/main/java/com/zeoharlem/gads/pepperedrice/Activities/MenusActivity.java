package com.zeoharlem.gads.pepperedrice.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zeoharlem.gads.pepperedrice.Adapter.MenuFoodItemRecyclerAdapter;
import com.zeoharlem.gads.pepperedrice.BaseApp;
import com.zeoharlem.gads.pepperedrice.Dialog.MenuFoodListBottomSheet;
import com.zeoharlem.gads.pepperedrice.Models.MenuFoodItem;
import com.zeoharlem.gads.pepperedrice.Network.MyVolleySingleton;
import com.zeoharlem.gads.pepperedrice.R;
import com.zeoharlem.gads.pepperedrice.Utils.L;
import com.zeoharlem.gads.pepperedrice.ViewModels.MenuFoodListViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MenusActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private MenuFoodItemRecyclerAdapter mMenuFoodItemRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private MenuFoodListViewModel viewModelProvider;
    private ProgressBar mProgressBar;
    private ArrayList<MenuFoodItem> mMenuFoodItemArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);
        mToolbar    = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView           = findViewById(R.id.menuRecyclerView);
        mProgressBar            = findViewById(R.id.progress_circular);

        //Using MVVM and LiveData with Volley. Watching with callbacks from volleyInterface
        viewModelProvider       = new ViewModelProvider(this).get(MenuFoodListViewModel.class);
        viewModelProvider.init(this);
        viewModelProvider.getListMutableLiveData().observe(this, new Observer<ArrayList<MenuFoodItem>>() {
            @Override
            public void onChanged(ArrayList<MenuFoodItem> menuFoodItems) {
                mMenuFoodItemRecyclerAdapter.setMenuFoodItemArrayList(menuFoodItems);
                mMenuFoodItemRecyclerAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }
        });

        setAdapterContents();

    }

    private void setAdapterContents(){
        mMenuFoodItemArrayList  = viewModelProvider.getListMutableLiveData().getValue();
        mMenuFoodItemRecyclerAdapter    = new MenuFoodItemRecyclerAdapter(
                getApplicationContext(),
                viewModelProvider.getListMutableLiveData().getValue()
        );

        final LinearLayoutManager layoutManager     = new LinearLayoutManager(getApplicationContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),  DividerItemDecoration.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMenuFoodItemRecyclerAdapter);
        menuFoodItemOnClicked();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem       = menu.findItem(R.id.search_menu);
        SearchView searchView   = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //getApiResultsQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void menuFoodItemOnClicked(){
        if(mMenuFoodItemRecyclerAdapter != null) {
            mMenuFoodItemRecyclerAdapter.setIMenuFoodItemOnClickListener(new MenuFoodItemRecyclerAdapter.iMenuFoodItemOnClickListener() {
                @Override
                public void onItemClickEvent(int position, View view, Bitmap bitmap) {
                    //ImageView imageView = view.findViewById(R.id.foodItemImage);
                    //imageView.buildDrawingCache();
                    //Bitmap bitmap       = imageView.getDrawingCache();

                    MenuFoodListBottomSheet listBottomSheet = new MenuFoodListBottomSheet();

                    Bundle bundle   = new Bundle();
                    bundle.putParcelable("menuItemKey", viewModelProvider.getListMutableLiveData().getValue().get(position));

                    //get ImageView resource
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    bundle.putByteArray("imageUri", byteArrayOutputStream.toByteArray());

                    listBottomSheet.setArguments(bundle);
                    listBottomSheet.show(getSupportFragmentManager(), "menuFoodBSDialog");
                }
            });
        }
    }
}