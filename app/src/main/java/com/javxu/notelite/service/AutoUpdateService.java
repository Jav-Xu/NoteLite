package com.javxu.notelite.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.javxu.notelite.gson.Weather;
import com.javxu.notelite.utils.JSONUtil;
import com.javxu.notelite.utils.SharedUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;


/**
 * Created by Jav-Xu on 2017/1/8.
 */

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        updateWeatherCache();
        updateBingPicCache();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 60 * 60 * 1000;
        long triggerTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeatherCache() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = pref.getString("weather", null);
        if (weatherString != null) {
            String weatherUrl = "https://api.heweather.com/x3/weather?cityid=CN101010100&key=bc0418b57b2d4918819d3974ac1285d9";
            RxVolley.get(weatherUrl, new HttpCallback() {
                @Override
                public void onFailure(VolleyError error) {
                    super.onFailure(error);
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    String reponseText = t;
                    Weather weather = JSONUtil.handleWeatherResponse(reponseText);
                    if (weather != null && "ok".equals(weather.status)) {
                        SharedUtil.putString(getApplicationContext(), "weather", reponseText);
                    }
                }
            });
        }
    }

    private void updateBingPicCache() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        RxVolley.get(requestBingPic, new HttpCallback() {
            @Override
            public void onFailure(VolleyError error) {
                super.onFailure(error);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                String bingPicUrl = t;
                SharedUtil.putString(getApplicationContext(), "bing_pic", bingPicUrl);

            }
        });
    }
}