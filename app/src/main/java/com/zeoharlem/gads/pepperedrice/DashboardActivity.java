package com.zeoharlem.gads.pepperedrice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zeoharlem.gads.pepperedrice.activities.ActiveMobileOutletsActivity;
import com.zeoharlem.gads.pepperedrice.activities.CartListActivity;
import com.zeoharlem.gads.pepperedrice.activities.MapsActivity;
import com.zeoharlem.gads.pepperedrice.activities.MenusActivity;
import com.zeoharlem.gads.pepperedrice.activities.SetupProfileActivity;
import com.zeoharlem.gads.pepperedrice.data.SessionManager;
import com.zeoharlem.gads.pepperedrice.databinding.ActivityDashboardBinding;
import com.zeoharlem.gads.pepperedrice.fragments.DrinksFragment;
import com.zeoharlem.gads.pepperedrice.fragments.HomeFragment;
import com.zeoharlem.gads.pepperedrice.fragments.ShawamaFragment;
import com.zeoharlem.gads.pepperedrice.fragments.ShopsFragment;
import com.zeoharlem.gads.pepperedrice.models.CartItem;
import com.zeoharlem.gads.pepperedrice.viewmodels.MenuFoodListViewModel;

import java.util.List;

public class DashboardActivity extends BaseApp implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private static final float END_SCALE = 0.7f;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    ImageView menuDrawerIconBurger;
    LinearLayout contentView;
    BottomNavigationView mBottomNavigationView;
    FrameLayout mFrameLayout;
    private Toolbar mToolbar;
    private MenuFoodListViewModel mViewModel;
    private int cartQuantity    = 0;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDatabase;

    ActivityDashboardBinding mActivityDashboardBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityDashboardBinding   = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        setSupportActionBar(mActivityDashboardBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mFirebaseAuth           = FirebaseAuth.getInstance();
        mFirebaseUser           = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase       = FirebaseDatabase.getInstance();

        mBottomNavigationView   = mActivityDashboardBinding.bottomNavigationView;
        mFrameLayout            = mActivityDashboardBinding.dashboardFrameLayout;

        checkIfUserProfileExist();

        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        mBottomNavigationView.setBackground(null);

        getSupportFragmentManager().beginTransaction().replace(R.id.dashboardFrameLayout, new HomeFragment()).commit();

        setDrawerLayoutTask();

        setViewModelItemRow();

        mActivityDashboardBinding.faButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent   = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setViewModelItemRow(){
        mViewModel  = new ViewModelProvider(this).get(MenuFoodListViewModel.class);
        mViewModel.getCartItemList().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                int quantity = 0;
                for (CartItem cartItem: cartItems) {
                    quantity += cartItem.getQuantity();
                }
                cartQuantity = quantity;
                invalidateOptionsMenu();
            }
        });
    }

    private void setDrawerLayoutTask(){
        mDrawerLayout   = mActivityDashboardBinding.drawerLayout;
        mNavigationView = mActivityDashboardBinding.navView;
        mNavigationView.bringToFront();
        //mNavigationView.setNavigationItemSelectedListener(this);
        activityNavigations(mNavigationView);
        mNavigationView.setCheckedItem(R.id.nav_dashboard);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mActivityDashboardBinding.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //animateNavigationDrawer();
    }

    private void activityNavigations(NavigationView navigationView){
        navigationView.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mFirebaseAuth.signOut();
                SessionManager.getInstance(DashboardActivity.this).destroySession();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
                return false;
            }
        });
    }

    private void animateNavigationDrawer() {
        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //super.onDrawerSlide(drawerView, slideOffset);
                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerVisible(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void checkIfUserProfileExist(){
        String uid  = mFirebaseUser.getUid();
        mFirebaseDatabase.getReference().child("UserProfile").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    startActivity(new Intent(DashboardActivity.this, SetupProfileActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment   = new HomeFragment();
        switch(item.getItemId()){
            case R.id.home:
                selectedFragment    = new HomeFragment();
                break;
            case R.id.shawama:
                selectedFragment    = new ShawamaFragment();
                break;
            case R.id.drinks:
                //selectedFragment    = new DrinksFragment();
                activityRedirect(MenusActivity.class);
                break;
            case R.id.shops:
                //selectedFragment    = new ShopsFragment();
                activityRedirect(ActiveMobileOutletsActivity.class);
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.dashboardFrameLayout, selectedFragment).commit();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.top_menu, menu);
        MenuItem menuItem   = menu.findItem(R.id.cart_btn);
        View actionView     = menuItem.getActionView();

        TextView cartBadgeTextView  = actionView.findViewById(R.id.cart_badge_text_view);
        cartBadgeTextView.setText(String.valueOf(cartQuantity));
        //cartBadgeTextView.setVisibility(cartQuantity == 0 ? View.GONE : View.VISIBLE);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(menuItem);
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        checkIfUserProfileExist();
    }

    private void activityRedirect(Class c){
        startActivity(new Intent(this, c));
    }
}