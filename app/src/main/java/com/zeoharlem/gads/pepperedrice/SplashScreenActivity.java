package com.zeoharlem.gads.pepperedrice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.cuberto.liquid_swipe.LiquidPager;
import com.zeoharlem.gads.pepperedrice.Pagers.OnboardingViewPager;

public class SplashScreenActivity extends BaseApp {

    LiquidPager mLiquidPager;
    OnboardingViewPager mOnboardingViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        mLiquidPager            = findViewById(R.id.pager);
        mOnboardingViewPager    = new OnboardingViewPager(getSupportFragmentManager(), 1);
        mLiquidPager.setAdapter(mOnboardingViewPager);
    }
}