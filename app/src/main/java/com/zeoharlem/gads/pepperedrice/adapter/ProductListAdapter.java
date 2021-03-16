package com.zeoharlem.gads.pepperedrice.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.zeoharlem.gads.pepperedrice.models.MenuFoodItem;
import com.zeoharlem.gads.pepperedrice.databinding.MenusFoodItemListBinding;

public class ProductListAdapter extends ListAdapter<MenuFoodItem, ProductListAdapter.ProductViewHolder> {

    private ProductOnClickListener mOnClickListener;

    public ProductListAdapter() {
        super(MenuFoodItem.itemCallback);
    }

    public ProductOnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(ProductOnClickListener onClickListener) {
        mOnClickListener    = onClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater       = LayoutInflater.from(parent.getContext());
        MenusFoodItemListBinding binding    = MenusFoodItemListBinding.inflate(layoutInflater, parent, false);
        binding.setIProductOnClickListener(mOnClickListener);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        MenuFoodItem menuFoodItem   = getItem(position);
        holder.mItemListBinding.setMenuFoodItem(menuFoodItem);
        holder.mItemListBinding.executePendingBindings();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder{

        MenusFoodItemListBinding mItemListBinding;

        public ProductViewHolder(MenusFoodItemListBinding binding) {
            super(binding.getRoot());
            mItemListBinding    = binding;
        }
    }

    public interface ProductOnClickListener{
        void addItem(MenuFoodItem MenuFoodItem);
        void onItemClicked(MenuFoodItem MenuFoodItem);
    }
}
