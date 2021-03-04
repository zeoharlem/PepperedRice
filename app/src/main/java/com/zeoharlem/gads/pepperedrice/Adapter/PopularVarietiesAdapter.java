package com.zeoharlem.gads.pepperedrice.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zeoharlem.gads.pepperedrice.Models.PopularVarieties;
import com.zeoharlem.gads.pepperedrice.R;

import java.util.ArrayList;

public class PopularVarietiesAdapter extends RecyclerView.Adapter<PopularVarietiesAdapter.PopularVarietiesViewHolder> {

    Context mContext;
    ArrayList<PopularVarieties> mPopularVarietiesArrayList;

    public PopularVarietiesAdapter(ArrayList<PopularVarieties> popularVarieties, Context context){
        mContext                    = context;
        mPopularVarietiesArrayList  = popularVarieties;
    }

    @NonNull
    @Override
    public PopularVarietiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PopularVarietiesViewHolder(LayoutInflater.from(mContext).inflate(R.layout.popular_food_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopularVarietiesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    static class PopularVarietiesViewHolder extends RecyclerView.ViewHolder{

        public PopularVarietiesViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
