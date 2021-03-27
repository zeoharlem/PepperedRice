package com.zeoharlem.gads.pepperedrice.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static SessionManager instance;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;

    private SessionManager(Context context) {
        mSharedPreferences  = context.getSharedPreferences("PR_Key_manager", Context.MODE_PRIVATE);
        mEditor             = mSharedPreferences.edit();
        mEditor.apply();
    }

    public static SessionManager getInstance(Context context){
        if (instance == null) {
            instance    = new SessionManager(context);
        }
        return instance;
    }

    public void setIsLoggedIn(boolean login){
        mEditor.putBoolean("key_login", login);
        mEditor.apply();
    }

    public boolean getLogin(){
        return mSharedPreferences.getBoolean("key_login", false);
    }

    public void setUsername(String username){
        mEditor.putString("key_username", username);
        mEditor.apply();
    }

    public String getUsername(){
        return mSharedPreferences.getString("key_username", "");
    }

    public void destroySession(){
        mEditor.clear();
    }
}
