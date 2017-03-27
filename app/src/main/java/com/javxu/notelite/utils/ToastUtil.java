package com.javxu.notelite.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.utils
 * File Name:     ToastUtil
 * Creator:       Jav-Xu
 * Create Time:   2017/3/27 18:29
 * Description:   Toast工具类
 */

public class ToastUtil {

    private static Toast toast;

    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
