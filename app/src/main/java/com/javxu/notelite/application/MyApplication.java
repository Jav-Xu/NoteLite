package com.javxu.notelite.application;

import android.app.Application;
import android.content.Context;

import com.javxu.notelite.utils.StaticUtil;
import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePal;

import cn.bmob.v3.Bmob;

/**
 * Created by Jav-Xu on 2017/1/8.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
        CrashReport.initCrashReport(getApplicationContext(), StaticUtil.BUGLY_Key, true);
        Bmob.initialize(this, StaticUtil.BMOB_Key);
    }

    public static Context getContext() {
        return context;
    }
}
