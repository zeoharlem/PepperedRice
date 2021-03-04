package com.zeoharlem.gads.pepperedrice.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeoharlem.gads.pepperedrice.DashboardActivity;
import com.zeoharlem.gads.pepperedrice.R;

public class FragmentB extends Fragment {

    public FragmentB() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view           = inflater.inflate(R.layout.fragment_b, container, false);
        TextView textView   = view.findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent   = new Intent(getContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}