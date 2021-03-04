package com.zeoharlem.gads.pepperedrice.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;
import com.zeoharlem.gads.pepperedrice.Models.FoodItem;
import com.zeoharlem.gads.pepperedrice.Network.MyVolleySingleton;
import com.zeoharlem.gads.pepperedrice.R;

import java.util.ArrayList;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodItemAdapterViewHolder> {

    private ImageLoader mImageLoader;
    private MyVolleySingleton volleySingleton;
    private ArrayList<FoodItem> mFoodItemArrayList;
    private Context mContext;

    public FoodItemAdapter(ArrayList<FoodItem> foodItemArrayList, Context context){
        mFoodItemArrayList  = foodItemArrayList;
        mContext            = context;
        volleySingleton     = MyVolleySingleton.getInstance(context);
        mImageLoader        = volleySingleton.getImageLoader();
    }

    @NonNull
    @Override
    public FoodItemAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodItemAdapterViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.food_item_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemAdapterViewHolder holder, int position) {
        holder.setFoodItemDetails(mFoodItemArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return mFoodItemArrayList.size();
    }

    static class FoodItemAdapterViewHolder extends RecyclerView.ViewHolder {

        private KenBurnsView mKenBurnsView;
        private TextView mTextViewTitle;
        private Button priceButton;
        private ProgressBar mProgressBar;

        public FoodItemAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mKenBurnsView   = itemView.findViewById(R.id.kenburnView);
            mTextViewTitle  = itemView.findViewById(R.id.textViewTitle);
            priceButton     = itemView.findViewById(R.id.priceButton);
            mProgressBar    = itemView.findViewById(R.id.progress_circular);
        }

        void setFoodItemDetails(FoodItem foodItem){
            //Picasso.get().load(foodItem.getImageUrl()).into(mKenBurnsView);
            Glide.with(itemView).load(foodItem.getImageUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            mProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(mKenBurnsView);
            mTextViewTitle.setText(foodItem.getTitle());
            priceButton.setText(foodItem.getPrice());
        }
    }
}
