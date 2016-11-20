package com.dankideacentral.dic.activities;

import android.content.Context;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dankideacentral.dic.algo.WeightedNodeAlgorithm;
import com.dankideacentral.dic.model.TweetNode;
import com.dankideacentral.dic.view.WeightedNodeRenderer;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.maps.android.clustering.ClusterManager;

/**
 * Created by srowhani on 10/6/16.
 */

public abstract class BaseMapActivity extends AppCompatActivity implements
        OnMapReadyCallback, ClusterManager.OnClusterClickListener, ClusterManager.OnClusterItemClickListener {
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    private ClusterManager <TweetNode> mClusterManager;
    private WeightedNodeAlgorithm <TweetNode> mNodeAlgorithm;
    private WeightedNodeRenderer <TweetNode> mNodeRenderer;

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
        Log.v(getClass().getName(), "onMapReady called");
        mMap = googleMap;

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);


        mClusterManager = new ClusterManager(this, mMap);
        mNodeAlgorithm = new WeightedNodeAlgorithm <TweetNode>();
        mNodeRenderer = new WeightedNodeRenderer<TweetNode>(this, mMap, mClusterManager);

        mClusterManager.setAlgorithm(mNodeAlgorithm);
        mClusterManager.setRenderer(mNodeRenderer);

        mMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mapReady(mMap, locationManager, mClusterManager);
    }

    public abstract void mapReady (GoogleMap map, LocationManager lm, ClusterManager cm);
}
