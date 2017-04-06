package com.javxu.notelite.bean;

import java.io.Serializable;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.bean
 * File Name:     Photo
 * Creator:       Jav-Xu
 * Create Time:   2017/4/6 15:58
 * Description:   TODO
 */

public class Photo implements Serializable {

    private String url;
    private String who;
    private String pubtime;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getPubtime() {
        return pubtime;
    }

    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
    }
}
