package com.dankideacentral.dic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// Copied from the tutorial
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import twitter4j.auth.RequestToken;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean useStoredTokenKey = true;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeLoginButton();

        /* If the user is already authenticated, log the user in */
        if (useStoredTokenKey) {
            logIn();
        }
    }

    /* Attach click listener to the login button */
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

        if (sharedPreferences.getBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN, false)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            new TwitterAuthenticateTask().execute();
        }
    }

    private class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {
        @Override
        protected RequestToken doInBackground(String... params) {
            return TwitterUtil.getInstance().getRequestToken();
        }

        @Override
        protected void onPostExecute(RequestToken requestToken) {
            boolean useWebViewForAuthentication = true;

            if (requestToken != null)
            {
                if (useWebViewForAuthentication) {
                    Intent intent = new Intent(getApplicationContext(), OAuthActivity.class);
                    intent.putExtra(ConstantValues.STRING_EXTRA_AUTHENTICATION_URL, requestToken.getAuthenticationURL());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
                    startActivity(intent);
                }
            }
        }
    }
}