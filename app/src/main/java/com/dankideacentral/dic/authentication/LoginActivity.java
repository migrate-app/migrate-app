package com.dankideacentral.dic.authentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeLoginButton();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // Automatically login users that are already authenticated
        // TODO: Add logic to automatically log in authenticated users.
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

    /* Start MainActivity for users already authenticated, otherwise open the authentication UI */
    private void logIn() {
        if (sharedPreferences.getBoolean(getString(R.string.preference_twitter_logged_in), false)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            new TwitterAuthenticateTask().execute();
        }
    }

    /* This task creates a RequestToken using the consumer key and consumer secret for our team's
     * Twitter account, then opens a WebView that allows the user to enter their username and
     * password directly to Twitter. This task runs in a new thread.
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