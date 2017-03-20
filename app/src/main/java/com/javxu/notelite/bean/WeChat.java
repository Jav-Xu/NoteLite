package com.javxu.notelite.bean;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.bean
 * File Name:     WeChat
 * Creator:       Jav-Xu
 * Create Time:   2017/3/20 14:24
 * Description:   微信精选实体类
 */

public class WeChat {
    private String title;
    private String source;
    private String firstImg;
    private String newsUrl;

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFirstImg() {
        return firstImg;
    }

    public void setFirstImg(String firstImg) {
        this.firstImg = firstImg;
    }
}
