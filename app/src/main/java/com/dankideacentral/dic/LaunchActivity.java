package com.dankideacentral.dic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class LaunchActivity extends AppCompatActivity {

    private static final String EMPTY_STRING = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TwitterUtil.init(getApplicationContext()); // initialize the Twitter Singleton
        String twitterAuth = preferences.getString(getString(R.string.twitter_auth_preference), "");
        String fineLocationPermission = "android.permission.ACCESS_FINE_LOCATION";
        PackageManager pm = this.getPackageManager();
        int hasFineLocationPermission = pm.checkPermission(
                fineLocationPermission,
                this.getPackageName());
        // auth tokens don't expire. Just check if it exists
        if (EMPTY_STRING.equals(twitterAuth)) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {
            if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
                Intent mapIntent = new Intent(this, MainActivity.class);
                startActivity(mapIntent);
            } else {
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
            }
        }
    }
}
