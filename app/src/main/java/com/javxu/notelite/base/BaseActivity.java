package com.javxu.notelite.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.javxu.notelite.utils.LoogUtil;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.activity
 * File Name:     BaseActivity
 * Creator:       Jav-Xu
 * Create Time:   2017/6/11 15:55
 * Description:   TODO
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoogUtil.d(getClass().getName() + " -- onCreate");
        // 解决 home 键返回桌面，再打开 app 仍然被 LoginActivity 插队的现象
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoogUtil.d(getClass().getName() + " -- onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        LoogUtil.d(getClass().getName() + " -- onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LoogUtil.d(getClass().getName() + " -- onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LoogUtil.d(getClass().getName() + " -- onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoogUtil.d(getClass().getName() + " -- onDestory");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LoogUtil.d(getClass().getName() + " -- onRestart");
    }
}
