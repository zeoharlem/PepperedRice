package com.zeoharlem.gads.pepperedrice.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeoharlem.gads.pepperedrice.DashboardActivity;
import com.zeoharlem.gads.pepperedrice.LoginActivity;
import com.zeoharlem.gads.pepperedrice.R;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public class FragmentA extends Fragment {

    public FragmentA() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view           = inflater.inflate(R.layout.fragment_a, container, false);
        TextView textView   = view.findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent   = new Intent(getContext(), LoginActivity.class);
                getActivity().getSupportFragmentManager().popBackStack(null, POP_BACK_STACK_INCLUSIVE);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }
}