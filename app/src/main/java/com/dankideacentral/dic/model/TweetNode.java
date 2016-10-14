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


public class TweetNode implements ClusterItem, Status {
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
    public Date getCreatedAt() {
        return status.getCreatedAt();
    }

    @Override
    public long getId() {
        return status.getId();
    }

    @Override
    public String getText() {
        return status.getText();
    }

    @Override
    public String getSource() {
        return status.getSource();
    }

    @Override
    public boolean isTruncated() {
        return status.isTruncated();
    }

    @Override
    public long getInReplyToStatusId() {
        return status.getInReplyToStatusId();
    }

    @Override
    public long getInReplyToUserId() {
        return status.getInReplyToUserId();
    }

    @Override
    public String getInReplyToScreenName() {
        return status.getInReplyToScreenName();
    }

    @Override
    public GeoLocation getGeoLocation() {
        return status.getGeoLocation();
    }

    @Override
    public Place getPlace() {
        return status.getPlace();
    }

    @Override
    public boolean isFavorited() {
        return status.isFavorited();
    }

    @Override
    public boolean isRetweeted() {
        return status.isRetweeted();
    }

    @Override
    public int getFavoriteCount() {
        return status.getFavoriteCount();
    }

    @Override
    public User getUser() {
        return status.getUser();
    }

    @Override
    public boolean isRetweet() {
        return status.isRetweet();
    }

    @Override
    public Status getRetweetedStatus() {
        return status.getRetweetedStatus();
    }

    @Override
    public long[] getContributors() {
        return status.getContributors();
    }

    @Override
    public int getRetweetCount() {
        return status.getRetweetCount();
    }

    @Override
    public boolean isRetweetedByMe() {
        return status.isRetweetedByMe();
    }

    @Override
    public long getCurrentUserRetweetId() {
        return status.getCurrentUserRetweetId();
    }

    @Override
    public boolean isPossiblySensitive() {
        return status.isPossiblySensitive();
    }

    @Override
    public String getLang() {
        return status.getLang();
    }

    @Override
    public Scopes getScopes() {
        return status.getScopes();
    }

    @Override
    public String[] getWithheldInCountries() {
        return status.getWithheldInCountries();
    }

    @Override
    public long getQuotedStatusId() {
        return status.getQuotedStatusId();
    }

    @Override
    public Status getQuotedStatus() {
        return status.getQuotedStatus();
    }

    @Override
    public int compareTo(Status o) {
        return status.compareTo(o);
    }

    @Override
    public UserMentionEntity[] getUserMentionEntities() {
        return status.getUserMentionEntities();
    }

    @Override
    public URLEntity[] getURLEntities() {
        return status.getURLEntities();
    }

    @Override
    public HashtagEntity[] getHashtagEntities() {
        return status.getHashtagEntities();
    }

    @Override
    public MediaEntity[] getMediaEntities() {
        return status.getMediaEntities();
    }

    @Override
    public ExtendedMediaEntity[] getExtendedMediaEntities() {
        return status.getExtendedMediaEntities();
    }

    @Override
    public SymbolEntity[] getSymbolEntities() {
        return status.getSymbolEntities();
    }

    @Override
    public RateLimitStatus getRateLimitStatus() {
        return status.getRateLimitStatus();
    }

    @Override
    public int getAccessLevel() {
        return status.getAccessLevel();
    }
}
