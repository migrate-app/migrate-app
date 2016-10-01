package com.dankideacentral.dic;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.dankideacentral.dic.TweetListFragment.OnListFragmentInteractionListener;
import com.dankideacentral.dic.dummy.DummyContent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, OnListFragmentInteractionListener {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    // TODO: Fix problem reading the .properties file
    private String CONSUMER_KEY = "vLENiRXCWbppil87JS3mJPnt9";
    private String CONSUMER_SECRET = "oUq42taMrOjoScNSLPZYiFdMxw4VtZYXgejXwDX4aHwAGroObn";

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private TwitterLoginButton loginButton;
    private Button toggleButton;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Fragment listFragment;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        initFragments();

        toggleButton = (Button) findViewById(R.id.toggle);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Fragment current = fm.findFragmentByTag("CURRENT_FRAGMENT")
                        instanceof TweetListFragment
                            ? mapFragment
                            : listFragment;
                createTransaction(current, "CURRENT_FRAGMENT");
            }
        });
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, MY_PERMISSIONS_REQUEST_LOCATION);

    }

    private void requestPermission(String requested, int granted) {
        if (ContextCompat.checkSelfPermission(this, requested) != 0) {
            requestPermissions(
                new String[]{requested},
                granted
            );
        }
    }

    private void createTransaction(Fragment currentFragment) {
        createTransaction(currentFragment, null);
    }

    private void createTransaction(Fragment currentFragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, currentFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    private void initFragments() {
        mapFragment = new SupportMapFragment();
        mapFragment.getMapAsync(this);
        createTransaction(mapFragment);

        listFragment = new TweetListFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Location loc = null;
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location loc) {
                    LatLng currentLocation = new LatLng(loc.getLatitude(), loc.getLongitude());

                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
            loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                LatLng currentLocation = new LatLng(loc.getLatitude(), loc.getLongitude());

                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            }
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
