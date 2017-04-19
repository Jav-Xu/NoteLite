package com.javxu.notelite.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.gson
 * File Name:     WeChatsResult
 * Creator:       Jav-Xu
 * Create Time:   2017/4/20 00:05
 * Description:   微信精选请求Gson整体类
 */

public class WeChatsResult {
    public int error_code;
    @SerializedName("result")
    public WeChats weChats;
}
