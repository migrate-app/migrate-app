package com.dankideacentral.dic.authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dankideacentral.dic.TweetFeedActivity;
import com.dankideacentral.dic.R;

public class OAuthWebViewFragment extends Fragment {
    private WebView webView;
    public OAuthWebViewFragment () {}
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String auth = getArguments().getString(getString(R.string.string_extra_authentication_url));

        webView.loadUrl(auth);
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.contains("oauth_verifier=")) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), TweetFeedActivity.class);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings webSettings = webView.getSettings();

        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oauth_webview,container,false);
        webView = (WebView) view.findViewById(R.id.webViewOAuth);
        return view;
    }
}
