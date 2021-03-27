package com.zeoharlem.gads.pepperedrice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.zeoharlem.gads.pepperedrice.R;
import com.zeoharlem.gads.pepperedrice.models.ChatMessage;
import com.zeoharlem.gads.pepperedrice.viewholders.MyChatMessageReceiverViewHolder;
import com.zeoharlem.gads.pepperedrice.viewholders.MyChatMessageSenderViewHolder;

import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter {


    final int ITEM_SENT     = 1;
    final int ITEM_RECEIVED = 2;
    Context mContext;
    ArrayList<ChatMessage> mChatMessageArrayList;

    public ChatMessageAdapter(Context context, ArrayList<ChatMessage> chatMessageArrayList) {
        mContext                = context;
        mChatMessageArrayList   = chatMessageArrayList;
    }

    public ArrayList<ChatMessage> getChatMessageArrayList() {
        return mChatMessageArrayList;
    }

    public void setChatMessageArrayList(ArrayList<ChatMessage> chatMessageArrayList) {
        mChatMessageArrayList = chatMessageArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SENT){
            View view   = LayoutInflater.from(mContext).inflate(R.layout.item_sent_message, parent, false);
            return new MyChatMessageSenderViewHolder(view);
        }
        else{
            View view   = LayoutInflater.from(mContext).inflate(R.layout.item_receive_message, parent, false);
            return new MyChatMessageReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getClass().equals(MyChatMessageSenderViewHolder.class)){
            MyChatMessageSenderViewHolder viewHolder    = (MyChatMessageSenderViewHolder) holder;
            //L.l(mContext, "Sent Message:" + mChatMessageArrayList.get(position).getMessage());
            viewHolder.mItemSentMessageBinding.idSentMessage.setText(mChatMessageArrayList.get(position).getMessage());
        }
        else{
            MyChatMessageReceiverViewHolder viewHolder    = (MyChatMessageReceiverViewHolder) holder;
            //L.l(mContext, "Received Message:" + mChatMessageArrayList.get(position).getMessage());
            viewHolder.mItemReceiveMessageBinding.idReceivedMessage.setText(mChatMessageArrayList.get(position).getMessage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(FirebaseAuth.getInstance().getUid().equals(mChatMessageArrayList.get(position).getUid())){
            return ITEM_SENT;
        }
        else{
            return ITEM_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        return mChatMessageArrayList.size();
    }
}
