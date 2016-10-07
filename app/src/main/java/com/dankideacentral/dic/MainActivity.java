package com.dankideacentral.dic;

import android.Manifest;
import android.content.Context;
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

import twitter4j.GeoLocation;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, OnListFragmentInteractionListener {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;

    private final String CURRENT_FRAGMENT = "CURRENT_FRAGMENT";

    private GoogleMap mMap;

    private SupportMapFragment mapFragment;
    private Fragment listFragment;

    private Button toggleButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        initFragments();

        initTweetListener();
        createTransaction(mapFragment, CURRENT_FRAGMENT);

        toggleButton = (Button) findViewById(R.id.toggle);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Fragment current = fm.findFragmentByTag(CURRENT_FRAGMENT)
                        instanceof TweetListFragment
                            ? mapFragment
                            : listFragment;
                createTransaction(current, CURRENT_FRAGMENT);
            }
        });
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, MY_PERMISSIONS_REQUEST_LOCATION);

    }

    private void initTweetListener() {
        ConfigurationBuilder cb = new ConfigurationBuilder();

        // TODO: Delete this object because app.properties is not being used any more.
        // PropertiesReader pr = new PropertiesReader("app.properties", getAssets());
        cb
                .setOAuthConsumerKey(getString(R.string.twitter_consumer_key))
                .setOAuthConsumerSecret(getString(R.string.twitter_consumer_secret))
                .setOAuthAccessToken(getString(R.string.twitter_access_key))
                .setOAuthAccessTokenSecret(getString(R.string.twitter_access_secret));
        TwitterStream stream = new TwitterStreamFactory(cb.build()).getInstance();
        stream.addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {
                Log.v("Twitter Status", status.toString());
                GeoLocation geo = status.getGeoLocation();
                if (geo != null) {
                    LatLng loc = new LatLng(geo.getLatitude(), geo.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(loc).title(status.getUser().getScreenName()));
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {

            }

            @Override
            public void onStallWarning(StallWarning warning) {

            }

            @Override
            public void onException(Exception ex) {

            }
        });
        stream.sample();
    }

    private void requestPermission(String requestedPermission, int grantedPermission) {
        if (ContextCompat.checkSelfPermission(this, requestedPermission) != 0) {
            requestPermissions(
                new String[]{requestedPermission},
                grantedPermission
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
        listFragment = new TweetListFragment();
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
                    Log.d("TEST", loc.getLatitude() + " " + loc.getLongitude());

                    LatLng currentLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(currentLocation));

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
                Log.d("TEST", loc.getLatitude() + " " + loc.getLongitude());
                LatLng currentLocation = new LatLng(loc.getLatitude(), loc.getLongitude());
                mMap.addMarker(new MarkerOptions().position(currentLocation));
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
