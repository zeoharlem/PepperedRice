package com.zeoharlem.gads.pepperedrice.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zeoharlem.gads.pepperedrice.R;

public class ShopsFragment extends Fragment {

    public ShopsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Toast.makeText(getContext(), "We areshops", Toast.LENGTH_LONG).show();
        return inflater.inflate(R.layout.fragment_shops, container, false);
    }
}