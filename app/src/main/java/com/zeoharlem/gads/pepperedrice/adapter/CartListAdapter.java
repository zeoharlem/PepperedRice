package com.zeoharlem.gads.pepperedrice.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.zeoharlem.gads.pepperedrice.databinding.CartItemRowBinding;
import com.zeoharlem.gads.pepperedrice.models.CartItem;

public class CartListAdapter extends ListAdapter<CartItem, CartListAdapter.CartViewHolder> {

    private CartListListener mCartListListener;

    public CartListAdapter() {
        super(CartItem.itemCallback);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater   = LayoutInflater.from(parent.getContext());
        CartItemRowBinding cartBinding  = CartItemRowBinding.inflate(layoutInflater, parent, false);
        return new CartViewHolder(cartBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.mCartItemRowBinding.setCartItem(getItem(position));
        holder.mCartItemRowBinding.executePendingBindings();
    }

    class CartViewHolder extends RecyclerView.ViewHolder{

        CartItemRowBinding mCartItemRowBinding;

        public CartViewHolder(@NonNull CartItemRowBinding cartItemRowBinding) {
            super(cartItemRowBinding.getRoot());
            mCartItemRowBinding = cartItemRowBinding;
            mCartItemRowBinding.deleteProductButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCartListListener.deleteItem(getItem(getAdapterPosition()));
                }
            });

            mCartItemRowBinding.quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int quantity    = i + 1;
                    if(quantity == getItem(getAdapterPosition()).getQuantity()){
                        return;
                    }
                    mCartListListener.changeQuantity(getItem(getAdapterPosition()), quantity);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    public CartListListener getCartListListener() {
        return mCartListListener;
    }

    public void setCartListListener(CartListListener cartListListener) {
        mCartListListener   = cartListListener;
    }

    public interface CartListListener{
        void deleteItem(CartItem cartItem);
        void changeQuantity(CartItem cartItem, int quantity);
    }
}
