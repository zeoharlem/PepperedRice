package com.zeoharlem.gads.pepperedrice.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zeoharlem.gads.pepperedrice.models.UserListProfile;
import com.zeoharlem.gads.pepperedrice.repositories.ActiveUsersRepo;

import java.util.ArrayList;

public class ActiveUsersViewModel extends ViewModel {
    private MutableLiveData<ArrayList<UserListProfile>> mArrayListMutableLiveData;
    private MutableLiveData<UserListProfile> mProfileMutableLiveData    = new MutableLiveData<>();
    private UserListProfile mUserListProfile    = new UserListProfile();

    //Get ArrayList of UserListProfile
    public MutableLiveData<ArrayList<UserListProfile>> getArrayListMutableLiveData() {
        return mArrayListMutableLiveData;
    }

    public void setProfileMutableLiveData(UserListProfile userListProfile){
        mProfileMutableLiveData.setValue(userListProfile);
    }

    public void init(){
        if(mArrayListMutableLiveData == null){
            mArrayListMutableLiveData   = new MutableLiveData<>();
        }
        Log.d("ActiveUsers", "init: Called");
        mArrayListMutableLiveData   = ActiveUsersRepo.getInstance().getActiveUsersList();
        //mArrayListMutableLiveData.setValue(ActiveUsersRepo.getInstance().getActiveUsersList());
    }

    //Get single UserListProfile
    public MutableLiveData<UserListProfile> getProfileMutableLiveData() {
        return mProfileMutableLiveData;
    }
}
