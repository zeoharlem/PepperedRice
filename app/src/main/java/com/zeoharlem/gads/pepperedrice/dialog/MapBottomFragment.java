package com.zeoharlem.gads.pepperedrice.dialog;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zeoharlem.gads.pepperedrice.R;
import com.zeoharlem.gads.pepperedrice.databinding.FragmentMapBottomBinding;
import com.zeoharlem.gads.pepperedrice.utils.L;

import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;

public class MapBottomFragment extends BottomSheetDialogFragment {

    FragmentMapBottomBinding mBottomBinding;
    private static final int PERMISSION_CODE = 321;

    public MapBottomFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBottomBinding  = FragmentMapBottomBinding.inflate(inflater, container, false);
        return mBottomBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        if((getArguments() != null)){
            String moUid    = getArguments().getString("mouid");
            String address  = getArguments().getString("address");
            //L.l(getContext(), moUid);
            FirebaseDatabase.getInstance().getReference("UserProfile").child(moUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String fullname = snapshot.child("name").getValue().toString();
                        String phone    = snapshot.child("phoneNumber").getValue().toString();
                        mBottomBinding.fullname.setText(fullname);
                        mBottomBinding.phoneNumber.setText(phone);
                        mBottomBinding.locationAddress.setText(address);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            mBottomBinding.callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    L.l(getContext(), "Call Button Clicked");
                    callPepperedLine("2348038596978");
                }
            });

            mBottomBinding.whatsAppButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    L.l(getContext(), "WhatsApp");
                    String message  = "Hi Peppered Rice!";
                    if(isPackageExisted("com.whatsapp")) {
                        openWhatsAppMessageApp("2348038596978", message);
                    }
                    else{
                        openWhatsBussinessApp("2348038596978", message);
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    callPepperedLine("2348038596978");
                }
                break;

            default:
                break;
        }
    }

    //if whatsapp buzz or whatappMsg is intalled
    private boolean isPackageExisted(String targetPackage){
        PackageManager pm       = getContext().getPackageManager();
        try {
            PackageInfo info    = pm.getPackageInfo(targetPackage,PackageManager.GET_META_DATA);
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    private void navigateIntent(Class navigate){
        Intent intent   = new Intent(getContext(), navigate);
        startActivity(intent);
    }

    //WhatsApp Messenger App
    private void openWhatsAppMessageApp(String phone, String message){
        PackageManager packageManager   = getContext().getPackageManager();
        Intent i                        = new Intent(Intent.ACTION_VIEW);

        try {
            String url  = "https://api.whatsapp.com/send?phone="+ phone +"&text=" + URLEncoder.encode(message, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                getContext().startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //Buniess WhatsAppBuzzAPp
    private void openWhatsBussinessApp(String phone, String message){
        PackageManager packageManager   = getContext().getPackageManager();
        Intent i                        = new Intent(Intent.ACTION_VIEW);

        try {
            String url  = "https://api.whatsapp.com/send?phone="+ phone +"&text=" + URLEncoder.encode(message, "UTF-8");
            i.setPackage("com.whatsapp.w4b");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                getContext().startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void makePhoneCalls(String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        getContext().startActivity(intent);
    }

    private void callPepperedLine(String phone){
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
        }
        else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + phone)));
        }
    }

}