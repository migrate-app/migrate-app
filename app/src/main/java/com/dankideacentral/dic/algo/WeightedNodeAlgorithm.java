package com.dankideacentral.dic.algo;

import com.dankideacentral.dic.model.TweetNode;
import com.dankideacentral.dic.model.WeightedNode;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.algo.Algorithm;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.google.maps.android.geometry.Point;
import com.google.maps.android.projection.SphericalMercatorProjection;
import com.google.maps.android.quadtree.PointQuadTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @author srowhani
 * @param <T>
 */
public class WeightedNodeAlgorithm <T extends WeightedNode> implements Algorithm <T>{

    public static final int MAX_DISTANCE_AT_ZOOM = 100; // essentially 100 dp.

    /**
     * Any modifications should be synchronized on mQuadTree.
     */
    private final Collection<WeightedItem<T>> mItems = new ArrayList<WeightedItem<T>>();

    /**
     * Any modifications should be synchronized on mQuadTree.
     */
    private final PointQuadTree<WeightedItem<T>> mQuadTree = new PointQuadTree<WeightedItem<T>>(0, 1, 0, 1);

    private static final SphericalMercatorProjection PROJECTION = new SphericalMercatorProjection(1);

    private static class WeightedItem <T extends ClusterItem> implements PointQuadTree.Item, Cluster<T> {
        private final T mClusterItem;
        private final Point mPoint;
        private final LatLng mPosition;
        private Set<T> singletonSet;

        private WeightedItem (T item) {
            mClusterItem = item;
            mPosition = item.getPosition();
            mPoint = PROJECTION.toPoint(mPosition);
            singletonSet = Collections.singleton(mClusterItem);
        }

        @Override
        public Point getPoint() {
            return mPoint;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public Set<T> getItems() {
            return singletonSet;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public int hashCode() {
            return mClusterItem.hashCode();
        };

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof WeightedItem<?>)) {
                return false;
            }

            return ((WeightedItem<?>) other).mClusterItem.equals(mClusterItem);
        }
    }


    @Override
    public void addItem(T item) {
        final WeightedItem<T> quadItem = new WeightedItem<T>(item);
        synchronized (mQuadTree) {
            mItems.add(quadItem);
            mQuadTree.add(quadItem);
        }
    }
    @Override
    public void addItems(Collection<T> items) {
        for (T item : items) {
            addItem(item);
        }
    }

    @Override
    public void clearItems() {

    }

    @Override
    public void removeItem(T t) {

    }

    @Override
    public Set<? extends Cluster<T>> getClusters(double v) {
        return null;
    }

    @Override
    public Collection<T> getItems() {
        return null;
    }
}
