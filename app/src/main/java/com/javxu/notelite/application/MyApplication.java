package com.javxu.notelite.application;

import android.app.Application;
import android.content.Context;

import com.javxu.notelite.utils.StaticClass;
import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePal;

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
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_Key, true);
    }

    public static Context getContext() {
        return context;
    }
}
