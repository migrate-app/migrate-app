package com.dankideacentral.dic;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Class:   SearchActivity.java
 * Purpose: Activity to search for a geographical location
 *          or to enable location services for the app
 *
 * @author Chris Ermel
 * @version 1.1
 * @since 2016-10-2
 */
public class SearchActivity extends AppCompatActivity {

    private static final String FINE_LOCATION_PERMISSION = "android.permission.ACCESS_FINE_LOCATION";
    private static final int REQUEST_PERMISSION_FINE_LOCATION = 1;

    private ImageButton locationButton;
    private Button searchButton;
    private PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get packageManager
        packageManager = this.getPackageManager();

        setContentView(R.layout.activity_search);

        // Retrieve button views from resources
        locationButton = (ImageButton) findViewById(R.id.locationButton);
        searchButton = (Button) findViewById(R.id.searchButton);

        // Sets the onclick event listeners for each button in the activity
        setOnClickListeners();
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
                // TODO: Implement search on click event
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
            ActivityCompat.requestPermissions(this,
                    new String[]{FINE_LOCATION_PERMISSION},
                    REQUEST_PERMISSION_FINE_LOCATION);
        } else {
            // Case permission is enabled - inform user of this via a dialog box
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Build dialog to inform user that location services already enabled
            builder.setMessage(R.string.locationDialogMessage)
                    .setTitle(R.string.locationDialogTitle)
                    .setNeutralButton(R.string.okay, null);

            // Create & show the dialog box
            builder.create().show();
        }
    }
}
