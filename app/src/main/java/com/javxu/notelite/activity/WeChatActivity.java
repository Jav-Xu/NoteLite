package com.javxu.notelite.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.javxu.notelite.R;
import com.javxu.notelite.bean.WeChat;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class WeChatActivity extends AppCompatActivity  {

    private SweetAlertDialog mMyDialog;
    private WebView mWebView;
    private Toolbar tb_wechat;

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
        initToorbar();
        initView();
        initData();
    }

    private void initToorbar() {
        tb_wechat = (Toolbar) findViewById(R.id.toolbar_wechat);
        setSupportActionBar(tb_wechat);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_wechat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.share:
                //TODO
                break;
        }
        return true;
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