package com.zeoharlem.gads.pepperedrice.utils;

import android.graphics.drawable.Drawable;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.zeoharlem.gads.pepperedrice.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Theophilus on 10/28/2017.
 */

public class Helpers {
    //Email Validation pattern
    public static final String regEx = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";

    //public static final String URL_STRING   = "https://artdeals.ng/ndevapi";
    public static final String URL_STRING   = "http://localhost/pepapi/";
    public static final String CHANNEL_ID   = "MyPepperedRiceFirebase";
    public static final String API_ID       = "59146428466177992482";
    public static final String API_KEY      = "tkQ03VCcziNtG5qmrh1cKXKQDEEkqojUGnL7tYgW";
    public static final String URL_VARS     = "?apiId="+ API_ID+"&apiKey="+ API_KEY;
    public static final String NOTIFICATION_URL   = "https://fcm.googleapis.com/fcm/send";
    public static final String SERVER_KEY   = "AAAAUYNb9RY:APA91bFGy9jrM0Vafh2B2QCmVcrgkZnbWVYs8UpYQ_jVSsbfnHNSnJaiZy4lZpAkWctlyCYrfynCIZ17YexxTNyRflt-Y8_9tWbKpsdzFM6NjJs67RMFBHGBzYyqQ3pRjU-HlY_NdFYo";
    public static final String UID          = "FoRpUQwjvRZJYfEjVhm6iCbxTUg1";
    public static final String senderId     = "350096192790";

}
