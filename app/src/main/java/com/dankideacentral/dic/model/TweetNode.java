package com.dankideacentral.dic.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.dankideacentral.dic.util.ImageProcessor;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import twitter4j.GeoLocation;
import twitter4j.Status;

public class TweetNode extends WeightedNode {
    private LatLng mPosition;
    private Status mStatus;
    private Bitmap mIcon;

    public TweetNode (double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }
    public TweetNode (GeoLocation geo) {
        this.mPosition = new LatLng(geo.getLatitude(), geo.getLongitude());
        this.mStatus = null;
    }

    public TweetNode (LatLng coords) {
        this.mPosition = coords;
        this.mStatus = null;
    }

    public TweetNode (Status status) {
        this(status.getGeoLocation());
        this.mStatus = status;
    }

    public void processImage (final Handler.Callback cb) {
        assert cb != null;
        new AsyncTask<String, Void, Bitmap> () {
            @Override
            protected Bitmap doInBackground(String... params) {
                return ImageProcessor.bitmapFromUrl(params[0]);
            }

            @Override
            protected void onPostExecute(Bitmap mBitMap) {
                super.onPostExecute(mBitMap);
                mIcon = mBitMap; // accessible from getIcon
                new Handler(cb).sendMessage(new Message()); // code for the Message.what
            }
        }.execute(getStatus().getUser().getProfileImageURL());
    }

    public Bitmap getIcon () {
        return mIcon;
    }
    public Status getStatus() {
        return mStatus;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public int getSize() {
        return mStatus.getHashtagEntities().length
                + mStatus.getUserMentionEntities().length
                + (mStatus.getUser().isVerified() ? 2 : 0)
                + (int) Math.log(mStatus.getUser().getFollowersCount());
    }
}
