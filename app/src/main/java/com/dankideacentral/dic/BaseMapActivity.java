package com.dankideacentral.dic;

import android.content.Context;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;

import com.dankideacentral.dic.model.TweetNode;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.Algorithm;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.Collection;
import java.util.Set;

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
        clusterManager.setRenderer(new ClusterRenderer<TweetNode>() {
            @Override
            public void onClustersChanged(Set<? extends Cluster<TweetNode>> set) {

            }

            @Override
            public void setOnClusterClickListener(ClusterManager.OnClusterClickListener<TweetNode> onClusterClickListener) {

            }

            @Override
            public void setOnClusterInfoWindowClickListener(ClusterManager.OnClusterInfoWindowClickListener<TweetNode> onClusterInfoWindowClickListener) {

            }

            @Override
            public void setOnClusterItemClickListener(ClusterManager.OnClusterItemClickListener<TweetNode> onClusterItemClickListener) {

            }

            @Override
            public void setOnClusterItemInfoWindowClickListener(ClusterManager.OnClusterItemInfoWindowClickListener<TweetNode> onClusterItemInfoWindowClickListener) {

            }

            @Override
            public void onAdd() {

            }

            @Override
            public void onRemove() {

            }
        });
        mMap.setOnMarkerClickListener(clusterManager);

        mapReady(mMap, locationManager, clusterManager);
    }

    public abstract void mapReady (GoogleMap map, LocationManager lm, ClusterManager cm);
}
