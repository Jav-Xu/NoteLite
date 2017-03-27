package com.javxu.notelite.bean;

import cn.bmob.v3.BmobUser;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.bean
 * File Name:     MyUser
 * Creator:       Jav-Xu
 * Create Time:   2017/3/27 16:08
 * Description:   用户实体类
 */

public class MyUser extends BmobUser {

    // Bmob 默认已包含 id username
    private int age;
    private String desc;
    private boolean sex;
    private String avatarStr;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getAvatarStr() {
        return avatarStr;
    }

    public void setAvatarStr(String avatarStr) {
        this.avatarStr = avatarStr;
    }
}
