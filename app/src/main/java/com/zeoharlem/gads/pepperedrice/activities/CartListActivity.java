package com.zeoharlem.gads.pepperedrice.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.MessageFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.gson.Gson;
import com.zeoharlem.gads.pepperedrice.BaseApp;
import com.zeoharlem.gads.pepperedrice.R;
import com.zeoharlem.gads.pepperedrice.adapter.CartListAdapter;
import com.zeoharlem.gads.pepperedrice.databinding.ActivityCartListBinding;
import com.zeoharlem.gads.pepperedrice.dialog.CheckOutBottomSheet;
import com.zeoharlem.gads.pepperedrice.models.CartItem;
import com.zeoharlem.gads.pepperedrice.utils.L;
import com.zeoharlem.gads.pepperedrice.viewmodels.MenuFoodListViewModel;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import co.paystack.android.PaystackSdk;

public class CartListActivity extends BaseApp implements CartListAdapter.CartListListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final long UPDATE_INTERVAL = 5000;
    private static final long UPDATE_FAST_INTERVAL = 5000;

    private GoogleApiClient mGoogleApiClient;
    private ActivityCartListBinding mActivityCartListBinding;
    private static final String tag = "CartListActivity";
    private MenuFoodListViewModel mFoodListViewModel;
    private CartListAdapter cartListAdapter;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private String getLocationString;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private static final int PERMISSION_CODE = 44;
    private String deliveryService  = "No";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityCartListBinding = DataBindingUtil.setContentView(this, R.layout.activity_cart_list);
        mActivityCartListBinding = ActivityCartListBinding.inflate(getLayoutInflater());
        setContentView(mActivityCartListBinding.getRoot());
        setSupportActionBar(mActivityCartListBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setup GoogleClient
        client  = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();


        mActivityCartListBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        setRecyclerViewAdapterRow();
        cartListAdapter.setCartListListener(this);

        mFoodListViewModel = new ViewModelProvider(this).get(MenuFoodListViewModel.class);

        mFoodListViewModel.getCartItemList().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                cartListAdapter.submitList(cartItems);
                mActivityCartListBinding.placeOrderButton.setEnabled(cartItems.size() > 0);
                mActivityCartListBinding.deliveryService.setEnabled(cartItems.size() > 0);
                //mActivityCartListBinding.locationDesc.setEnabled(cartItems.size() > 0);
            }
        });

        mFoodListViewModel.getCartTotalPrice().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                mActivityCartListBinding.orderTotalPrice.setText("Total: N" + aDouble.toString());
            }
        });

        inits();

        //createFusedLocationClient();
        checkLocationGpsIsEnabled();
        getLocation();

        mActivityCartListBinding.placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckOutBottomSheet checkOutBottomSheet = new CheckOutBottomSheet();
                Bundle arguments = new Bundle();
                arguments.putString("deliveryService", deliveryService);
                arguments.putString("currentAddress", getLocationString);
                checkOutBottomSheet.setArguments(arguments);
                checkOutBottomSheet.show(getSupportFragmentManager(), "checkOutFrag");
            }
        });
    }

    private void inits(){
        //Initialize Paystack
        PaystackSdk.initialize(this);
        //setApiKeyGooglePlacesTextView();
    }

    private void getLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
            return;
        }
        mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    //L.l(getApplicationContext(), "location: "+new Gson().toJson(location));
                    Geocoder geocoder = new Geocoder(CartListActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        getLocationString       = getValuesForLocation(addresses);
                        L.l(getApplicationContext(), getLocationString);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        L.l(getApplicationContext(), "Geocoder start error: "+e.getMessage());
                    }
                }
                else{
                    LocationRequest locationRequest = new LocationRequest()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(UPDATE_INTERVAL)
                            .setFastestInterval(UPDATE_FAST_INTERVAL)
                            .setNumUpdates(1);

                    LocationCallback callback   = new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            Location location1  = locationResult.getLastLocation();
                            Geocoder geocoder   = new Geocoder(CartListActivity.this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(location1.getLatitude(), location1.getLongitude(), 1);
                                getLocationString   = getValuesForLocation(addresses);
                                L.l(getApplicationContext(), getLocationString);
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                                L.l(getApplicationContext(), "Geocoder recalled error: "+e.getMessage());
                            }
                        }
                    };

                    mFusedLocationProviderClient.requestLocationUpdates(locationRequest, callback, Looper.myLooper());
                }
            }
        });
    }

    private void checkLocationGpsIsEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean gpsEnabled              = false;
        boolean networkEnabled          = false;
        gpsEnabled  = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkEnabled  = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(!gpsEnabled && !networkEnabled){
            new AlertDialog.Builder(this)
                    .setTitle("Enable GPS Service")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(CartListActivity.this, Locale.getDefault());
                        //geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1)
                    }
                    else{
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(UPDATE_INTERVAL)
                                .setFastestInterval(UPDATE_FAST_INTERVAL)
                                .setNumUpdates(1);
                        LocationCallback callback   = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                Location location1  = locationResult.getLastLocation();
                            }
                        };

                        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, callback, Looper.myLooper());
                    }
                }
            });
        }
        else{
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void setPermissionRequests(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }
    }

    private void createFusedLocationClient() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            L.l(getApplicationContext(), "Create Fused");

            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        //Set TextView or EditText
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            L.l(getApplicationContext(), MessageFormat.format("Lat: {0} Long: {1}", location.getLatitude(), location.getLongitude()));
                        }
                        else{
                            L.l(getApplicationContext(), location.getLatitude() + "|long:"+location.getLongitude());
                        }
                    }
                }
            });
            startLocationUpdates();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(UPDATE_FAST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            L.l(getApplicationContext(), "You need to enable permission to display location");
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if(locationResult != null){
                    Location location   = locationResult.getLastLocation();
                    //Set textView or EditText
                    L.l(getApplicationContext(), location.getLatitude() + "|long:"+location.getLongitude());
                }
            }

            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        }, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CODE){
            if(grantResults.length > 0 && (grantResults[0] + grantResults[1]) == PackageManager.PERMISSION_GRANTED){
                if(client != null){
                    client.connect();
                }
                //getCurrentLocation();
                getLocation();
            }
            else{
                L.l(getApplicationContext(), "Permission Denied...");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(client != null){
            client.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        client.disconnect();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        checkPlayServices();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(client != null && client.isConnected()){
            LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                }

                @Override
                public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                    super.onLocationAvailability(locationAvailability);
                }
            });
            client.disconnect();
        }
    }

    private void checkPlayServices(){
        int errorCode   = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if(errorCode != ConnectionResult.SUCCESS){
            Dialog errorDialog  = GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, errorCode, errorCode, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            L.l(getApplicationContext(), "No Location Serivces");
                            finish();
                        }
                    });
            errorDialog.show();
        }
        else{
            L.l(getApplicationContext(), "Location Services active");
        }
    }

    private void setRecyclerViewAdapterRow(){
        cartListAdapter             = new CartListAdapter();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),  DividerItemDecoration.VERTICAL);
        mActivityCartListBinding.cartListRecyclerView.addItemDecoration(dividerItemDecoration);
        mActivityCartListBinding.cartListRecyclerView.setAdapter(cartListAdapter);
    }

    @Override
    public void deleteItem(CartItem cartItem) {
        mFoodListViewModel.removeItemFromCart(cartItem);
    }

    @Override
    public void changeQuantity(CartItem cartItem, int quantity) {
        mFoodListViewModel.changeQuantity(cartItem, quantity);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(location != null){
            L.l(getApplicationContext(), location.getLatitude() + "|long:"+location.getLongitude());
        }
    }

    private String getValuesForLocation(List<Address> addresses){
        String address          = addresses.get(0).getAddressLine(0);
        String area             = " " + addresses.get(0).getLocality();
        String city             = " " + addresses.get(0).getAdminArea();
        String country          = " " + addresses.get(0).getCountryName();
        StringBuilder fullAddr  = new StringBuilder();

        return fullAddr.append(address).append(area).append(city).append(country).toString();
    }
}