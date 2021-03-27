package com.zeoharlem.gads.pepperedrice.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zeoharlem.gads.pepperedrice.DashboardActivity;
import com.zeoharlem.gads.pepperedrice.R;
import com.zeoharlem.gads.pepperedrice.databinding.ActivitySetupProfileBinding;
import com.zeoharlem.gads.pepperedrice.models.UserProfile;
import com.zeoharlem.gads.pepperedrice.utils.L;
import com.zeoharlem.gads.pepperedrice.viewmodels.LoginViewModel;

public class SetupProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 45;
    private ActivitySetupProfileBinding mActivitySetupProfileBinding;
    private LoginViewModel mLoginViewModel;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseUser mFirebaseOwner;

    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySetupProfileBinding    = ActivitySetupProfileBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_setup_profile);
        setContentView(mActivitySetupProfileBinding.getRoot());
        setSupportActionBar(mActivitySetupProfileBinding.toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mLoginViewModel     = new ViewModelProvider(this).get(LoginViewModel.class);
        mFirebaseOwner      = mLoginViewModel.getFirebaseAuth().getCurrentUser();
        mFirebaseDatabase   = FirebaseDatabase.getInstance();
        mFirebaseStorage    = FirebaseStorage.getInstance();

        mActivitySetupProfileBinding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"), REQUEST_CODE);
            }
        });

        mActivitySetupProfileBinding.createProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name     = mActivitySetupProfileBinding.fullname.getText().toString();
                String phone    = mActivitySetupProfileBinding.phoneNumber.getText().toString();

                Button clickBtn = (Button)view;
                clickBtn.setText("Please Wait...");
                clickBtn.setEnabled(false);

                if(name.isEmpty() && phone.isEmpty()){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clickBtn.setEnabled(true);
                            clickBtn.setText("Create Profile");
                        }
                    }, 2000);
                    L.l(SetupProfileActivity.this, "One of the Fields is empty");
                }
                if(selectedImageUri != null){
                    StorageReference reference  = mFirebaseStorage.getReference().child("Profiles").child(mLoginViewModel.getFirebaseAuth().getUid());

                    reference.putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                            @Override
                                            public void onComplete(@NonNull Task<String> task) {
                                                if(task.isSuccessful()){
                                                    String imageUrl         = uri.toString();
                                                    String fullname         = mActivitySetupProfileBinding.fullname.getText().toString();
                                                    String phoneNumber      = mActivitySetupProfileBinding.phoneNumber.getText().toString();
                                                    String uid              = mLoginViewModel.getFirebaseAuth().getUid();
                                                    String token            = task.getResult();
                                                    String userType         = "client";

                                                    UserProfile userProfile = new UserProfile(uid, fullname, phoneNumber, imageUrl, userType, token);
                                                    mFirebaseDatabase.getReference().child("UserProfile").child(uid).setValue(userProfile)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    new Handler().postDelayed(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            clickBtn.setText("Create Profile");
                                                                            L.l(SetupProfileActivity.this, "Successfully Created Profile");
                                                                            startActivity(new Intent(SetupProfileActivity.this, DashboardActivity.class));
                                                                            finish();
                                                                        }
                                                                    }, 2000);

                                                                }
                                                            });
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
                else{
                    clickBtn.setEnabled(true);
                    clickBtn.setText("Create Profile");
                    L.l(getApplicationContext(), "You have not provided profile image");
                }
            }
        });

        mActivitySetupProfileBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null && data.getData() != null){
            mActivitySetupProfileBinding.profileImage.setImageURI(data.getData());
            selectedImageUri    = data.getData();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.profile_menu, menu);
//        MenuItem menuItem   = menu.findItem(R.id.active_users_view);
//        View actionView     = menuItem.getActionView();
//
//        actionView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onOptionsItemSelected(menuItem);
//            }
//        });
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){

        }
        //return super.onOptionsItemSelected(item);
        return true;
    }
}