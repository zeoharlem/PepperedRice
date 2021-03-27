package com.zeoharlem.gads.pepperedrice.viewmodels;

import android.app.Application;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.zeoharlem.gads.pepperedrice.R;
import com.zeoharlem.gads.pepperedrice.models.LoginUser;
import com.zeoharlem.gads.pepperedrice.repositories.LoginUserRepo;
import com.zeoharlem.gads.pepperedrice.utils.L;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<LoginUser> mLoginUserMutableLiveData;
    private MutableLiveData<String> username    = new MutableLiveData<>();
    private MutableLiveData<String> password    = new MutableLiveData<>();
    private LoginUser mLoginUser                = new LoginUser();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<LoginUser> getLoginUserMutableLiveData() {
        if(mLoginUserMutableLiveData == null){
            mLoginUserMutableLiveData   = new MutableLiveData<>();
        }
        return mLoginUserMutableLiveData;
    }

    public void getLoginUserLiveData(View view){
        mLoginUser.setUsername(username.getValue());
        mLoginUser.setPassword(password.getValue());

        if(mLoginUser.isEmailValid() && mLoginUser.isEmptyPassword()){
            LoginUserRepo.getInstance().userLoginByUsingFirebase(username.getValue(), password.getValue(), new LoginUserRepo.iUserLoginCallbackListener() {
                @Override
                public void onSuccess(AuthResult firebaseUser) {
                    mLoginUser.setLoggedIn(true);
                    mLoginUser.setUserId(firebaseUser.getUser().getUid());
                    mLoginUser.setMessage("You have successfully logged in");
                    mLoginUserMutableLiveData.setValue(mLoginUser);
                }

                @Override
                public void onFailure(String message) {
                    view.setEnabled(true);
                    Button button   = (Button) view;
                    button.setText("Login Now");

                    ProgressBar progressBar = view.getRootView().findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.GONE);

                    mLoginUser.setLoggedIn(false);
                    mLoginUser.setMessage(message);
                    mLoginUserMutableLiveData.setValue(mLoginUser);
                }
            });
        }
        else{
            L.l(getApplication(), "Check the Login fields for empty or incorrect field format");
        }
    }

    public LoginUser getLoginUser() {
        return mLoginUser;
    }

    public MutableLiveData<String> getUsername() {
        return username;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public FirebaseAuth getFirebaseAuth(){
        return LoginUserRepo.getInstance().getFirebaseAuth();
    }
}
