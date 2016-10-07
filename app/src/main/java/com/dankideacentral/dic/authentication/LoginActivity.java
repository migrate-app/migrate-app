package com.dankideacentral.dic.authentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// Copied from the tutorial
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.dankideacentral.dic.MainActivity;
import com.dankideacentral.dic.R;
import com.dankideacentral.dic.TwitterUtil;

import twitter4j.auth.RequestToken;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeLoginButton();

        System.out.println(this.getString(R.string.twitter_consumer_key));
        System.out.println(this.getString(R.string.twitter_consumer_secret));

        // Start the login process automatically
        // TODO: Add an if-condition that only logs the user if they're already authenticated.
        logIn();
    }

    /* Attach a click listener to the login button which starts the authentication process */
    private void initializeLoginButton() {
        View.OnClickListener buttonLoginOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        };

        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(buttonLoginOnClickListener);
    }

    private void logIn() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // Start MainActivity if user already logged in, otherwise start the authentication
        if (sharedPreferences.getBoolean(getString(R.string.preference_twitter_logged_in), false)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            new TwitterAuthenticateTask().execute();
        }
    }

    /* This task creates a RequestToken using the consumer key and consumer secret for our team
     * Twitter account, then opens a WebView that allows the user to enter their username and
     * password directly to Twitter. This task will run in a new thread.
     */
    private class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {
        @Override
        protected RequestToken doInBackground(String... params) {
            return TwitterUtil.getInstance().getRequestToken(getString(R.string.twitter_callback_url));
        }

        @Override
        protected void onPostExecute(RequestToken requestToken) {
            if (requestToken == null) {
                return; // TODO: Improve error handling when no request token is received.
            }

            // Use a WebView to provide an authentication interface to the user
            Intent intent = new Intent(getApplicationContext(), OAuthActivity.class);
            intent.putExtra(getString(R.string.string_extra_authentication_url), requestToken.getAuthenticationURL());
            startActivity(intent);
        }
    }
}