package com.dankideacentral.dic.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.dankideacentral.dic.R;
import com.dankideacentral.dic.fragments.OAuthWebViewFragment;

public class OAuthActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String authenticationUrl = getIntent().getStringExtra(
                getString(R.string.string_extra_authentication_url));
        Bundle args = new Bundle();
        args.putString(getString(R.string.string_extra_authentication_url), authenticationUrl);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment oAuthWebViewFragment = Fragment.instantiate(
                getApplicationContext(), OAuthWebViewFragment.class.getName(), args);
        fragmentTransaction.add(android.R.id.content, oAuthWebViewFragment);
        fragmentTransaction.commit();
    }
}
