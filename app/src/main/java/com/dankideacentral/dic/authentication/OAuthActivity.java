package com.dankideacentral.dic.authentication;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.dankideacentral.dic.R;

public class OAuthActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String authenticationUrl = getIntent().getStringExtra(getString(R.string.string_extra_authentication_url));
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        OAuthWebViewFragment oAuthWebViewFragment = new OAuthWebViewFragment(authenticationUrl);
        fragmentTransaction.add(android.R.id.content,oAuthWebViewFragment);
        fragmentTransaction.commit();
    }
}
