package com.manandhiman.sdcproject;

import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivty extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_activty);
        webView = findViewById(R.id.webView);


        loadContentIntoWebView();
    }

    private void loadContentIntoWebView() {
        Uri uri = getIntent().getParcelableExtra("uri");
        webView.loadUrl(String.valueOf(uri));
    }
}