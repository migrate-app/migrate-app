package com.dankideacentral.dic;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Set;

import twitter4j.GeoLocation;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterStreamService extends Service {

    private final IBinder mBinder = new TwitterStreamBinder();

    private TwitterStream twitterStream = null;
    private GeolocationFilter geoFilter = null;
    private HashMap<GeoLocation, Integer> locationCount = new HashMap<GeoLocation, Integer>();
    public String className = "TwitterStreamService";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(className, "is running");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int radius = preferences.getInt(getString(R.string.preference_radius), 20);
        //  TODO: what happens when the auth or auth_secret is empty... we need to stop and return to login
        String authToken = preferences.getString(getString(R.string.twitter_auth_preference), null);
        String authTokenSecret = preferences.getString(getString(R.string.twitter_auth_secret_preference), null);
        AccessToken accessToken = new AccessToken(getString(R.string.twitter_access_key),
                getString(R.string.twitter_access_secret));
        double lat = intent.getDoubleExtra(getString(R.string.intent_lat), 0.0);
        double lon = intent.getDoubleExtra(getString(R.string.intent_long), 0.0);
        geoFilter = new GeolocationFilter(lat, lon, radius);
        // set up the twitter stream
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .setOAuthConsumerKey(getString(R.string.twitter_consumer_key))
                .setOAuthConsumerSecret(getString(R.string.twitter_consumer_secret))
                .setOAuthAccessToken(authToken)
                .setOAuthAccessTokenSecret(authTokenSecret);
        twitterStream = new TwitterStreamFactory(configurationBuilder.build()).getInstance(accessToken);
        twitterStream.addListener(twitterStreamListener);
        twitterStream.sample();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public class TwitterStreamBinder extends Binder {
        TwitterStreamService getService() {
            return TwitterStreamService.this;
        }

    }

    // listens to the twitter stream
    StatusListener twitterStreamListener = new StatusListener() {

        @Override
        public void onException(Exception ex) {

        }

        @Override
        public void onStatus(Status status) {
            GeoLocation tweetLocation = status.getGeoLocation();
            Log.d("TwitterStream - tweet", tweetLocation.toString());


            if (tweetLocation != null && geoFilter.inSearchRegion(tweetLocation)) {
                addToLocationCount(tweetLocation);
                Intent statusIntent = new Intent(getApplicationContext(), TweetFeedActivity.class);
                statusIntent.setAction(getString(R.string.tweet_broadcast));
                statusIntent.putExtra("tweet", status);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(statusIntent);

            }
        }

        @Override
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

        }

        @Override
        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

        }

        @Override
        public void onScrubGeo(long userId, long upToStatusId) {

        }

        @Override
        public void onStallWarning(StallWarning warning) {

        }
    };

    private void addToLocationCount(GeoLocation gl) {

        Integer count = locationCount.get(gl);
        if (count == null) {
            locationCount.put(gl, 1);
        } else {
            locationCount.put(gl, count +1);
        }
    }

    // provide a bunch of public methods for the client to get data

    // get location count

    public int getLocationTweetCount(GeoLocation location) {
        Integer count = 0;
        if (location != null) {
            count = locationCount.get(location);
            return (count != null) ? count : 0;
        }
        return 0;
    }

    public Set<GeoLocation> getLocations() {
        return locationCount.keySet();
    }


}
