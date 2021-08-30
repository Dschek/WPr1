package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webactivity);
        WebView webView = (WebView) findViewById (R.id.webView);
        String url = getIntent().getExtras().getString("url");
        webView.loadUrl(url);
    }
}