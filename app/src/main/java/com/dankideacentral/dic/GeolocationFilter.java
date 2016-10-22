package com.dankideacentral.dic;

import twitter4j.GeoLocation;

/**
 * Created by amaxwell on 2016-10-08.
 */

public class GeolocationFilter {

    // either the current location or the searched location
    private GeoLocation searchLocation = null;
    private int radius = 0; // in KM
    private final static int EARTH_RADIUS = 6371;

    public GeolocationFilter(GeoLocation searchRegion, int radius) {
        searchLocation = searchRegion;
        radius = radius;
    }

    public GeolocationFilter(double lat, double lon, int radius) {
        searchLocation = new GeoLocation(lat, lon);
        radius = radius;
    }

    public GeolocationFilter(){
        searchLocation = null;
        radius = 1;
    }

    public void setRadius(int newRadius) {
        radius = newRadius;
    }

    public void setLocation(GeoLocation newSearchRegion) {
        searchLocation =  newSearchRegion;
    }

    public boolean inSearchRegion(GeoLocation tweetLocation){
        return (getDistanceFromSearchLocationToTweet(tweetLocation) <= radius)? true: false;
    }

    public static double [][] coordinatesToBoundingBox (double lat, double lon, int radius) {

        double rad = radius / EARTH_RADIUS;
        double x1 = lon - Math.toDegrees(rad / Math.cos(Math.toRadians(lat)));

        double x2 = lon + Math.toDegrees(rad / Math.cos(Math.toRadians(lat)));

        double y1 = lat + Math.toDegrees(rad);

        double y2 = lat - Math.toDegrees(rad);

        double[][] box = {{x1,y1}, {x2,y2}};
        return box;
    }
    // implementation of the Haversine formula

    private double getDistanceFromSearchLocationToTweet(GeoLocation tweetLocation){
        double dLat = Math.toRadians(tweetLocation.getLatitude() - searchLocation.getLatitude());
        double dLong = Math.toRadians(tweetLocation.getLongitude() - searchLocation.getLongitude());
        double searchLat = Math.toRadians(searchLocation.getLatitude());
        double tweetLat = Math.toRadians(tweetLocation.getLatitude());
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLong / 2),2) * Math.cos(searchLat) * Math.cos(tweetLat);
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS * c;
    }

}
