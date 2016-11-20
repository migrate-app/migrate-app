package com.dankideacentral.dic.util;

import android.content.Context;

import com.dankideacentral.dic.R;

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

    /**
     * Sets {@link Twitter}'s {@link AccessToken}.
     * If the token is null, reinitializes Twitter without
     * an AccessToken.
     *
     * @param accessToken
     *          The User's {@link AccessToken} we are giving
     *          to the {@link Twitter} object.
     */
    public void setTwitterAccessToken(AccessToken accessToken) {
        if (accessToken == null) {
            twitter = twitterFactory.getInstance();
        } else {
            twitter = twitterFactory.getInstance(accessToken);
        }
    }

    /**
     * Clears the Singleton's current {@link RequestToken}.
     */
    public void clearTwitterRequestToken() {
        requestToken = null;
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public static TwitterUtil getInstance() {
        return instance;
    }
}