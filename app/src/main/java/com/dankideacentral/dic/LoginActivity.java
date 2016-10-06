package com.dankideacentral.dic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// Copied from the tutorial
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import com.hintdesk.core.activities.AlertMessageBox;
import com.hintdesk.core.util.OSUtil;
import com.hintdesk.core.util.StringUtil;
import twitter4j.auth.RequestToken;

public class LoginActivity extends AppCompatActivity {

    private Button buttonLogin;
    private boolean isUseStoredTokenKey = false;
    private boolean isUseWebViewForAuthentication = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeLoginButton();

        /* If the user is already authenticated, log the user in */
        if (isUseStoredTokenKey) {
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

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(buttonLoginOnClickListener);
    }

    private void logIn() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!sharedPreferences.getBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN, false)) {
            new TwitterAuthenticateTask().execute();
        } else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {
        @Override
        protected void onPostExecute(RequestToken requestToken) {
            if (requestToken!=null)
            {
                if (!isUseWebViewForAuthentication)
                {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), OAuthActivity.class);
                    intent.putExtra(ConstantValues.STRING_EXTRA_AUTHENCATION_URL,requestToken.getAuthenticationURL());
                    startActivity(intent);
                }
            }
        }

        @Override
        protected RequestToken doInBackground(String... params) {
            return TwitterUtil.getInstance().getRequestToken();
        }
    }
}