package com.darshan09200.products.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.darshan09200.products.R;
import com.darshan09200.products.databinding.ActivityMapsBinding;
import com.darshan09200.products.helper.CurrentProductHelper;
import com.darshan09200.products.model.Product;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private static final float ZOOM = 15;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    FusedLocationProviderClient mClient;

    LatLng userLocation;

    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getLocationPermission();

        mClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        binding.saveLocation.setOnClickListener(v -> {
            if(marker != null) {
                Product product = CurrentProductHelper.instance.getProduct();
                if (product == null) {
                    product = new Product();
                }
                product.setCoordinate(marker.getPosition());
                CurrentProductHelper.instance.setProduct(product);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setOnMapLongClickListener(latLng -> {
            if (marker != null) mMap.clear();
            addMarker(latLng, "Selected Location", null);
        });

        googleMap.setOnMyLocationButtonClickListener(() -> {
            userLocation = null;
            enableGPS();
            return true;
        });

        googleMap.setOnMarkerDragListener(MapsActivity.this);

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);


        Product product = CurrentProductHelper.instance.getProduct();
        if (product != null) {
            userLocation = product.getCoordinate();
            addMarker(product.getCoordinate(), "Selected Product", null);
            zoomAt(product.getCoordinate());
        }

        onPermissionGranted();
    }

    public void addMarker(LatLng latLng, String title, String snippet) {
        MarkerOptions options = new MarkerOptions().position(latLng).title(title).draggable(true);
        if (snippet != null) {
            options = options.snippet(snippet);
        }

        if (mMap != null) {
            marker = mMap.addMarker(options);
        }
    }

    public void zoomAt(LatLng latLng) {
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM));
        }
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }

    void onPermissionGranted() {
        if (mMap != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);


            mClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                @NonNull
                @Override
                public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                    return null;
                }

                @Override
                public boolean isCancellationRequested() {
                    return false;
                }
            }).addOnSuccessListener(this, location -> {
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if (userLocation == null) zoomAt(latLng);
                    userLocation = latLng;
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private boolean isCheckingGps = false;
    ActivityResultLauncher<IntentSenderRequest> gpsActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),
            result -> {
                if (result.getResultCode() == MapsActivity.RESULT_OK) {
                    userLocation = null;
                    onPermissionGranted();
                }
                isCheckingGps = false;
            });

    private void enableGPS() {
        if (!isCheckingGps) {
            isCheckingGps = true;
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(5 * 1000);
            locationRequest.setFastestInterval(3 * 1000);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            Task<LocationSettingsResponse> result =
                    LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

            result.addOnCompleteListener(task -> {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    onPermissionGranted();
                    isCheckingGps = false;

                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                gpsActivityResult.launch(new IntentSenderRequest.Builder(
                                        resolvable.getResolution().getIntentSender()
                                ).setFillInIntent(new Intent())
                                        .build());
                            } catch (ClassCastException e) {
                                isCheckingGps = false;
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            isCheckingGps = false;
                            break;
                    }
                }
            });
        }
    }
}