package com.dankideacentral.dic.view;

import com.dankideacentral.dic.model.WeightedNode;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.Set;

/**
 * Created by srowhani on 10/21/16.
 */

public class WeightedNodeRenderer <T extends WeightedNode> implements ClusterRenderer<T> {
    @Override
    public void onClustersChanged(Set<? extends Cluster<T>> set) {

    }

    @Override
    public void setOnClusterClickListener(ClusterManager.OnClusterClickListener<T> onClusterClickListener) {

    }

    @Override
    public void setOnClusterInfoWindowClickListener(ClusterManager.OnClusterInfoWindowClickListener<T> onClusterInfoWindowClickListener) {

    }

    @Override
    public void setOnClusterItemClickListener(ClusterManager.OnClusterItemClickListener<T> onClusterItemClickListener) {

    }

    @Override
    public void setOnClusterItemInfoWindowClickListener(ClusterManager.OnClusterItemInfoWindowClickListener<T> onClusterItemInfoWindowClickListener) {

    }

    @Override
    public void onAdd() {

    }

    @Override
    public void onRemove() {

    }
}
