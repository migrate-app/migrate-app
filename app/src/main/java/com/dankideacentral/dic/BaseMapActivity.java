package com.dankideacentral.dic;

import android.content.Context;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;

import com.dankideacentral.dic.model.TweetNode;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.maps.android.clustering.ClusterManager;

/**
 * Created by srowhani on 10/6/16.
 */

public abstract class BaseMapActivity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    public BaseMapActivity () {
        mapFragment = SupportMapFragment.newInstance();
    }

    public GoogleMap getMap () {
        return this.mMap;
    }

    public SupportMapFragment getFragment () {
        return this.mapFragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        ClusterManager<TweetNode> clusterManager = new ClusterManager(this, mMap);
        mMap.setOnMarkerClickListener(clusterManager);

        mapReady(mMap, locationManager, clusterManager);
    }

    public abstract void mapReady (GoogleMap map, LocationManager lm, ClusterManager cm);
}
