package com.dankideacentral.dic.util;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.text.internal.client.BoundingBoxParcel;

/**
 * Created by srowhani on 10/21/16.
 */

public class LocationUtil {
    public static final int EARTH_RADIUS = 6371;
    public static double [][] coordinatesToBoundingBox (double lat, double lon, int radius) {

        double rad = radius / EARTH_RADIUS;
        double x1 = lon - Math.toDegrees(rad / Math.cos(Math.toRadians(lat)));

        double x2 = lon + Math.toDegrees(rad / Math.cos(Math.toRadians(lat)));

        double y1 = lat + Math.toDegrees(rad);

        double y2 = lat - Math.toDegrees(rad);

        double[][] box = {{x1,y1}, {x2,y2}};
        return box;
    }

}
