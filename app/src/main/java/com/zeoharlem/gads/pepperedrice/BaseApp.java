package com.zeoharlem.gads.pepperedrice;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.zeoharlem.gads.pepperedrice.utils.L;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

public class BaseApp extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //forceTls();
    }

    private void forceTls(){
        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        }
        catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
