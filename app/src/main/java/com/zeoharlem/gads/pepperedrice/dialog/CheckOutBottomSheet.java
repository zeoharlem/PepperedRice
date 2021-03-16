package com.zeoharlem.gads.pepperedrice.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zeoharlem.gads.pepperedrice.activities.CheckOutActivity;
import com.zeoharlem.gads.pepperedrice.databinding.CheckOutBottomDialogSheetBinding;
import com.zeoharlem.gads.pepperedrice.models.PaystackTransaction;
import com.zeoharlem.gads.pepperedrice.utils.Helpers;
import com.zeoharlem.gads.pepperedrice.utils.L;
import com.zeoharlem.gads.pepperedrice.viewmodels.MenuFoodListViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class CheckOutBottomSheet extends BottomSheetDialogFragment {
    private String fullname, address, email, totalPrice = "0";
    private String phone, cv, expiryMonth, expiryYear, delivery;
    private CheckOutBottomDialogSheetBinding mCheckOutBottomDialogSheetBinding;
    private MenuFoodListViewModel mMenuFoodListViewModel;
    private String cardNumber;
    private Charge charge;
    private ProgressBar dialog;
    Transaction mTransaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCheckOutBottomDialogSheetBinding   = CheckOutBottomDialogSheetBinding.inflate(inflater, container, false);
        Bundle arguments                    = getArguments();
        String addrString                   = arguments.getString("currentAddress", "");
        //L.l(getContext(), addrString);
        mCheckOutBottomDialogSheetBinding.locationAddress.setText(addrString);
        return mCheckOutBottomDialogSheetBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMenuFoodListViewModel  = new ViewModelProvider(requireActivity()).get(MenuFoodListViewModel.class);
        mMenuFoodListViewModel.getCartTotalPrice().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                totalPrice  = String.valueOf(aDouble.intValue() * 100);
                mCheckOutBottomDialogSheetBinding.paypalTotalAmount.setText("Total: N" + aDouble.toString());
            }
        });
        mMenuFoodListViewModel  = new ViewModelProvider(requireActivity()).get(MenuFoodListViewModel.class);

        //Payments Parameters set
        //Make Payments
        mCheckOutBottomDialogSheetBinding.payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPaymentParams();
                pay();
            }
        });

    }

    private void setPaymentParams(){
        fullname    = mCheckOutBottomDialogSheetBinding.fullname.getText().toString();
        email       = mCheckOutBottomDialogSheetBinding.emailAddress.getText().toString();
        phone       = mCheckOutBottomDialogSheetBinding.phoneNumber.getText().toString();
        address     = mCheckOutBottomDialogSheetBinding.locationAddress.getText().toString();
        cardNumber  = mCheckOutBottomDialogSheetBinding.cardNumber.getText().toString();
        expiryMonth = mCheckOutBottomDialogSheetBinding.month.getText().toString();
        cv          = mCheckOutBottomDialogSheetBinding.cvc.getText().toString();
        expiryYear  = mCheckOutBottomDialogSheetBinding.year.getText().toString();
        L.l(getContext(), totalPrice);
    }

    private void pay(){
        mCheckOutBottomDialogSheetBinding.payButton.setEnabled(false);
        mCheckOutBottomDialogSheetBinding.payButton.setText("Please Wait. Checking...");
        Card card   = new Card(cardNumber, Integer.parseInt(expiryMonth), Integer.parseInt(expiryYear), cv);
        if(card.isValid()){
            //Charge Card
            charge  = new Charge();
            charge.setCard(card);

            createProgressBar();
            dialog.setVisibility(View.VISIBLE);

            charge.setEmail(email);
            charge.setAmount(Integer.parseInt(totalPrice));
            charge.setReference("ChargedPRApp" + Calendar.getInstance().getTimeInMillis());
            try {
                charge.putCustomField("phone", phone);
                charge.putCustomField("fullname", fullname);
                charge.putCustomField("address", address);
            }
            catch (JSONException e) {
                e.printStackTrace();
                L.l(getContext(), "CustomField Error: " + e.getMessage());
            }
            performCardCharge();
        }
        else{
            L.l(getContext(), "Invalid Card Details Provided");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCheckOutBottomDialogSheetBinding.payButton.setEnabled(true);
                    mCheckOutBottomDialogSheetBinding.payButton.setText("Make Payment");
                }
            }, 3000);

        }
    }

    private void performCardCharge(){
        PaystackSdk.chargeCard(getActivity(), charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                mTransaction    = transaction;
                L.l(getContext(), "ref: "+mTransaction.getReference());
                mCheckOutBottomDialogSheetBinding.payButton.setEnabled(true);
                mCheckOutBottomDialogSheetBinding.payButton.setText("Make Payment");
                Intent intent   = new Intent(getActivity(), CheckOutActivity.class);

                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                intent.putExtra("address", address);
                intent.putExtra("totalPrice", totalPrice);
                intent.putExtra("fullname", fullname);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                mTransaction    = transaction;
                L.l(getContext(), "ref: "+mTransaction.getReference());
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                mTransaction    = transaction;
                if (error instanceof ExpiredAccessCodeException) {
                    performCardCharge();
                    return;
                }
                if (transaction.getReference() != null) {
                    L.l(getContext(), transaction.getReference() + " concluded with error: " + error.getMessage());
                    verifyOnServer(transaction.getReference(), new iPaystackListener() {
                        @Override
                        public void onSuccess(PaystackTransaction paystackTransaction) {
                            mCheckOutBottomDialogSheetBinding.payButton.setText("Make Payment");
                            mCheckOutBottomDialogSheetBinding.payButton.setEnabled(true);
                        }
                    });
                    //new verifyOnServer().execute(transaction.getReference());
                }
                else {
                    L.l(getContext(), error.getMessage());
                }
            }
        });
    }

    private void verifyOnServer(String reference, iPaystackListener paystackListener){
        String url  = Helpers.URL_STRING + "verify/"+reference + Helpers.URL_VARS+"&reference="+reference;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject   = new JSONObject(response);
                    if(jsonObject.getBoolean("status")){
                        JSONObject dataflow = jsonObject.getJSONObject("data");
                        paystackListener.onSuccess(parseJson(dataflow));
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    L.l(getContext(), e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.l(getContext(), error.getMessage());
            }
        });
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        50000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
    }

    private PaystackTransaction parseJson(JSONObject response) throws JSONException {
        PaystackTransaction paystackTransaction = new PaystackTransaction();
        JSONObject dataObjectResponse           = response.getJSONObject("data");
        paystackTransaction.setAmount(dataObjectResponse.getString("amount"));
        paystackTransaction.setReference(dataObjectResponse.getString("reference"));
        paystackTransaction.setTransactionDate(dataObjectResponse.getString("transaction_date"));
        paystackTransaction.setStatus(dataObjectResponse.getString("status"));
        return paystackTransaction;
    }

    private void createProgressBar(){
        LinearLayout layout = mCheckOutBottomDialogSheetBinding.checkoutBody;
        dialog = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(dialog, params);
    }

    interface iPaystackListener{
        void onSuccess(PaystackTransaction paystackTransaction);
    }

}
