package com.javxu.notelite.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.javxu.notelite.R;
import com.javxu.notelite.bean.WeChat;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class WeChatActivity extends BackActivity {

    private SweetAlertDialog mMyDialog;
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
        mMyDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mMyDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mMyDialog.setTitleText("Loading");
        mMyDialog.setCancelable(false);
        mMyDialog.show();
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
                mMyDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                mMyDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                mMyDialog.setTitleText("文章加载成功");
                mMyDialog.dismiss();
            }
            super.onProgressChanged(view, newProgress);
        }


    }
}