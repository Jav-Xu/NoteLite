package com.javxu.notelite.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
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
     *
     * @return
     */
    public static File getExternalPicturesFileDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    /**
     * 原始File -> 原始Uri，供拍照时使用 cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
     *
     * @param context 上下文
     * @param file    原始文件 /storage/sdcard0/Pictures/Origin_1491973368473.jpg
     * @return 原始Uri /storage/sdcard0/Pictures/Origin_1491973368473.jpg
     */
    public static Uri getUriFromFile(Context context, File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context, "com.javxu.notelite.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * 原始File -> 封装Uri，供裁剪时使用 cropIntent.setDataAndType(uri, "image/*");
     *
     * @param context   上下文
     * @param imageFile 原始文件 /storage/sdcard0/Pictures/Origin_1491973368473.jpg
     * @return 封装Uri  /external/images/media/24
     */
    public static Uri getContentUriFromFile(Context context, File imageFile) {

        String filePath = imageFile.getAbsolutePath();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}
