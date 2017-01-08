package com.javxu.notelite.utils;

import android.os.Environment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.google.gson.Gson;
import com.javxu.notelite.R;
import com.javxu.notelite.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.UUID;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Jav-Xu on 2016/12/20.
 */

public class Utils {

    public static File getExternalFileDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    public static void loadImage(String s, ImageView view) {
        Glide.with(MyApplication.getContext()).load(s).signature(new StringSignature(UUID.randomUUID().toString()))
                .placeholder(R.mipmap.ic_launcher).into(view);
    }

    public static void sendOkHttpRequest(String url, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 处理并解析历史上的今天JSON数据
     *
     * @param reponse
     * @return [List(dieEvent), List(birthEvent), List(bangEvent)]
     */
    /*public static List<List<Event>> handleTodayResponse(String reponse) {
        List<List<Event>> threeEventList = null;
        if (!TextUtils.isEmpty(reponse)) {
            try {
                JSONObject object = new JSONObject(reponse);
                String month = object.getString("month");
                String day = object.getString("day");
                JSONArray array = object.getJSONArray("res");

                for (int i = 0; i < array.length(); i++) {
                    JSONObject eventObject = array.getJSONObject(i);
                    String events = eventObject.getString("lists");
                    List<Event> eventList = new Gson().fromJson(events, new TypeToken<List<Event>>() {
                    }.getType());
                    threeEventList.add(eventList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return threeEventList;
    }*/

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
}
