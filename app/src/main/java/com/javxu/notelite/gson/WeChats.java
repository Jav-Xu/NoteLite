package com.javxu.notelite.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.gson
 * File Name:     WeChats
 * Creator:       Jav-Xu
 * Create Time:   2017/4/19 18:56
 * Description:   微信精选请求Gson处理细化类
 */

public class WeChats {
    @SerializedName("list")
    public List<WeChat> weChatList;
}
