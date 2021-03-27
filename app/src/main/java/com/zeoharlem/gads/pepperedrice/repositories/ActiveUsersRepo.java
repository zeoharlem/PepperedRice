package com.zeoharlem.gads.pepperedrice.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.zeoharlem.gads.pepperedrice.models.UserListProfile;


import java.util.ArrayList;

public class ActiveUsersRepo {
    private FirebaseStorage mFirebaseStorage;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private static ActiveUsersRepo instance;
    private ArrayList<UserListProfile> mUserListProfileArrayList    = new ArrayList<>();

    private ActiveUsersRepo(){
        mFirebaseAuth       = FirebaseAuth.getInstance();
        mFirebaseDatabase   = FirebaseDatabase.getInstance();
        mFirebaseUser       = mFirebaseAuth.getCurrentUser();
        mFirebaseStorage    = FirebaseStorage.getInstance();
    }

    public static ActiveUsersRepo getInstance(){
        if(instance == null){
            instance    = new ActiveUsersRepo();
        }
        return instance;
    }

    public MutableLiveData<ArrayList<UserListProfile>> getActiveUsersList(){
        MutableLiveData<ArrayList<UserListProfile>> mutableLiveData = new MutableLiveData<>();
        mFirebaseDatabase.getReference().child("UserProfile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    mUserListProfileArrayList.clear();
                    Log.d("ActiveUsers", "onDataChange: snapshot exists");
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String userTypeCheck    = dataSnapshot.child("userType").getValue(String.class);
                        if(userTypeCheck.equals("agent")) {
                            UserListProfile users = dataSnapshot.getValue(UserListProfile.class);
                            mUserListProfileArrayList.add(users);
                        }
                    }
                    mutableLiveData.setValue(mUserListProfileArrayList);
                }
                else{
                    Log.d("ActiveUsers", "onDataChange: snapshot doesnot exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("ActiveUsersRepo", "getActiveUsersList: " + new Gson().toJson(mUserListProfileArrayList));
        return mutableLiveData;
        //return mUserListProfileArrayList;
    }
}
