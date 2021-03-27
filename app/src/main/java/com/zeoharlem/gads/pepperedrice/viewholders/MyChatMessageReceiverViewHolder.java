package com.zeoharlem.gads.pepperedrice.viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zeoharlem.gads.pepperedrice.databinding.ItemReceiveMessageBinding;

public class MyChatMessageReceiverViewHolder extends RecyclerView.ViewHolder{

    public ItemReceiveMessageBinding mItemReceiveMessageBinding;

    public MyChatMessageReceiverViewHolder(@NonNull View itemView) {
        super(itemView);
        mItemReceiveMessageBinding  = ItemReceiveMessageBinding.bind(itemView);
    }
}
