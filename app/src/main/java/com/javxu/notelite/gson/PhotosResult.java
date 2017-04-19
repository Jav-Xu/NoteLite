package com.javxu.notelite.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.gson
 * File Name:     PhotosResult
 * Creator:       Jav-Xu
 * Create Time:   2017/4/19 22:54
 * Description:   网络图片请求Gson整体类
 */

public class PhotosResult {
    public String error;
    @SerializedName("results")
    public List<Photo> photoList;
}
