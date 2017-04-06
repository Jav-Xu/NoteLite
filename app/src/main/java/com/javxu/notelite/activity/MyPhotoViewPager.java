package com.javxu.notelite.activity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.activity
 * File Name:     MyPhotoViewPager
 * Creator:       Jav-Xu
 * Create Time:   2017/4/6 15:50
 * Description:   仅重写 onInterceptTouchEvent 的 Viewpager
 */

public class MyPhotoViewPager extends ViewPager {

    public MyPhotoViewPager(Context context) {
        super(context);
    }

    public MyPhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //return super.onInterceptTouchEvent(ev);
        // 防止 Zoom a lot 带来的异常
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
