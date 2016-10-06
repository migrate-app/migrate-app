package com.dankideacentral.dic.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


public class TweetNode implements ClusterItem {
    LatLng position;
    public TweetNode (double lat, double lng) {
        position = new LatLng(lat, lng);
    }
    public TweetNode (LatLng coords) {
        this.position = coords;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }
}
