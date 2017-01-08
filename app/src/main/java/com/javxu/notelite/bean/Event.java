package com.javxu.notelite.bean;

/**
 * Created by Jav-Xu on 2017/1/4.
 */

public class Event {

    private String year;
    private String title;

    public Event(String year, String title) {
        this.year = year;
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
