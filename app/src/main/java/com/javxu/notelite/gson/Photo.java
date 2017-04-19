package com.javxu.notelite.gson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.bean
 * File Name:     Photo
 * Creator:       Jav-Xu
 * Create Time:   2017/4/6 15:58
 * Description:   网络图片请求Gson处理实例类
 */

public class Photo implements Serializable {
    public String url;
    public String who;
    @SerializedName("publishedAt")
    public String pubtime;
}
