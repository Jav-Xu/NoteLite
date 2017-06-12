package com.javxu.notelite.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.javxu.notelite.R;
import com.javxu.notelite.base.BaseActivity;
import com.javxu.notelite.gson.Forecast;
import com.javxu.notelite.gson.Weather;
import com.javxu.notelite.service.AutoUpdateService;
import com.javxu.notelite.utils.ImageUtil;
import com.javxu.notelite.utils.JSONUtil;
import com.javxu.notelite.utils.SharedUtil;
import com.javxu.notelite.utils.StaticUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.activity
 * File Name:     WeatherActivity
 * Creator:       Jav-Xu
 * Create Time:   2017/4/28 15:28
 * Description:   北京天气Activity
 */

public class WeatherActivity extends BaseActivity {

    private ImageView bingPicImg;
    private SwipeRefreshLayout swipeRefreshlayout;
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initStatusBar();
        initView();
        initData();
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initView() {
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        swipeRefreshlayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshlayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestBJWeather();
            }
        });
    }

    private void initData() {
        String bingPic = SharedUtil.getString(this, "bing_pic", null);
        if (bingPic != null) {
            ImageUtil.loadImage(bingPic, bingPicImg);
        } else {
            loadBingPic();
        }
        String weatherString = SharedUtil.getString(this, "weather", null);
        if (weatherString != null) {
            Weather weather = JSONUtil.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            weatherLayout.setVisibility(View.INVISIBLE);
            requestBJWeather();
        }
    }

    private void loadBingPic() {
        String requestBingPic = StaticUtil.BINGPIC;
        RxVolley.get(requestBingPic, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                final String bingPicUrl = t;
                SharedUtil.putString(WeatherActivity.this, "bing_pic", bingPicUrl);
                ImageUtil.loadImage(bingPicUrl, bingPicImg);
            }

            @Override
            public void onFailure(VolleyError error) {
                super.onFailure(error);
            }
        });
    }

    public void requestBJWeather() {
        String weatherUrl = StaticUtil.BJWEATHER;
        RxVolley.get(weatherUrl, new HttpCallback() {
            @Override
            public void onFailure(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_LONG).show();
                swipeRefreshlayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                final String weatherText = t;
                final Weather weather = JSONUtil.handleWeatherResponse(weatherText);
                if (weather != null && "ok".equals(weather.status)) {
                    SharedUtil.putString(WeatherActivity.this, "weather", weatherText);
                    showWeatherInfo(weather);
                } else {
                    Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_LONG).show();
                }
                swipeRefreshlayout.setRefreshing(false);
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        if (weather != null && "ok".equals(weather.status)) {

            String cityName = weather.basic.cityName;
            String updateTime = weather.basic.update.updateTime.split(" ")[1];
            String degree = weather.now.temperature + "℃";
            String weatherInfo = weather.now.more.info;

            titleCity.setText(cityName);
            titleUpdateTime.setText(updateTime);
            degreeText.setText(degree);
            weatherInfoText.setText(weatherInfo);

            forecastLayout.removeAllViews();

            for (Forecast forecast : weather.forecastList) {

                View view = LayoutInflater.from(WeatherActivity.this)
                        .inflate(R.layout.item_forecast, forecastLayout, false);

                TextView dateText = (TextView) view.findViewById(R.id.date_text);
                TextView infoText = (TextView) view.findViewById(R.id.info_text);
                TextView maxText = (TextView) view.findViewById(R.id.max_text);
                TextView minText = (TextView) view.findViewById(R.id.min_text);

                dateText.setText(forecast.date);
                infoText.setText(forecast.more.info);
                maxText.setText(forecast.temperature.max);
                minText.setText(forecast.temperature.min);

                forecastLayout.addView(view);
            }

            if (weather.aqi != null) {
                aqiText.setText(weather.aqi.city.aqi);
                pm25Text.setText(weather.aqi.city.pm25);
            }

            String comfort = "舒适度：" + weather.suggestion.comfort.info;
            String carWash = "洗车指数：" + weather.suggestion.carWash.info;
            String sport = "运行建议：" + weather.suggestion.sport.info;

            comfortText.setText(comfort);
            carWashText.setText(carWash);
            sportText.setText(sport);

            weatherLayout.setVisibility(View.VISIBLE);

            Intent intent = new Intent(WeatherActivity.this, AutoUpdateService.class);
            WeatherActivity.this.startService(intent);
        } else {
            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_LONG).show();
        }
    }
}


