package com.ldi.android.Activitys;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ldi.android.Activitys.Base.BaseActivity;
import com.ldi.android.R;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_web_view_client)
public class WebViewClientActivity extends BaseActivity {
    @Extra("title")
    String title;

    @Extra("url")
    String url;

    @ViewById(R.id.webViewClientWV)
    WebView webView;

    @ViewById(R.id.webViewProgressBar)
    ProgressBar progressbar;

    @AfterViews
    void afterViews(){
        //设置title
        setTitle(R.id.navigation_bar_back_tv,title);
        //webview
        webViewSetting();

        webView.loadUrl(url);
    }

    /**
     * WebView设置
     * */
    private void webViewSetting(){
        // User settings
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });
    }
    public class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(View.GONE);
            } else {
                if (progressbar.getVisibility() == View.GONE)
                    progressbar.setVisibility(View.VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Click({R.id.navigation_bar_back_ib})
    void click(View v){
        switch (v.getId()){
            case R.id.navigation_bar_back_ib:
                finish();
                break;
        }
    }
}
