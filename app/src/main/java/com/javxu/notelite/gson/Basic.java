package com.javxu.notelite.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jav-Xu on 2016/12/11.
 */
public class Basic {

    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String  weatherId;
    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }

}
