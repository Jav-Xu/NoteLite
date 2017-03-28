package com.javxu.notelite.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.utils
 * File Name:     FileUtil
 * Creator:       Jav-Xu
 * Create Time:   2017/3/28 00:18
 * Description:   文件工具类，注意区分RxVolley自带的FileUtils类
 */

public class FileUtil {

    /**
     * 获取 /storage/sdcard0/PICTURES
     * @return
     */
    public static File getExternalPicturesFileDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    /**
     * 获取File对应的Uri
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriFromFile(Context context, File file) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context, context.getPackageName(), file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }
}
