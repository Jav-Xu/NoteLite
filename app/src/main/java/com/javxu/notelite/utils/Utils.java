package com.javxu.notelite.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.javxu.notelite.R;
import com.javxu.notelite.application.MyApplication;
import com.javxu.notelite.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Jav-Xu on 2016/12/20.
 */

public class Utils {

    public static File getExternalFileDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    public static void loadImage(String s, ImageView view) {
        Glide.with(MyApplication.getContext()).load(s).placeholder(R.mipmap.ic_launcher).into(view);
    }

    /**
     * @param reponse
     * @return
     */
    public static Weather handleWeatherResponse(String reponse) {
        try {
            JSONObject jsonObject = new JSONObject(reponse);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather data service 3.0");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            Weather weather = new Gson().fromJson(weatherContent, Weather.class);
            return weather;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 获取File对应的Uri
     *
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriFromFile(Context context, File file) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context, "com.javxu.notelite", file);
            ;
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }
}
