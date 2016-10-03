package com.dankideacentral.dic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Class:   SearchActivity.java
 * Purpose: Activity to search for a geographical location
 *          or to enable location services for the app
 *
 * @author Chris Ermel
 * @version 1.0
 * @since 2016-10-2
 */
public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
}
