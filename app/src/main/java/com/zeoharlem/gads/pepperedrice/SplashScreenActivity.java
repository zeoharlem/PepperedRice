package com.zeoharlem.gads.pepperedrice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.cuberto.liquid_swipe.LiquidPager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zeoharlem.gads.pepperedrice.Pagers.OnboardingViewPager;

public class SplashScreenActivity extends BaseApp {

    LiquidPager mLiquidPager;
    OnboardingViewPager mOnboardingViewPager;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mFirebaseAuth           = FirebaseAuth.getInstance();
        mFirebaseUser           = mFirebaseAuth.getCurrentUser();

        setContentView(R.layout.activity_splash_screen);
        mLiquidPager            = findViewById(R.id.pager);
        mOnboardingViewPager    = new OnboardingViewPager(getSupportFragmentManager(), 1);
        mLiquidPager.setAdapter(mOnboardingViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mFirebaseUser != null){
            startActivity(new Intent(this, DashboardActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }
}