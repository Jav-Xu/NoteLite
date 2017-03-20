package com.javxu.notelite.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.javxu.notelite.R;
import com.javxu.notelite.bean.WeChat;

public class WeChatActivity extends BackActivity {

    private ProgressBar mProgressBar;
    private WebView mWebView;

    public static Intent getIntent(Context context, WeChat weChat) {
        Intent intent = new Intent(context, WeChatActivity.class);
        intent.putExtra("url", weChat.getNewsUrl());
        intent.putExtra("title", weChat.getTitle());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat);
        initView();
        initData();
    }

    private void initView() {
        mProgressBar = (ProgressBar) findViewById(R.id.pb_wechats);
        mWebView = (WebView) findViewById(R.id.wv_wechat);
    }

    private void initData() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        final String url = intent.getStringExtra("url");
        getSupportActionBar().setTitle(title);

        //支持JS
        mWebView.getSettings().setJavaScriptEnabled(true);

        //支持缩放
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);

        //接口回调
        mWebView.setWebChromeClient(new WebViewClient());

        //加载网页
        mWebView.loadUrl(url);

        //本地显示
        mWebView.setWebViewClient(new android.webkit.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true; //我接受这个事件
            }
        });
    }

    public class WebViewClient extends WebChromeClient {

        //进度变化的监听
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}