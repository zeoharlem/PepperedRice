package com.zeoharlem.gads.pepperedrice.viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zeoharlem.gads.pepperedrice.models.ChatMessage;

public abstract class MultiViewHolder extends RecyclerView.ViewHolder {
    public MultiViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    abstract void onBindVewHolder(Integer position, ChatMessage chatMessage);

    abstract void onViewDetached(Integer position, ChatMessage chatMessage);
}
