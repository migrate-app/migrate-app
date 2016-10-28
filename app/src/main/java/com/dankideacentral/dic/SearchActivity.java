package com.dankideacentral.dic;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dankideacentral.dic.util.LocationFinder;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

/**
 * Class:   SearchActivity.java
 * Purpose: Activity to search for a geographical location
 *          or to enable location services for the app
 *
 * @author Chris Ermel
 * @version 1.2
 * @since 2016-10-2
 */
public class SearchActivity extends AppCompatActivity {

    private static final String LOG_TAG = "SEARCH_ACTIVITY";
    private static final String FINE_LOCATION_PERMISSION = "android.permission.ACCESS_FINE_LOCATION";
    private static final String COARSE_LOCATION_PERMISSION = "android.permissions.ACCESS_COARSE_LOCATION";
    private static final int REQUEST_PERMISSION_LOCATIONS = 1;

    private ImageButton locationButton;
    private Button searchButton;
    private PlaceAutocompleteFragment autocompleteFragment;
    private PackageManager packageManager;
    private AlertDialog.Builder alertBuilder;
    private LatLng currentLatLng;
    private LocationFinder locationFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get packageManager
        packageManager = this.getPackageManager();

        // Instantiate alertBuilder
        alertBuilder = new AlertDialog.Builder(this);

        setContentView(R.layout.activity_search);

        // Retrieve button & fragment views from resources
        locationButton = (ImageButton) findViewById(R.id.locationButton);
        searchButton = (Button) findViewById(R.id.searchButton);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // Sets the google places autocomplete listeners
        setAutocompleteListeners();

        // Sets the onclick event listeners for each button in the activity
        setOnClickListeners();
    }

    /**
     * Callback function that occurs when a permission has been
     * request from the system.
     *
     * Handles the fine location permission request by
     * attempting to obtain the device's current location.
     *
     * @param requestCode
     *          The request code passed in to identify which
     *          permission was requested.
     *
     * @param permissions
     *          The requested permissions.
     *
     * @param grantResults
     *          Whether or not the specified permission was granted or not.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        // Case requestCode is the location permission request
        if (requestCode == REQUEST_PERMISSION_LOCATIONS) {

            // Case grantResults not empty (occurs on permission request cancellation)
            // and permission granted by user
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Uses the LocationFinder to grab the current location of the device
                getCurrentLocation();
            }
        }
    }

    /**
     * Sets the {@link android.view.View.OnClickListener}'s of
     * all button's contained within the {@link SearchActivity}.
     */
    private void setOnClickListeners() {
        // Sets the onClick event listener for the location service button
        locationButton.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View view) {
                locationButtonOnClickEvent();
            }
        });

        // Sets the onClick event listener for the search button
        searchButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                sendLatLngObject(currentLatLng);
            }
        });
    }

    /**
     * Event triggered by clicking the {@link SearchActivity}'s
     * location button.
     *
     * Requests location permissions if they aren't enabled,
     * otherwise warns the user that location permissions are
     * enabled.
     */
    private void locationButtonOnClickEvent() {
        // Check if location is currently enabled in button's view context
        int isLocationEnabled = packageManager.checkPermission(FINE_LOCATION_PERMISSION,
                this.getPackageName());

        // Case permission not enabled
        if (isLocationEnabled == PackageManager.PERMISSION_DENIED) {

            // Send location permission request to the user
            // Passes REQUEST_PERMISSION_LOCATIONS for callback identification
            ActivityCompat.requestPermissions(this,
                    new String[]{FINE_LOCATION_PERMISSION, COARSE_LOCATION_PERMISSION},
                    REQUEST_PERMISSION_LOCATIONS);
        } else {
            // Uses the LocationFinder to get the current location of the device
            getCurrentLocation();
        }
    }

    /**
     * Creates a new instance of a {@link LocationFinder} object,
     * implementing its onLocationChanged() method to guarantee
     * reception of current known location.
     *
     * Once location is received, passes focus to the {@link TweetFeedActivity}.
     */
    private void getCurrentLocation() {
        // Acquire location from the device by initializing a locationFinder object
        locationFinder = new LocationFinder(this) {
            @Override
            public void onLocationChanged(Location location) {
                // Prevent further location updates from occurring
                locationFinder.stopLocationUpdates();

                // Disconnect from the googleApiClient
                locationFinder.disconnect();

                // Handle device location response - send to
                handleLocationSend(location);
            }
        };
    }

    /**
     * Handles the app's control flow after attempting to recover
     * the devices current location.
     *
     * If location was successfully obtained, pass control to the
     * {@link TweetFeedActivity} passing a {@link LatLng} object
     * in a {@link Bundle}.
     *
     * If location acquisition failed, alerts the user by displaying
     * an {@link AlertDialog}.
     *
     * @param location
     *          The location object we are handling.
     */
    private void handleLocationSend(Location location) {
        // Case location successfully obtained
        if (location != null) {
            // Parcel a LatLng object to be sent as a Bundle to the TweetFeedActivity
            LatLng currentLocation = new LatLng(location.getLatitude(),
                    location.getLongitude());

            sendLatLngObject(currentLocation);
        } else {
            // Build dialog to inform user that obtaining location failed
            alertBuilder.setMessage(R.string.location_dialog_message)
                    .setTitle(R.string.location_dialog_title)
                    .setNeutralButton(R.string.location_dialog_okay, null);

            // Create & show the dialog box
            alertBuilder.create().show();
        }
    }

    /**
     * Bundles a {@link LatLng} object and sends it to
     * the {@link TweetFeedActivity}.
     *
     * @param latLng
     *          The object containing latitude and longitude values.
     */
    private void sendLatLngObject(LatLng latLng) {
        // Bundle up the LatLng object identified by search_location_key
        Bundle locationBundle = new Bundle();
        locationBundle.putParcelable(getString(R.string.search_location_key),
                latLng);

        // Open TweetFeedActivity and send latitude and longitude bundle as an extra
        Intent mapIntent = new Intent(this, TweetFeedActivity.class);
        mapIntent.putExtras(locationBundle);

        startActivity(mapIntent);
    }

    /**
     * Sets all listeners associated with the Google Places Autocomplete fragment.
     */
    private void setAutocompleteListeners() {
        autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button)
                .setOnClickListener(new View.OnClickListener() {
                    // Overrides clear button in the autocomplete fragment
                    @Override
                    public void onClick(View view) {
                        // Clear the text box
                        ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setText("");
                        view.setVisibility(View.GONE);

                        // Unset the currentLatLng object
                        currentLatLng = null;

                        // Disable the button
                        searchButton.setEnabled(false);
                    }
        });


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            // Implements the listener for when the place field is filled
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(LOG_TAG, "Places autocomplete selected: " + place.getName());

                // Set the current LatLng object
                currentLatLng = place.getLatLng();

                // Set the search button to be enabled
                searchButton.setEnabled(true);
            }

            @Override
            public void onError(Status status) {
                Log.i(LOG_TAG, "An error occurred with the autocomplete text view: " + status);
            }
        });
    }
}
