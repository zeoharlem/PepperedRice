package com.zeoharlem.gads.pepperedrice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.zeoharlem.gads.pepperedrice.databinding.ActivityLoginBinding;
import com.zeoharlem.gads.pepperedrice.models.LoginUser;
import com.zeoharlem.gads.pepperedrice.utils.L;
import com.zeoharlem.gads.pepperedrice.viewmodels.LoginViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding mActivityLoginBinding;
    private ProgressDialog mProgressBar;
    private FirebaseAuth mFirebaseAuth;
    private LoginViewModel mLoginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLoginBinding   = DataBindingUtil.setContentView(this, R.layout.activity_login);

        mFirebaseAuth           = FirebaseAuth.getInstance();

        mActivityLoginBinding.redirectRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent   = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        mProgressBar    = new ProgressDialog(this);

        mActivityLoginBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser(
                        mActivityLoginBinding.loginEmailid.getText().toString(),
                        mActivityLoginBinding.loginPassword.getText().toString()
                );
            }
        });

    }

    private void signInUser(String email, String password){
        mProgressBar.setTitle("Login");
        mProgressBar.setMessage("Please wait! the process is on");
        mProgressBar.show();
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mProgressBar.dismiss();
                    L.l(getApplicationContext(), "Login Successful");
                    Intent intent   = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                }
                else{
                    mProgressBar.dismiss();
                    L.l(getApplicationContext(), "Incorrect Username and Password");
                }
            }
        });
    }

}