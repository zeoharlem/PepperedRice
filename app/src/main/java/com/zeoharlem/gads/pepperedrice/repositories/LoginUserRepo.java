package com.zeoharlem.gads.pepperedrice.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.zeoharlem.gads.pepperedrice.models.LoginUser;

public class LoginUserRepo {
    private FirebaseAuth mFirebaseAuth;
    private static LoginUserRepo instance;
    private boolean isLoggedIn  = false;
    private MutableLiveData<LoginUser> mLoginUserMutableLiveData;

    private LoginUserRepo(){
        mFirebaseAuth               = FirebaseAuth.getInstance();
        mLoginUserMutableLiveData   = new MutableLiveData<>();
    }

    public static LoginUserRepo getInstance(){
        if(instance == null){
            instance    = new LoginUserRepo();
        }
        return instance;
    }

    public void userloginStart(String email, String password, LoginUser loginUser){
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    isLoggedIn  = true;
                    loginUser.setUserId(task.getResult().getUser().getUid());
                    loginUser.setUsername(task.getResult().getUser().getEmail());
                    loginUser.setLoggedIn(isLoggedIn);
                }
                else{
                    Log.d("Repo", "false");
                    isLoggedIn  = false;
                    loginUser.setLoggedIn(isLoggedIn);
                }
            }
        });
        Log.d("Outer", new Gson().toJson(loginUser));
    }

    public MutableLiveData<LoginUser> getLoginUserMutableLiveData() {
        return mLoginUserMutableLiveData;
    }

    public void setLoginUserMutableLiveData(MutableLiveData<LoginUser> loginUserMutableLiveData) {
        mLoginUserMutableLiveData   = loginUserMutableLiveData;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
