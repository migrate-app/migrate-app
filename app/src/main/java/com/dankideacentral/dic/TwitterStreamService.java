package com.dankideacentral.dic;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import twitter4j.FilterQuery;
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


    private TwitterStream twitterStream = null;
    private GeolocationFilter geoFilter = null;
    public String className = "TwitterStreamService";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int radius = preferences.getInt(getString(R.string.preference_radius), 250); // radius in km
        Log.d("RADIUS_PREF", radius + "");
        //  TODO: what happens when the auth or auth_secret is empty... we need to stop and return to login
        String authToken = preferences.getString(getString(R.string.twitter_auth_preference), null);
        String authTokenSecret = preferences.getString(
                getString(R.string.twitter_auth_secret_preference), null);
        AccessToken accessToken = new AccessToken(getString(R.string.twitter_access_key),
                getString(R.string.twitter_access_secret));

        double lat = intent.getDoubleExtra(getString(R.string.intent_lat), 0.0);
        double lon = intent.getDoubleExtra(getString(R.string.intent_long), 0.0);

        FilterQuery mFilter = new FilterQuery();
        mFilter.locations(GeolocationFilter.coordinatesToBoundingBox(lat, lon, radius));
        // set up the twitter stream
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .setOAuthConsumerKey(getString(R.string.twitter_consumer_key))
                .setOAuthConsumerSecret(getString(R.string.twitter_consumer_secret))
                .setOAuthAccessToken(authToken)
                .setOAuthAccessTokenSecret(authTokenSecret);
        twitterStream = new TwitterStreamFactory(configurationBuilder.build()).getInstance(accessToken);
        twitterStream.addListener(twitterStreamListener);
        // Begin filter stream
        twitterStream.filter(mFilter);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    // listens to the twitter stream
    StatusListener twitterStreamListener = new StatusListener() {
        private final String TAG = "TwitterStreamListener";
        @Override
        public void onException(Exception ex) {
            Log.e(TAG.concat("- Exception"), ex.getMessage());
        }

        @Override
        public void onStatus(Status status) {
            Log.d(TAG.concat("- Tweet"), status.toString());
            GeoLocation tweetLocation = status.getGeoLocation();

            if (tweetLocation != null) {
                Log.d(TAG.concat("- GeoTweet"), tweetLocation.toString());
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


}
