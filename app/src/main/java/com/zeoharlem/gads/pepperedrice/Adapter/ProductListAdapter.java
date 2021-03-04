package com.zeoharlem.gads.pepperedrice.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.zeoharlem.gads.pepperedrice.Models.Product;
import com.zeoharlem.gads.pepperedrice.databinding.MenusFoodItemListBinding;

public class ProductListAdapter extends ListAdapter<Product, ProductListAdapter.ProductViewHolder> {

    protected ProductListAdapter() {
        super(Product.itemCallback);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater   = LayoutInflater.from(parent.getContext());
        MenusFoodItemListBinding binding    = MenusFoodItemListBinding.inflate(layoutInflater, parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

    }

    class ProductViewHolder extends RecyclerView.ViewHolder{

        MenusFoodItemListBinding mItemListBinding;

        public ProductViewHolder(MenusFoodItemListBinding binding) {
            super(binding.getRoot());
            mItemListBinding    = binding;
        }
    }

    public interface ProductListener{
        void addItem(Product product);
        void onItemClicked(Product product);
    }
}
