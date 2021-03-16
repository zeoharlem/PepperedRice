package com.zeoharlem.gads.pepperedrice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.zeoharlem.gads.pepperedrice.databinding.ActivityRegisterBinding;
import com.zeoharlem.gads.pepperedrice.utils.L;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding mActivityRegisterBinding;
    private FirebaseAuth mFirebaseAuth;
    private ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityRegisterBinding    = DataBindingUtil.setContentView(this, R.layout.activity_register);

        mFirebaseAuth               = FirebaseAuth.getInstance();

        mActivityRegisterBinding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(
                        mActivityRegisterBinding.loginEmailid.getText().toString(),
                        mActivityRegisterBinding.loginPassword.getText().toString()
                );
            }
        });

        mProgressBar                = new ProgressDialog(this);
    }

    private void registerUser(String email, String password){

        if(TextUtils.isEmpty(email)){
            L.l(getApplicationContext(), "Empty Email Address");
        }
        else if(TextUtils.isEmpty(password)){
            L.l(getApplicationContext(), "Empty Password Address");
        }
        else{
            mProgressBar.setTitle("Registration");
            mProgressBar.setMessage("Please wait! the process is on");
            mProgressBar.show();
            mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        L.l(getApplicationContext(), "Successfully Registered");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mProgressBar.dismiss();
                                Intent intent   = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.putExtra("emailAddress", email);
                                startActivity(intent);
                            }
                        }, 2000);
                    }
                    else{
                        L.l(getApplicationContext(), "Unable to Register "+task.getException().getMessage());
                        mProgressBar.dismiss();
                    }
                }
            });
        }
    }
}