package com.dankideacentral.dic.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import twitter4j.GeoLocation;


public class TweetNode implements ClusterItem {
    private LatLng position;

    public TweetNode (double lat, double lng) {
        position = new LatLng(lat, lng);
    }

    public TweetNode (GeoLocation geo) {
        this.position = new LatLng(geo.getLatitude(), geo.getLongitude());
    }

    public TweetNode (LatLng coords) {
        this.position = coords;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }
}
