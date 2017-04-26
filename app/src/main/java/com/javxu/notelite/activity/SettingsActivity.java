package com.javxu.notelite.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.javxu.notelite.R;
import com.javxu.notelite.base.BackActivity;
import com.javxu.notelite.utils.StaticUtil;

public class SettingsActivity extends BackActivity {

    private BroadcastReceiver mLogoutReceiver; // 退出登录后，UserActivity 发送广播，SettingActivity就不能作为栈底，也要销毁

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initBroadcastReceiver();
    }

    private void initBroadcastReceiver() {

        mLogoutReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SettingsActivity.this.finish();
            }
        };
        IntentFilter filter = new IntentFilter(StaticUtil.LOGOUT_ACTION_NAME);

        registerReceiver(mLogoutReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mLogoutReceiver);
    }
}
