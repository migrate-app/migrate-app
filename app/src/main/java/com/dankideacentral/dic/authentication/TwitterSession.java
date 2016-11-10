package com.dankideacentral.dic.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.dankideacentral.dic.R;
import com.dankideacentral.dic.TwitterUtil;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Class:   TwitterSession.java
 * Purpose: Singleton to store authenticated user session
 *          data. Ensures multiple calls to shared preferences
 *          don't occur.
 *
 * @author Chris Ermel
 * @version 1.0
 * @since 2016-11-9
 */
public class TwitterSession {

    private static final String TOKEN_VERIFIER = "oauth_verifier";
    private static final String LOG_TAG = "TwitterSession";

    private static TwitterSession instance = null;
    private static AccessToken accessToken = null;
    private static RequestToken requestToken = null;

    /**
     * Protected constructor to prevent Singleton instantiation
     */
    protected TwitterSession() { }

    /**
     * Acquire's the instance of the TwitterSession
     *
     * @return
     *          The {@link TwitterSession} instance.
     */
    public static TwitterSession getInstance() {
        if (instance == null) {
            instance = new TwitterSession();
        }

        return instance;
    }

    /**
     * Generates and sets the Singleton's {@link AccessToken}.
     * For use on application startup when token's are left in
     * the shared preferences.
     *
     * @param authToken
     *          The user's unique authorization token.
     *
     * @param authTokenSecret
     *          The user's unique authorization token secret.
     *
     * @return
     *          The generated {@link AccessToken}.
     */
    public AccessToken generateAccessToken(final String authToken, final String authTokenSecret) {
        AccessToken accessToken = new AccessToken(authToken, authTokenSecret);
        setAccessToken(accessToken);

        return accessToken;
    }

    /**
     * Getter method for the singleton's {@link AccessToken}.
     *
     * @return
     *          An initialized {@link AccessToken} or null.
     */
    public AccessToken getAccessToken() {
        return accessToken;
    }

    /**
     * Sets the singleton's {@link RequestToken}.
     *
     * @param newRequestToken
     *          The new {@link RequestToken} to give the
     *          {@link TwitterSession}.
     */
    public void setRequestToken(final RequestToken newRequestToken) {
        requestToken = newRequestToken;
    }

    /**
     * Sets the singleton's {@link AccessToken}.
     *
     * @param newAccessToken
     *          The new {@link AccessToken} to give the
     *          {@link TwitterSession}.
     */
    private void setAccessToken(final AccessToken newAccessToken) {
        accessToken = newAccessToken;
    }

    /**
     * Pulls the oauth_verifier query parameter off of
     * the {@link Uri} redirected from Twitter.
     *
     * @param uri
     *          The parsed {@link Uri} containing the
     *          oauth_verifier query parameter.
     *
     * @return
     *          The oauth_verifier string or null.
     */
    private String getOAuthVerifierFromUri(final Uri uri) {
        return uri.getQueryParameter(TOKEN_VERIFIER);
    }

    /**
     * Creates an {@link AccessToken} from Twitter's
     * redirect {@link Uri}. The URI contains the
     * oauth_verifier as a query parameter.
     *
     * @param uri
     *          The {@link Uri} containing the oauth_verifier.
     */
    void createSession(final Uri uri, final Context applicationContext) {
        String authVerifier = getOAuthVerifierFromUri(uri);

        // Start asynchronous task to request user accessToken from twitter
        new GetTwitterAccessTokenTask(applicationContext).execute(authVerifier);
    }

    /**
     * Getter method for the singleton's {@link RequestToken}.
     *
     * @return
     *          An initialized {@link RequestToken} or null.
     */
    RequestToken getRequestToken() {
        return requestToken;
    }

    /**
     * Asynchronous task to request a valid user {@link AccessToken}
     * token from Twitter using an oauth_verifier.
     */
    private class GetTwitterAccessTokenTask extends AsyncTask<String, Long, AccessToken> {

        private Context applicationContext;

        GetTwitterAccessTokenTask(Context applicationContext) {
            super();
            this.applicationContext = applicationContext;
        }

        @Override
        protected AccessToken doInBackground(String... params) {
            Twitter twitter = TwitterUtil.getInstance().getTwitter();
            AccessToken accessToken = null;

            // oAuthVerifier will be the first param given to the async task
            String authVerifier = params[0];

            try {
                // Send request to twitter in order to obtain the user's AccessToken
                accessToken = twitter.getOAuthAccessToken(getRequestToken(), authVerifier);
            } catch (TwitterException | IllegalStateException e) {
                Log.i(LOG_TAG, "Error occurred when attempting to contact Twitter.");
            }

            return accessToken;
        }

        @Override
        protected void onPostExecute(AccessToken accessToken) {
            // Case error occurred when sending request to Twitter
            if (accessToken == null) {
                return;
            }

            // Store accessToken in Singleton & in SharedPreferences
            TwitterSession.getInstance().setAccessToken(accessToken);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            SharedPreferences.Editor prefEditor = preferences.edit();

            prefEditor.putString(applicationContext.getString(
                    R.string.twitter_auth_preference), accessToken.getToken());
            prefEditor.putString(applicationContext.getString(
                    R.string.twitter_auth_secret_preference), accessToken.getTokenSecret());

            // Reinitialize the Twitter Util to contain AccessToken
            TwitterUtil.getInstance().setTwitterAccessToken(accessToken);

            prefEditor.apply();
        }
    }
}
