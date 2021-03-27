package com.zeoharlem.gads.pepperedrice.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.zeoharlem.gads.pepperedrice.Network.MyVolleySingleton;
import com.zeoharlem.gads.pepperedrice.R;
import com.zeoharlem.gads.pepperedrice.adapter.ChatMessageAdapter;
import com.zeoharlem.gads.pepperedrice.databinding.ActivityChatBinding;
import com.zeoharlem.gads.pepperedrice.models.ChatMessage;
import com.zeoharlem.gads.pepperedrice.utils.Helpers;
import com.zeoharlem.gads.pepperedrice.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private static final int PERMISSION_CODE = 321;
    ActivityChatBinding mActivityChatBinding;
    ChatMessageAdapter mChatMessageAdapter;
    ArrayList<ChatMessage> mMessageArrayList;

    private String username, receiverUid, senderUid, senderRoom, receiverRoom;
    private String myUsername, myImageUrl, myPhoneNum;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private String profileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityChatBinding    = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(mActivityChatBinding.getRoot());

        setSupportActionBar(mActivityChatBinding.toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //setContentView(R.layout.activity_chat);
        mFirebaseAuth   = FirebaseAuth.getInstance();

        if(getIntent().hasExtra("chatId")){
            receiverUid = getIntent().getStringExtra("hisId");
            username    = getIntent().getStringExtra("myUsernameR");
            profileImg  = getIntent().getStringExtra("myImageUrlR");
        }
        else {
            receiverUid = getIntent().getStringExtra("uid");
            username    = getIntent().getStringExtra("username");
            profileImg  = getIntent().getStringExtra("imageUrl");
        }
        //My Properties
        myUsername  = getIntent().getStringExtra("myUsername");
        myImageUrl  = getIntent().getStringExtra("myImageUrl");
        myPhoneNum  = getIntent().getStringExtra("myPhoneNum");

        senderUid   = mFirebaseAuth.getUid();

        //L.l(getApplicationContext(), myUsername);

        //Create the chatRoom ID
        senderRoom      = senderUid + receiverUid;
        receiverRoom    = receiverUid + senderUid;

        mFirebaseDatabase   = FirebaseDatabase.getInstance();
        mFirebaseDatabase.getReference().child("presence").child(receiverUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String status   = snapshot.getValue(String.class);
                    if(!status.isEmpty()) {
                        mActivityChatBinding.typingStatus.setText(status);
                        mActivityChatBinding.typingStatus.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mMessageArrayList   = new ArrayList<>();

        //Set Values and Data for fetched Chats from Firebase Database
        setAdapterActions();
        getFirebaseChatData();

        getLatLong(receiverUid);

        mActivityChatBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        mActivityChatBinding.toolbarTitle.setText(username);
        Glide.with(ChatActivity.this).load(profileImg)
                .placeholder(R.drawable.avatar)
                .into(mActivityChatBinding.profileImage);

        //Send Message Actins
        sendPrivateChatMessage();

        textWatchingUserTyping();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.callMenuButton) {
            callPepperedLine("2348038596978");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentId    = FirebaseAuth.getInstance().getUid();
        mFirebaseDatabase.getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    protected void onStop() {
        String currentId    = FirebaseAuth.getInstance().getUid();
        mFirebaseDatabase.getReference().child("presence").child(currentId).setValue("Offline");
        super.onStop();
    }


    @Override
    protected void onPause() {
        super.onPause();
        String currentId    = FirebaseAuth.getInstance().getUid();
        mFirebaseDatabase.getReference().child("presence").child(currentId).setValue("Offline");
    }

    private void callPepperedLine(String phone){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
        }
        else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + phone)));
        }
    }

    //Send Message Method
    private void sendPrivateChatMessage(){
        mActivityChatBinding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date               = new Date();
                String messageText      = mActivityChatBinding.messageTextField.getText().toString();
                ChatMessage chatMessage = new ChatMessage(messageText, senderUid, date.getTime());
                mActivityChatBinding.messageTextField.setText("");
                String randomKey        = mFirebaseDatabase.getReference().push().getKey();
                mFirebaseDatabase.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(randomKey).setValue(chatMessage)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mFirebaseDatabase.getReference().child("chats")
                                        .child(receiverRoom)
                                        .child("messages")
                                        .child(randomKey).setValue(chatMessage)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        });
                            }
                        });

                getToken(messageText, receiverUid, profileImg, randomKey, username);
            }
        });
    }

    //Get Data from firebaseDatabase
    private void getFirebaseChatData(){
        mFirebaseDatabase.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mMessageArrayList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                            mMessageArrayList.add(chatMessage);
                        }
                        mChatMessageAdapter.setChatMessageArrayList(mMessageArrayList);
                        mChatMessageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    //Setup Adapter
    private void setAdapterActions(){
        mChatMessageAdapter = new ChatMessageAdapter(this, mMessageArrayList);
        mActivityChatBinding.chatRecyclerView.setAdapter(mChatMessageAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    //TextWatching if user is typing
    private void textWatchingUserTyping(){
        Handler handler = new Handler();
        mActivityChatBinding.messageTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mFirebaseDatabase.getReference().child("presence").child(senderUid).setValue("typing...");
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(userStoppedTyping, 2000);
            }

            Runnable userStoppedTyping  = new Runnable() {
                @Override
                public void run() {
                    mFirebaseDatabase.getReference().child("presence").child(senderUid).setValue("Online");
                }
            };
        });
    }

    private void getToken(String message, String hisID, String hisImage, String chatID, String username){
        DatabaseReference reference = mFirebaseDatabase.getReference("UserProfile").child(hisID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String token    = snapshot.child("token").getValue().toString();
                String name     = snapshot.child("name").getValue().toString();
                JSONObject to   = new JSONObject();
                JSONObject data         = new JSONObject();
                try {
                    data.put("message", message);
                    data.put("hisImage", hisImage);
                    data.put("hisId", senderUid);
                    data.put("chatId", chatID);
                    data.put("title", name);
                    //My Properties for
                    data.put("myUsername", myUsername);
                    data.put("myImageUrl", myImageUrl);
                    data.put("myPhoneNum", myPhoneNum);

                    to.put("to", token);
                    to.put("data", data);

                    sendNotification(to);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotification(JSONObject to) {
        Log.d(TAG, "sendNotification: called");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Helpers.NOTIFICATION_URL, to, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "error: " + error.getMessage());
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization","key="+Helpers.SERVER_KEY);
                headers.put("Content-Type", "application/json;");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        jsonObjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        MyVolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest,"chatNotify");
    }

    private void getLatLong(String userkey){
        Geocoder geocoder       = new Geocoder(this, Locale.getDefault());
        mFirebaseDatabase.getReference("Active MobileOutlets").child(userkey).child("l").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    double locLat   = 0;
                    double locLng   = 0;
                    List<Object> currentUserKeyActs = (List<Object>) snapshot.getValue();
                    if(currentUserKeyActs.get(0) != null){
                        locLat  = Double.parseDouble(currentUserKeyActs.get(0).toString());
                    }
                    if(currentUserKeyActs.get(1) != null){
                        locLng  = Double.parseDouble(currentUserKeyActs.get(0).toString());
                    }
                    StringBuilder fullAddr  = new StringBuilder();
                    try {
                        List<Address> addresses = geocoder.getFromLocation(locLat, locLng, 1);
                        if (addresses != null && addresses.size() > 0) {
                            String address  = addresses.get(0).getAddressLine(0);
                            String city     = " " + addresses.get(0).getLocality();
                            String state    = " " + addresses.get(0).getAdminArea();
                            String country  = " " + addresses.get(0).getCountryName();
                            String addStr   = fullAddr.append(address).append(city).append(state).append(country).toString();
                            //L.l(getApplicationContext(), addStr);
                            mActivityChatBinding.locationAddress.setText(addStr);
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getTokenFireService(){
        final String[] firebaseToken = {""};
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    firebaseToken[0] = task.getResult();
                }
            }
        });
        return firebaseToken[0];
    }

    //Use for inapp-messaging
    private void sendMessageAction(String topic, String message){
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }
            }
        });
    }
}