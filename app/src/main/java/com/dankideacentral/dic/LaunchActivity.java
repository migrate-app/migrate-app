package com.dankideacentral.dic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.dankideacentral.dic.authentication.LoginActivity;

public class LaunchActivity extends AppCompatActivity {

    private static final String FINE_LOCATION_PERMISSION = "android.permission.ACCESS_FINE_LOCATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String twitterAuth = preferences.getString(getString(R.string.twitter_auth_preference), null);
        String twitterAuthSecret = preferences.getString(getString(R.string.twitter_auth_secret_preference), null);
        PackageManager pm = this.getPackageManager();
        int hasFineLocationPermission = pm.checkPermission(
                FINE_LOCATION_PERMISSION,
                this.getPackageName());
        // auth tokens don't expire. Just check if it exists
        if (twitterAuth == null || twitterAuthSecret == null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {
            // Initialize the TwitterUtil singleton only if auth tokens exist
            TwitterUtil.init(getApplicationContext());

            if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
                Intent mapIntent = new Intent(this, TweetFeedActivity.class);
                startActivity(mapIntent);
            } else {
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
            }
        }
    }
}
