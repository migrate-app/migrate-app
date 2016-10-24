package com.dankideacentral.dic.util;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Class:   LocationFinder.java
 * Purpose: Utility to help search for the device's
 *          current location.
 *
 * @author Chris Ermel
 * @version 1.0
 * @since 2016-10-23
 */
public abstract class LocationFinder implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String LOG_TAG = "LOCATION_FINDER";

    private GoogleApiClient googleApiClient;
    private Context context;

    public LocationFinder(Context context) {
        this.context = context;

        // Instantiate GoogleAPIClient with given context
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Upon initializing, attempt to connect to client right away
        googleApiClient.connect();
    }

    /**
     * Connection callback for {@link GoogleApiClient}.
     *
     * @param bundle
     *          Data provided to {@link GoogleApiClient} by Google Play services.
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // On successful connection, log at info level
        Log.i(LOG_TAG, "GoogleApiClient successfully connected.");

        // Begin requesting location updates
        startLocationUpdates();
    }

    /**
     * Connection suspension callback for {@link GoogleApiClient}.
     *
     * @param cause
     *          The suspension causation code given to the callback method.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        // On connection suspension, log at info level
        Log.i(LOG_TAG, "GoogleApiClient suspended with code: " + cause + ".");
    }

    /**
     * Connection error callback for {@link GoogleApiClient}.
     *
     * @param result
     *          A {@link ConnectionResult} object used to
     *          determine what type of error occurred.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        // Error handling can be implemented here
        // For now, log the connection failure at debug level
        Log.d(LOG_TAG, "GoogleApiClient connection failed.");
    }

    /**
     * Disconnects the {@link LocationFinder}'s
     * {@link GoogleApiClient}.
     */
    public void disconnect() {
        googleApiClient.disconnect();
    }

    /**
     * Stops the {@link GoogleApiClient}'s listening for location updates.
     */
    public void stopLocationUpdates() {
        // Stop location updates from occurring
        LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient, this);
    }

    /**
     * Starts a request for location updates from the {@link GoogleApiClient}.
     */
    private void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, LocationRequest.create(), this);
        } catch (SecurityException e) {
            // SecurityException thrown if location permissions are disabled
            Log.d(LOG_TAG, "Location permissions deactivated when attempting to recover location.");

            // Toast small message to user
            Toast.makeText(context, "Location services turned off.", Toast.LENGTH_LONG).show();
        }
    }
}
