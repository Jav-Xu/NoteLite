package com.javxu.notelite.utils;

import android.util.Log;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.utils
 * File Name:     LoogUtil
 * Creator:       Jav-Xu
 * Create Time:   2017/3/20 11:48
 * Description:   TODO
 */

public class LoogUtil {
    //DEBUG 开关
    public static final boolean DEBUG = true;

    public static final String TAG = "NoteLite";

    public static void d(String str) {
        if (DEBUG) {
            Log.d(TAG, str);
        }
    }

    public static void i(String str) {
        if (DEBUG) {
            Log.i(TAG, str);
        }
    }

    public static void w(String str) {
        if (DEBUG) {
            Log.w(TAG, str);
        }
    }

    public static void e(String str) {
        if (DEBUG) {
            Log.e(TAG, str);
        }
    }
}
