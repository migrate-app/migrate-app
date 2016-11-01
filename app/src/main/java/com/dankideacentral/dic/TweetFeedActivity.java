package com.dankideacentral.dic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;


import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dankideacentral.dic.TweetListFragment.OnListFragmentInteractionListener;
import com.dankideacentral.dic.model.TweetNode;
import com.dankideacentral.dic.util.Fragmenter;
import com.dankideacentral.dic.util.LocationFinder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Arrays;

import twitter4j.Status;

public class TweetFeedActivity extends BaseMapActivity
        implements OnListFragmentInteractionListener, ClusterManager.OnClusterClickListener, ClusterManager.OnClusterItemClickListener, LocationListener {

    private static final int MAP_ZOOM_DISTANCE = 12;
    private static final String CURRENT_FRAGMENT = "CURRENT_FRAGMENT";

    private static final int MIN_TIME = 250; //milliseconds
    private static final int MIN_DISTANCE = 0;

    private ClusterManager<TweetNode> clusterManager;

    private TweetListFragment listFragment;
    private Fragmenter fm;
    private LocationFinder locationFinder;

    private ArrayList<TweetNode> tweets = new ArrayList<>();
    private Button toggleButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tweet_feed);
        fm = new Fragmenter(getSupportFragmentManager());

        // Set the navigation icon of the tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_nav_button);

        // TODO: Implement toolbar's setNavigationOnClickListener method for nav drawer

        listFragment = new TweetListFragment();

        fm.create(R.id.activity_tweet_feed, getFragment(), CURRENT_FRAGMENT);
        getFragment().getMapAsync(this);

        toggleButton = (Button) findViewById(R.id.toggle);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TweetListFragment newFragment = new TweetListFragment(new ArrayList(cluster.getItems()));
                TweetListFragment newFragment = new TweetListFragment(tweets);
                Bundle args = new Bundle();
                //args.putParcelableArray("TWEET_LIST", cluster.getItems().toArray());
                newFragment.setArguments(args);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.activity_tweet_feed, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public void mapReady(GoogleMap map, LocationManager lm, final ClusterManager cm) {
        // Grab LatLng object from intent extra
        LatLng latLng = getIntent().getParcelableExtra(getString(
                R.string.search_location_key));

        // Case LatLng object returned is null (Could mean activity loaded on startup)
        if (latLng == null) {
            getCurrentLocation();
        } else {
            // Move the map to the specified latitude and longitude
            getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_ZOOM_DISTANCE));
        }

        // set up broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Status tweet = (Status) intent.getExtras().get("tweet");
                TweetNode tweetNode = new TweetNode(tweet);
                Log.v("Received Tweet: ", tweet.toString());
                tweets.add(tweetNode);
                clusterManager.addItem(tweetNode);
                clusterManager.cluster();
            }
        }, new IntentFilter(getString(R.string.tweet_broadcast)));

        clusterManager = cm;

        map.setOnCameraChangeListener(cm);
        clusterManager.setOnClusterClickListener(this);
        clusterManager.setOnClusterItemClickListener(this);

        try {
            lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME,
                    MIN_DISTANCE,
                    this);
        } catch (SecurityException e) {
            Toast.makeText(this, "Location services turned off.", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Creates a new instance of a {@link LocationFinder} object,
     * implementing its onLocationChanged() method to guarantee
     * reception of current known location.
     *
     * Once location is received, zooms the map fragment in to
     * the received location.
     */
    private void getCurrentLocation() {
        locationFinder = new LocationFinder(this) {
            @Override
            public void onLocationChanged(Location location) {
                // Convert location to a LatLng object
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                // Prevent further location updates from occurring
                locationFinder.stopLocationUpdates();

                // Disconnect from the googleApiClient
                locationFinder.disconnect();

                // Move the map to the specified latitude and longitude
                getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_ZOOM_DISTANCE));

                // Start and bind the tweet stream service
                startTwitterStreamService(latLng);
            }
        };
    }

    /**
     * Starts the twitter stream service to receive
     * tweets at the specified latitude & longitude locations.
     *
     * @param latLng
     *          The {@link LatLng} location to open the service at.
     */
    private void startTwitterStreamService(LatLng latLng) {
        double lat = latLng.latitude;
        double log = latLng.longitude;

        // Start and bind TwitterStreamService
        Intent startIntent = new Intent(this, TwitterStreamService.class);
        // put the radius and location on the intent
        startIntent.putExtra(getString(R.string.intent_lat), lat);
        startIntent.putExtra(getString(R.string.intent_long), log);
        startService(startIntent);
    }

    @Override
    public void onListFragmentInteraction(TweetNode item) {
        Snackbar.make(findViewById(R.id.activity_tweet_feed), item.toString(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onClusterClick(Cluster cluster) {
        Log.d("CLUSTER_CLICK", Arrays.toString(cluster.getItems().toArray()));
       // TweetListFragment newFragment = new TweetListFragment(new ArrayList(cluster.getItems()));
        TweetListFragment newFragment = new TweetListFragment(tweets);
        Bundle args = new Bundle();
        //args.putParcelableArray("TWEET_LIST", cluster.getItems().toArray());
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_tweet_feed, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        return true;
    }
    @Override
    public boolean onClusterItemClick(ClusterItem clusterItem) {
        Log.d("CLUSTER_ITEM_CLICK", clusterItem.getPosition().toString());
        return false;
    }

    @Override
    public void onLocationChanged(Location loc) {
        LatLng currentLocation = new LatLng(loc.getLatitude(), loc.getLongitude());

        Log.d("LOCATION_LISTENER", currentLocation.toString());

        getMap().moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service

        Intent stopServiceIntent = new Intent(this, TwitterStreamService.class);
        stopService(stopServiceIntent);
    }
}
