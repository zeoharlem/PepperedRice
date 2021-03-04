package com.zeoharlem.gads.pepperedrice.Pagers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.zeoharlem.gads.pepperedrice.Fragments.FragmentA;
import com.zeoharlem.gads.pepperedrice.Fragments.FragmentB;
import com.zeoharlem.gads.pepperedrice.Fragments.FragmentC;

public class OnboardingViewPager extends FragmentPagerAdapter {

    public OnboardingViewPager(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentA();
            case 1:
                return new FragmentB();
            case 2:
                return new FragmentC();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
