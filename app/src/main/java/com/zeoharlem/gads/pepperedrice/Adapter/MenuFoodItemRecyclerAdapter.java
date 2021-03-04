package com.zeoharlem.gads.pepperedrice.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.zeoharlem.gads.pepperedrice.Models.MenuFoodItem;
import com.zeoharlem.gads.pepperedrice.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MenuFoodItemRecyclerAdapter extends RecyclerView.Adapter<MenuFoodItemRecyclerAdapter.MenuFoodItemnViewHolder> {

    private ArrayList<MenuFoodItem> mMenuFoodItemArrayList;
    private Context mContext;
    private iMenuFoodItemOnClickListener mIMenuFoodItemOnClickListener;

    public MenuFoodItemRecyclerAdapter(Context context, ArrayList<MenuFoodItem> menuFoodItemArrayList) {
        this.mContext               = context;
        this.mMenuFoodItemArrayList = menuFoodItemArrayList;
    }

    public ArrayList<MenuFoodItem> getMenuFoodItemArrayList() {
        return mMenuFoodItemArrayList;
    }

    public void setMenuFoodItemArrayList(ArrayList<MenuFoodItem> menuFoodItemArrayList) {
        mMenuFoodItemArrayList = menuFoodItemArrayList;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public iMenuFoodItemOnClickListener getIMenuFoodItemOnClickListener() {
        return mIMenuFoodItemOnClickListener;
    }

    public void setIMenuFoodItemOnClickListener(iMenuFoodItemOnClickListener IMenuFoodItemOnClickListener) {
        mIMenuFoodItemOnClickListener = IMenuFoodItemOnClickListener;
    }

    @NonNull
    @Override
    public MenuFoodItemnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view   = LayoutInflater.from(parent.getContext()).inflate(R.layout.menus_food_item_list, parent, false);
        return new MenuFoodItemnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuFoodItemnViewHolder holder, int position) {
        holder.setHolderViewContent(mMenuFoodItemArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        if(mMenuFoodItemArrayList != null) {
            return mMenuFoodItemArrayList.size();
        }
        return 0;
    }

    class MenuFoodItemnViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextViewTitle, mTextViewFoodDesc, mTextViewRatings, textViewIsAvailable;
        CircularImageView mCircularImageView;
        AppCompatRatingBar mRatingBar;
        Bitmap mBitmapHolder;

        public MenuFoodItemnViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewRatings    = itemView.findViewById(R.id.ratingsText);
            mTextViewTitle      = itemView.findViewById(R.id.textViewTitle);
            mTextViewFoodDesc   = itemView.findViewById(R.id.foodDescription);
            mCircularImageView  = itemView.findViewById(R.id.foodItemImage);
            mRatingBar          = itemView.findViewById(R.id.ratings);
            textViewIsAvailable = itemView.findViewById(R.id.isAvailable);
            itemView.setOnClickListener(this);
        }

        public void setHolderViewContent(MenuFoodItem menuFoodItem){
            mTextViewTitle.setText(menuFoodItem.getTitle());
            mTextViewFoodDesc.setText(menuFoodItem.getDescr());
            textViewIsAvailable.setText(menuFoodItem.isAvailable() ? "Available" : "Out of Stock");
            if(!menuFoodItem.isAvailable()){
                textViewIsAvailable.setTextColor(mContext.getResources().getColor(R.color.colorRed300));
            }
            //textViewIsAvailable.setTextColor(menuFoodItem.isAvailable() ? );
            //Glide.with(itemView).load(menuFoodItem.getImageUrl()).into(mCircularImageView);
            Glide.with(mContext).asBitmap().load(menuFoodItem.getImageUrl()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    mCircularImageView.setImageBitmap(resource);
                    mBitmapHolder   = resource;
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
            mRatingBar.setNumStars(5);
            mRatingBar.setMax(100);
            mRatingBar.setRating(menuFoodItem.getRatings());

        }

        @Override
        public void onClick(View view) {
            if(mIMenuFoodItemOnClickListener != null){
                mIMenuFoodItemOnClickListener.onItemClickEvent(getAdapterPosition(), view, mBitmapHolder);
            }
        }
    }

    public interface iMenuFoodItemOnClickListener{
        void onItemClickEvent(int position, View view, Bitmap bitmap);
    }
}
