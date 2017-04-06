package com.javxu.notelite.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.javxu.notelite.R;
import com.javxu.notelite.adapter.PhotoPagerAdapter;
import com.javxu.notelite.bean.Photo;

import java.io.Serializable;
import java.util.List;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.activity
 * File Name:     PhotoPagerActivity
 * Creator:       Jav-Xu
 * Create Time:   2017/4/6 15:47
 * Description:   照片详情ViewPager展示
 */

public class PhotoPagerActivity extends AppCompatActivity {

    private MyPhotoViewPager mViewPager;
    private List<Photo> mPhotoList;
    private String mSelectUrl;

    public static Intent getIntent(Context context, List<Photo> photoList, String url) {
        Intent intent = new Intent(context, PhotoPagerActivity.class);
        intent.putExtra("PHOTOLIST", (Serializable) photoList);
        intent.putExtra("SELECTURL", url);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photopager);
        mViewPager = (MyPhotoViewPager) findViewById(R.id.my_photo_viewpager);

        mPhotoList = (List<Photo>) getIntent().getSerializableExtra("PHOTOLIST");
        mSelectUrl = getIntent().getStringExtra("SELECTURL");

        mViewPager.setAdapter(new PhotoPagerAdapter(this, mPhotoList));
        for (int i = 0; i < mPhotoList.size(); i++) {
            if (mSelectUrl.equals(mPhotoList.get(i).getUrl())) {
                mViewPager.setCurrentItem(i);
            }
        }
    }
}
