package com.zeoharlem.gads.pepperedrice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.zeoharlem.gads.pepperedrice.BaseApp;
import com.zeoharlem.gads.pepperedrice.DashboardActivity;
import com.zeoharlem.gads.pepperedrice.R;
import com.zeoharlem.gads.pepperedrice.viewmodels.MenuFoodListViewModel;

import cdflynn.android.library.checkview.CheckView;

public class CheckOutActivity extends BaseApp {

    private MenuFoodListViewModel mFoodListViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button button       = findViewById(R.id.continueShopping);

        CheckView checkView = findViewById(R.id.check);
        checkView.check();

        mFoodListViewModel  = new ViewModelProvider(this).get(MenuFoodListViewModel.class);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFoodListViewModel.resetCart();
                Intent intent   = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFoodListViewModel.resetCart();
                Intent intent   = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}