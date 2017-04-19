package com.javxu.notelite.utils;

import com.google.gson.Gson;
import com.javxu.notelite.gson.PhotosResult;
import com.javxu.notelite.gson.WeChatsResult;
import com.javxu.notelite.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.utils
 * File Name:     JSONUtil
 * Creator:       Jav-Xu
 * Create Time:   2017/3/28 00:16
 * Description:   JSON数据解析工具类
 */

public class JSONUtil {

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

    public static PhotosResult handleGalleryResponse(String reponse) {
        PhotosResult photos = new Gson().fromJson(reponse, PhotosResult.class);
        return photos;
    }

    public static WeChatsResult handleWeChatResponse(String reponse) {
        WeChatsResult result = new Gson().fromJson(reponse, WeChatsResult.class);
        return result;
    }
}
