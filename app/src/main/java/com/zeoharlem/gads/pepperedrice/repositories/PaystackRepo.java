package com.zeoharlem.gads.pepperedrice.repositories;

import android.content.Context;

public class PaystackRepo {
    private Context mContext;
    private static PaystackRepo instance;

    private PaystackRepo(Context context){
        mContext    = context;
    }
}
