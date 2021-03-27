package com.zeoharlem.gads.pepperedrice.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    public void userLoginByUsingFirebase(String email, String password, iUserLoginCallbackListener callbackListener){
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    callbackListener.onSuccess(task.getResult());
                }
                else{
                    callbackListener.onFailure(task.getException().getMessage());
                }
            }
        });
    }

    public FirebaseAuth getFirebaseAuth() {
        return mFirebaseAuth;
    }

    public interface iUserLoginCallbackListener{
        void onSuccess(AuthResult firebaseUser);
        void onFailure(String message);
    }

}
