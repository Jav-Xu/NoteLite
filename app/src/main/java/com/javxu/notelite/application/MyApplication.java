package com.javxu.notelite.application;

import android.app.Application;
import android.content.Context;

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
    }

    public static Context getContext() {
        return context;
    }
}
