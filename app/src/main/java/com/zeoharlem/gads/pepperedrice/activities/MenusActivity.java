package com.zeoharlem.gads.pepperedrice.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.zeoharlem.gads.pepperedrice.BaseApp;
import com.zeoharlem.gads.pepperedrice.adapter.MenuFoodItemRecyclerAdapter;
import com.zeoharlem.gads.pepperedrice.adapter.ProductListAdapter;
import com.zeoharlem.gads.pepperedrice.dialog.MenuFoodListBottomSheet;
import com.zeoharlem.gads.pepperedrice.models.CartItem;
import com.zeoharlem.gads.pepperedrice.models.MenuFoodItem;
import com.zeoharlem.gads.pepperedrice.R;
import com.zeoharlem.gads.pepperedrice.utils.L;
import com.zeoharlem.gads.pepperedrice.viewmodels.MenuFoodListViewModel;
import com.zeoharlem.gads.pepperedrice.databinding.ActivityMenusBinding;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MenusActivity extends BaseApp implements ProductListAdapter.ProductOnClickListener {

    private Toolbar mToolbar;
    private MenuFoodItemRecyclerAdapter mMenuFoodItemRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private MenuFoodListViewModel viewModelProvider;
    private ProgressBar mProgressBar;
    private ArrayList<MenuFoodItem> mMenuFoodItemArrayList = new ArrayList<>();
    private ActivityMenusBinding mActivityMenusBinding;
    private ProductListAdapter mProductListAdapter;
    private int cartQuantity    = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMenusBinding   = DataBindingUtil.setContentView(this, R.layout.activity_menus);
        setSupportActionBar(mActivityMenusBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mActivityMenusBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        setAdapterContents();

        viewModelProvider   = new ViewModelProvider(this).get(MenuFoodListViewModel.class);
        viewModelProvider.init(this);
        viewModelProvider.getListMutableLiveData().observe(this, new Observer<ArrayList<MenuFoodItem>>() {
            @Override
            public void onChanged(ArrayList<MenuFoodItem> menuFoodItems) {
                mProductListAdapter.submitList(menuFoodItems);
                mActivityMenusBinding.progressCircular.setVisibility(View.GONE);
            }
        });

        viewModelProvider.getCartItemList().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                if(cartItems.size() > 0) {
                    String title        = viewModelProvider.getMenuFoodItem().getTitle();
                    Snackbar snackbar   = Snackbar.make(mActivityMenusBinding.getRoot(), title + " added to cart.", Snackbar.LENGTH_LONG);
                    createSnackBarPosition(snackbar, title);
                }
                int quantity = 0;
                for (CartItem cartItem: cartItems) {
                    quantity += cartItem.getQuantity();
                }
                cartQuantity = quantity;
                invalidateOptionsMenu();
            }
        });
    }

    private void createSnackBarPosition(Snackbar snackbar, String textString){
        CoordinatorLayout coordinatorLayout = mActivityMenusBinding.menusCoordinatorLayout;
        snackbar   = Snackbar.make(coordinatorLayout, textString, Snackbar.LENGTH_LONG);
        View view  = snackbar.getView();
        CoordinatorLayout.LayoutParams params   = (CoordinatorLayout.LayoutParams)view.getLayoutParams();
        params.gravity                          = Gravity.TOP;
        view.setLayoutParams(params);
        snackbar.setAction("Checkout", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent   = new Intent(MenusActivity.this, CartListActivity.class);
                startActivity(intent);
            }
        }).show();
    }

    private void setAdapterContents(){
        mProductListAdapter = new ProductListAdapter();

        //Set the onclickListener eith by a set method or constructor
        mProductListAdapter.setOnClickListener(this);

        final LinearLayoutManager layoutManager     = new LinearLayoutManager(getApplicationContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),  DividerItemDecoration.VERTICAL);
        mActivityMenusBinding.menuRecyclerView.setLayoutManager(layoutManager);
        mActivityMenusBinding.menuRecyclerView.addItemDecoration(dividerItemDecoration);
        mActivityMenusBinding.menuRecyclerView.setAdapter(mProductListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem       = menu.findItem(R.id.search_menu);
        MenuItem cartMenu       = menu.findItem(R.id.cart_btn);

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

        View actionView     = cartMenu.getActionView();

        TextView cartBadgeTextView  = actionView.findViewById(R.id.cart_badge_text_view);
        cartBadgeTextView.setText(String.valueOf(cartQuantity));
        //cartBadgeTextView.setVisibility(cartQuantity == 0 ? View.GONE : View.VISIBLE);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(cartMenu);
            }
        });

//        return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cart_btn) {
            Intent intent = new Intent(getApplicationContext(), CartListActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private void menuFoodItemOnClicked(){
        if(mMenuFoodItemRecyclerAdapter != null) {
            mMenuFoodItemRecyclerAdapter.setIMenuFoodItemOnClickListener(new MenuFoodItemRecyclerAdapter.iMenuFoodItemOnClickListener() {
                @Override
                public void onItemClickEvent(int position, View view, Bitmap bitmap) {

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

    @Override
    public void addItem(MenuFoodItem MenuFoodItem) {
        boolean isAdded = viewModelProvider.addItemToCart(MenuFoodItem);
    }

    @Override
    public void onItemClicked(MenuFoodItem MenuFoodItem) {
        MenuFoodListBottomSheet listBottomSheet = new MenuFoodListBottomSheet();
        viewModelProvider.setItemMutableLiveData(MenuFoodItem);
        listBottomSheet.show(getSupportFragmentManager(), "menuFoodBSDialog");
    }
}