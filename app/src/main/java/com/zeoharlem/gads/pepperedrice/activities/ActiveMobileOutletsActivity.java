package com.zeoharlem.gads.pepperedrice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zeoharlem.gads.pepperedrice.R;
import com.zeoharlem.gads.pepperedrice.adapter.UsersAdapter;
import com.zeoharlem.gads.pepperedrice.databinding.ActivityActiveMobileOutletsBinding;
import com.zeoharlem.gads.pepperedrice.models.UserListProfile;
import com.zeoharlem.gads.pepperedrice.viewmodels.ActiveUsersViewModel;

import java.util.ArrayList;

public class ActiveMobileOutletsActivity extends AppCompatActivity implements UsersAdapter.iUserAdapterItemClickListener{

    ActivityActiveMobileOutletsBinding mActivityActiveUsersBinding;
    private UsersAdapter mUsersAdapter;
    private ArrayList<UserListProfile> mUserListProfileArrayList    = new ArrayList<>();
    private ActiveUsersViewModel mActiveUsersViewModel;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private String myUsername, myImageUrl, myPhoneNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityActiveUsersBinding = DataBindingUtil.setContentView(this, R.layout.activity_active_mobile_outlets);
        //setContentView(mActivityActiveUsersBinding.getRoot());

        setSupportActionBar(mActivityActiveUsersBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mFirebaseAuth       = FirebaseAuth.getInstance();
        mFirebaseDatabase   = FirebaseDatabase.getInstance();

        setUsersAdapterAction();

        mActiveUsersViewModel   = new ViewModelProvider(this).get(ActiveUsersViewModel.class);
        mActiveUsersViewModel.init();
        mActiveUsersViewModel.getArrayListMutableLiveData().observe(this, new Observer<ArrayList<UserListProfile>>() {
            @Override
            public void onChanged(ArrayList<UserListProfile> userListProfiles) {
                //L.l(getApplicationContext(), new Gson().toJson(userListProfiles));
                mUsersAdapter.submitList(userListProfiles);
            }
        });

        mActivityActiveUsersBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        if(mFirebaseAuth.getUid() != null) {
            mFirebaseDatabase.getReference("UserProfile").child(mFirebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        myUsername = snapshot.child("name").getValue().toString();
                        myImageUrl = snapshot.child("profileImage").getValue().toString();
                        myPhoneNum = snapshot.child("phoneNumber").getValue().toString();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void setUsersAdapterAction(){
        mUsersAdapter   = new UsersAdapter();
        mUsersAdapter.setIAdapterItemClickListener(this);

        final LinearLayoutManager layoutManager     = new LinearLayoutManager(getApplicationContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),  DividerItemDecoration.VERTICAL);
        mActivityActiveUsersBinding.usersRecyclerView.setLayoutManager(layoutManager);
        mActivityActiveUsersBinding.usersRecyclerView.addItemDecoration(dividerItemDecoration);
        mActivityActiveUsersBinding.usersRecyclerView.setAdapter(mUsersAdapter);
    }

//    @Override
//    public void onItemClicked() {
//        startActivity(new Intent(this, ChatActivity.class));
//    }

    @Override
    public void onItemClicked(UserListProfile userListProfile) {
        startActivity(new Intent(this, ChatActivity.class)
                .putExtra("myUsername", myUsername)
                .putExtra("myImageUrl", myImageUrl)
                .putExtra("myPhoneNum", myPhoneNum)
                .putExtra("username", userListProfile.getName())
                .putExtra("uid", userListProfile.getUid())
                .putExtra("details", userListProfile.getPhoneNumber())
                .putExtra("imageUrl", userListProfile.getProfileImage()));
    }


    @Override
    protected void onResume() {
        super.onResume();
        String currentId    = FirebaseAuth.getInstance().getUid();
        mFirebaseDatabase.getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        String currentId    = FirebaseAuth.getInstance().getUid();
        mFirebaseDatabase.getReference().child("presence").child(currentId).setValue("Offline");
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_active_mobile_outlets);
//    }
}