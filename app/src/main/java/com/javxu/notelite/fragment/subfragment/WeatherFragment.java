package com.javxu.notelite.fragment.subfragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.javxu.notelite.R;
import com.javxu.notelite.gson.Forecast;
import com.javxu.notelite.gson.Weather;
import com.javxu.notelite.service.AutoUpdateService;
import com.javxu.notelite.utils.SharedUtil;
import com.javxu.notelite.utils.StaticClass;
import com.javxu.notelite.utils.Utils;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

public class WeatherFragment extends Fragment {

    private ImageView bingPicImg;
    private SwipeRefreshLayout swipeRefreshlayout;
    private NestedScrollView weatherLayout;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        bingPicImg = (ImageView) view.findViewById(R.id.bing_pic_img);
        swipeRefreshlayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshlayout.setColorSchemeResources(R.color.colorPrimary);
        weatherLayout = (NestedScrollView) view.findViewById(R.id.weather_layout);
        titleCity = (TextView) view.findViewById(R.id.title_city);
        titleUpdateTime = (TextView) view.findViewById(R.id.title_update_time);
        degreeText = (TextView) view.findViewById(R.id.degree_text);
        weatherInfoText = (TextView) view.findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) view.findViewById(R.id.forecast_layout);
        aqiText = (TextView) view.findViewById(R.id.aqi_text);
        pm25Text = (TextView) view.findViewById(R.id.pm25_text);
        comfortText = (TextView) view.findViewById(R.id.comfort_text);
        carWashText = (TextView) view.findViewById(R.id.car_wash_text);
        sportText = (TextView) view.findViewById(R.id.sport_text);

        String bingPic = SharedUtil.getString(getActivity(), "bing_pic", null);
        if (bingPic != null) {
            Utils.loadImage(bingPic, bingPicImg);
        } else {
            loadBingPic();
        }
        String weatherString = SharedUtil.getString(getActivity(), "weather", null);
        if (weatherString != null) {
            Weather weather = Utils.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            weatherLayout.setVisibility(View.INVISIBLE);
            requestBJWeather();
        }

        swipeRefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestBJWeather();
            }
        });

        return view;
    }

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        RxVolley.get(requestBingPic, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                final String bingPicUrl = t;
                SharedUtil.putString(getActivity(), "bing_pic", bingPicUrl);
                Utils.loadImage(bingPicUrl, bingPicImg);
            }

            @Override
            public void onFailure(VolleyError error) {
                super.onFailure(error);
            }
        });
    }

    public void requestBJWeather() {
        String weatherUrl = StaticClass.BJWEATHER;
        RxVolley.get(weatherUrl, new HttpCallback() {
            @Override
            public void onFailure(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_LONG).show();
                swipeRefreshlayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                final String weatherText = t;
                final Weather weather = Utils.handleWeatherResponse(weatherText);
                if (weather != null && "ok".equals(weather.status)) {
                    SharedUtil.putString(getActivity(), "weather", weatherText);
                    showWeatherInfo(weather);
                } else {
                    Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_LONG).show();
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

                View view = LayoutInflater.from(getActivity()).inflate(R.layout.forecast_item, forecastLayout, false);

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

            Intent intent = new Intent(getActivity(), AutoUpdateService.class);
            getActivity().startService(intent);
        } else {
            Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_LONG).show();
        }
    }
}
