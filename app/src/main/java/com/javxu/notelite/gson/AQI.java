package com.javxu.notelite.gson;

/**
 * Created by Jav-Xu on 2016/12/11.
 */

public class AQI {
    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
    }

}
