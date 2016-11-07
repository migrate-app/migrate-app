package com.dankideacentral.dic;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.IDs;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterStreamService extends Service {

    private static Set<Long> friends = new HashSet<>();
    private static Set<Long> followers = new HashSet<>();

    private static Twitter twitter;

    private static TwitterStream twitterStream = null;
    private static GeolocationFilter geoFilter = null;
    public  static String className = "TwitterStreamService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Asynchronously fetch user's friends and followers
        twitter = TwitterUtil.getInstance().getTwitter();
        new GetTwitterFollowers().execute();
        new GetTwitterFriends().execute();

        Log.v(className, "Starting Twitter Stream");

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
        mFilter.locations(GeolocationFilter.createBounds(lat, lon, radius));
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
        //twitterStream.sample();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(className, "Destroying Twitter Stream");
        twitterStream.shutdown();
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

    private class GetTwitterFollowers extends AsyncTask<Void, Void, Set<Long>> {

        @Override
        protected Set<Long> doInBackground(Void... params) {
            Set<Long> followersSet = new HashSet<>();
            try {
                long userID = twitter.getId();
                IDs ids = twitter.getFollowersIDs(userID, -1);
                int remaining = ids.getIDs().length;
                while(remaining > 0) {
                    for (long id : ids.getIDs()) {
                        followersSet.add(id);
                    }
                    ids = twitter.getFollowersIDs(userID, ids.getNextCursor());
                    remaining = ids.getIDs().length;
                }
            } catch (TwitterException e) {
                Log.i("TweetStreamService", "Error occurred when attempting to find user's followers. " +
                        "It's probably because you're trying to fetch too many followers.");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return followersSet;
        }

        @Override
        protected void onPostExecute(Set<Long> followersSet) {
            if (followersSet == null) {
                Toast.makeText(TwitterStreamService.this.getBaseContext(),
                        "Unable to get followers from Twitter.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(TwitterStreamService.this.getBaseContext(),
                        "You have " + followersSet.size() + " followers you loser.",
                        Toast.LENGTH_LONG).show();
                followers.addAll(followersSet);
            }
        }
    }

    private class GetTwitterFriends extends AsyncTask<Void, Void, Set<Long>> {

        @Override
        protected Set<Long> doInBackground(Void... params) {
            Set<Long> friendsSet = new HashSet<>();
            try {
                long userID = twitter.getId();
                IDs ids = twitter.getFriendsIDs(userID, -1);
                int remaining = ids.getIDs().length;
                while(remaining > 0) {
                    for (long id : ids.getIDs()) {
                        friendsSet.add(id);
                    }
                    ids = twitter.getFriendsIDs(userID, ids.getNextCursor());
                    remaining = ids.getIDs().length;
                }
            } catch (TwitterException e) {
                Log.i("TweetStreamService", "Error occurred when attempting to find user's friends." +
                        "It's probably because you're trying to fetch too many friends.");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return friendsSet;
        }

        @Override
        protected void onPostExecute(Set<Long> friendsSet) {
            if (friendsSet == null) {
                Toast.makeText(TwitterStreamService.this.getBaseContext(),
                        "Unable to get followers from Twitter.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(TwitterStreamService.this.getBaseContext(),
                        "You have " + friendsSet.size() + " friends you loser.",
                        Toast.LENGTH_LONG).show();
                friends.addAll(friendsSet);
            }
        }
    }
}
