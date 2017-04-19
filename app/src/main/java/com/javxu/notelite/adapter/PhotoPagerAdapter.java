package com.javxu.notelite.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.javxu.notelite.gson.Photo;
import com.javxu.notelite.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.adapter
 * File Name:     PhotoPagerAdapter
 * Creator:       Jav-Xu
 * Create Time:   2017/4/6 14:42
 * Description:   照片放大展示的ViewPager适配器
 */

public class PhotoPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Photo> mList = new ArrayList<>();

    public PhotoPagerAdapter(Context context, List<Photo> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {

        PhotoView photoView = new PhotoView(container.getContext());
        ImageUtil.loadImage(mList.get(position).url, photoView);

        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
