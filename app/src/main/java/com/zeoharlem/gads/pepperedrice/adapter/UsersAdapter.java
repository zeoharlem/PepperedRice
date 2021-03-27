package com.zeoharlem.gads.pepperedrice.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.zeoharlem.gads.pepperedrice.databinding.UsersRowItemBinding;
import com.zeoharlem.gads.pepperedrice.models.UserListProfile;

public class UsersAdapter extends ListAdapter<UserListProfile, UsersAdapter.UsersViewHolder> {

    private iUserAdapterItemClickListener mIUserAdapterItemClickListener;

    public UsersAdapter(){
        super(UserListProfile.userListProfileItemCallback);
    }

    public void setIAdapterItemClickListener(iUserAdapterItemClickListener IUserAdapterItemClickListener) {
        mIUserAdapterItemClickListener  = IUserAdapterItemClickListener;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater     = LayoutInflater.from(parent.getContext());
        UsersRowItemBinding binding = UsersRowItemBinding.inflate(inflater, parent, false);
        //binding.setIUserAdapterItemClickListener(mIUserAdapterItemClickListener);
        return new UsersViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        holder.mUserListProfile = getItem(position);
        holder.mRowItemBinding.setUserListProfile(getItem(position));
        holder.mRowItemBinding.executePendingBindings();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        UsersRowItemBinding mRowItemBinding;
        UserListProfile mUserListProfile;

        public UsersViewHolder(@NonNull UsersRowItemBinding itemView) {
            super(itemView.getRoot());
            mRowItemBinding = itemView;
            itemView.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mIUserAdapterItemClickListener.onItemClicked(mUserListProfile);
        }
    }

    public interface iUserAdapterItemClickListener{
        void onItemClicked(UserListProfile userListProfile);
    }
}
