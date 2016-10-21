package com.dankideacentral.dic.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.Date;

import twitter4j.ExtendedMediaEntity;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Place;
import twitter4j.RateLimitStatus;
import twitter4j.Scopes;
import twitter4j.Status;
import twitter4j.SymbolEntity;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;


public class TweetNode extends WeightedNode {
    private LatLng position;
    private Status status;

    public TweetNode (double lat, double lng) {
        position = new LatLng(lat, lng);
    }

    public TweetNode (Status status) {
        this(status.getGeoLocation());
        this.status = status;
    }
    public TweetNode (GeoLocation geo) {
        this.position = new LatLng(geo.getLatitude(), geo.getLongitude());
        this.status = null;
    }

    public TweetNode (LatLng coords) {
        this.position = coords;
        this.status = null;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public float getWeight() {
        return 0;
    }
}
