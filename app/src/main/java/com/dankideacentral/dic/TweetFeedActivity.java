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
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dankideacentral.dic.TweetListFragment.OnListFragmentInteractionListener;
import com.dankideacentral.dic.model.TweetNode;
import com.dankideacentral.dic.util.Fragmenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.Arrays;

import twitter4j.Status;

public class TweetFeedActivity extends BaseMapActivity
        implements OnListFragmentInteractionListener, ClusterManager.OnClusterClickListener, ClusterManager.OnClusterItemClickListener, LocationListener {

    private static final int MAP_ZOOM_DISTANCE = 12;
    private static final String CURRENT_FRAGMENT = "CURRENT_FRAGMENT";

    private static final int MIN_TIME = 250; //milliseconds
    private static final int MIN_DISTANCE = 0;

    // OTTAWA LAT AND LONG CONSTANTS
    private static final double OTTAWA_LATITUDE = 45.4215;
    private static final double OTTAWA_LONGITUDE = -75.6972;

    private ClusterManager<TweetNode> clusterManager;

    private TweetListFragment listFragment;
    private Fragmenter fm;
    private LocationFinder locationFinder;
    private AlertDialog.Builder alertBuilder;

    private Button toggleButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instantiate alertBuilder
        alertBuilder = new AlertDialog.Builder(this);

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
                Fragment current = fm.find(CURRENT_FRAGMENT)
                        instanceof TweetListFragment
                            ? getFragment()
                            : listFragment;
                fm.create(R.id.activity_tweet_feed, current, CURRENT_FRAGMENT);
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
                clusterManager.addItem(tweetNode);
                listFragment.insert(tweetNode);
                clusterManager.cluster();
            }
        }, new IntentFilter(getString(R.string.tweet_broadcast)));

        clusterManager = cm;

        clusterManager.setOnClusterClickListener(this);
        clusterManager.setOnClusterItemClickListener(this);

        try {
            lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME,
                    MIN_DISTANCE,
                    this);
        } catch (SecurityException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     *
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
     *
     * @param latLng
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
    protected void onStart() {
        super.onStart();

        // TODO: Remove if mapReady works
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service

        Intent stopServiceIntent = new Intent(this, TwitterStreamService.class);
        stopService(stopServiceIntent);
    }

    private void requestPermission(String requestedPermission, int grantedPermission) {
        if (ContextCompat.checkSelfPermission(this, requestedPermission) != 0)
            requestPermissions(new String[]{requestedPermission}, grantedPermission);
    }

    /**
     *
     * @param location
     * @return
     */
    private LatLng handleLocationGet(Location location) {
        // Case location successfully obtained
        if (location != null) {
            // Pull the location's latitude & longitude params into a LatLng object
            LatLng currentLocation = new LatLng(location.getLatitude(),
                    location.getLongitude());

            // Return the obtained LatLng object
            return currentLocation;
        } else {
            // Build dialog to inform user that obtaining location failed
            alertBuilder.setMessage(R.string.location_dialog_message)
                    .setTitle(R.string.location_dialog_title)
                    .setNeutralButton(R.string.location_dialog_okay, null);

            // Create & show the dialog box
            alertBuilder.create().show();

            // Return LatLng of Ottawa if could not obtain the location
            return new LatLng(OTTAWA_LATITUDE, OTTAWA_LONGITUDE);
        }
    }
}


