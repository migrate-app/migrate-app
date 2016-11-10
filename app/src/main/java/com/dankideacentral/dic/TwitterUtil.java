package com.dankideacentral.dic;

import android.content.Context;

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
        // Create twitter authentication configuration
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true)
                .setOAuthConsumerKey(applicationContext.getString(R.string.twitter_consumer_key))
                .setOAuthConsumerSecret(applicationContext.getString(R.string.twitter_consumer_secret));

        twitterFactory = new TwitterFactory(configurationBuilder.build());
        twitter = twitterFactory.getInstance();
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

    public void setTwitterAccessToken(AccessToken accessToken) {
        twitter = twitterFactory.getInstance(accessToken);
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public static TwitterUtil getInstance() {
        return instance;
    }
}