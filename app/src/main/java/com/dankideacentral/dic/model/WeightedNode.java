package com.dankideacentral.dic.model;

import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by srowhani on 10/21/16.
 */

public abstract class WeightedNode implements ClusterItem {
    public abstract float getWeight();
}

