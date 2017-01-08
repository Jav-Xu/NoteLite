package com.javxu.notelite.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jav-Xu on 2016/12/11.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
