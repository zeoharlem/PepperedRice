package com.zeoharlem.gads.pepperedrice.viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zeoharlem.gads.pepperedrice.databinding.ItemSentMessageBinding;

public class MyChatMessageSenderViewHolder extends RecyclerView.ViewHolder{

    public ItemSentMessageBinding mItemSentMessageBinding;

    public MyChatMessageSenderViewHolder(@NonNull View itemView) {
        super(itemView);
        mItemSentMessageBinding = ItemSentMessageBinding.bind(itemView);
    }
}
