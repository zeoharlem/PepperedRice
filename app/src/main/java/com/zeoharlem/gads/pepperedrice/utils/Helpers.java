package com.zeoharlem.gads.pepperedrice.utils;

import android.widget.EditText;

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

    public static final String API_ID       = "59146428466177992482";
    public static final String API_KEY      = "tkQ03VCcziNtG5qmrh1cKXKQDEEkqojUGnL7tYgW";
    public static final String URL_VARS     = "?apiId="+ API_ID+"&apiKey="+ API_KEY;

}
