package com.zeoharlem.gads.pepperedrice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.zeoharlem.gads.pepperedrice.Fragments.DrinksFragment;
import com.zeoharlem.gads.pepperedrice.Fragments.HomeFragment;
import com.zeoharlem.gads.pepperedrice.Fragments.ShawamaFragment;
import com.zeoharlem.gads.pepperedrice.Fragments.ShopsFragment;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private static final float END_SCALE = 0.7f;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    ImageView menuDrawerIconBurger;
    LinearLayout contentView;
    BottomNavigationView mBottomNavigationView;
    FrameLayout mFrameLayout;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //contentView = findViewById(R.id.contentViewMove);
        mToolbar        = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mBottomNavigationView   = findViewById(R.id.bottomNavigationView);
        mFrameLayout            = findViewById(R.id.dashboardFrameLayout);

        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        mBottomNavigationView.setBackground(null);

        getSupportFragmentManager().beginTransaction().replace(R.id.dashboardFrameLayout, new HomeFragment()).commit();

        setDrawerLayoutTask();

    }

    private void forceTls(){
        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        }
        catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private void setDrawerLayoutTask(){
        mDrawerLayout   = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.bringToFront();
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setCheckedItem(R.id.nav_dashboard);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


//        menuDrawerIconBurger    = findViewById(R.id.hambuger_menu);
//        menuDrawerIconBurger.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(mDrawerLayout.isDrawerVisible(GravityCompat.START)){
//                    mDrawerLayout.closeDrawer(GravityCompat.START);
//                }
//                else{
//                    mDrawerLayout.openDrawer(GravityCompat.START);
//                }
//            }
//        });

        //animateNavigationDrawer();
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

    private void navigateDashboard(){
        Intent intent   = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment   = null;
        switch(item.getItemId()){
            case R.id.home:
                selectedFragment    = new HomeFragment();
                break;
            case R.id.shawama:
                selectedFragment    = new ShawamaFragment();
                break;
            case R.id.drinks:
                selectedFragment    = new DrinksFragment();
                break;
            case R.id.shops:
                selectedFragment    = new ShopsFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.dashboardFrameLayout, selectedFragment).commit();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }
}