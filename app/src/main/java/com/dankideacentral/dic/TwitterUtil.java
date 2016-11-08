package com.dankideacentral.dic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public final class TwitterUtil {

    private static RequestToken requestToken = null;
    private static TwitterFactory twitterFactory = null;
    private static Twitter twitter = null;

    private static TwitterUtil instance = new TwitterUtil();

    public static void init(Context applicationContext) {
        // Get twitter auth token & secret from shared preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);

        String twitterAuthToken = preferences.getString(
                applicationContext.getString(R.string.twitter_auth_preference), null);
        String twitterAuthTokenSecret = preferences.getString(
                applicationContext.getString(R.string.twitter_auth_secret_preference), null);

        // Create AccessToken to give to the twitterFactory
        AccessToken accessToken = new AccessToken(applicationContext.getString(R.string.twitter_access_key),
                applicationContext.getString(R.string.twitter_access_secret));

        // Create twitter authentication configuration
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true)
                .setOAuthConsumerKey(applicationContext.getString(R.string.twitter_consumer_key))
                .setOAuthConsumerSecret(applicationContext.getString(R.string.twitter_consumer_secret))
                .setOAuthAccessToken(twitterAuthToken)
                .setOAuthAccessTokenSecret(twitterAuthTokenSecret);

        twitterFactory = new TwitterFactory(configurationBuilder.build());
        twitter = twitterFactory.getInstance(accessToken);
    }

    /* Making the constructor private disallows the creation of TwitterUtil objects,
     * which is required to stay in line with the Singleton design pattern */
    private TwitterUtil() {}

    public RequestToken getRequestToken(String twitterCallbackURL) {
        if (requestToken == null) {
            try {
                requestToken = twitterFactory.getInstance().getOAuthRequestToken(twitterCallbackURL);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
        return requestToken;
    }

    public void reset() {
        instance = new TwitterUtil();
    }

    /***********************
     * Getters and setters *
     ***********************/

    public TwitterFactory getTwitterFactory() {
        return twitterFactory;
    }

    public void setTwitterFactory(AccessToken accessToken) {
        twitter = twitterFactory.getInstance(accessToken);
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public static TwitterUtil getInstance() {
        return instance;
    }
}