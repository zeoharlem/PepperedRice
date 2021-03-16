package com.zeoharlem.gads.pepperedrice.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.zeoharlem.gads.pepperedrice.models.LoginUser;
import com.zeoharlem.gads.pepperedrice.repositories.LoginUserRepo;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<String> username                        = new MutableLiveData<>();
    private MutableLiveData<LoginUser> mLoginUserMutableLiveData    = new MutableLiveData<>();
    private LoginUser mLoginUser    = new LoginUser();

    public LiveData<LoginUser> getLoginUserMutableLiveData() {
        return mLoginUserMutableLiveData;
    }

    public void getLoginUserLiveData(String email, String password){
        LoginUserRepo.getInstance().userloginStart(email, password, mLoginUser);
        Log.d("ViewModel", new Gson().toJson(mLoginUser));
        mLoginUserMutableLiveData.setValue(mLoginUser);
    }

    public LoginUser getLoginUser() {
        return mLoginUser;
    }
}
