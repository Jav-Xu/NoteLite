package com.javxu.notelite.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.utils
 * File Name:     DateUtil
 * Creator:       Jav-Xu
 * Create Time:   2017/4/1 01:43
 * Description:   时间日期工具类
 */

public class DateUtil {

    public static String dateToStr(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "年" + month + "月" + day + "日";
    }

}
