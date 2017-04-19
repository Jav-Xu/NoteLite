package com.javxu.notelite.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.javxu.notelite.R;
import com.javxu.notelite.activity.PhotoPagerActivity;
import com.javxu.notelite.gson.Photo;

import java.util.List;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.adapter
 * File Name:     PhotoAdapter
 * Creator:       Jav-Xu
 * Create Time:   2017/3/27 12:28
 * Description:   图片适配器
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private List<Photo> mPhotoList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private WindowManager mWindowManager;
    private int width;

    public PhotoAdapter(Context context, List<Photo> photoList) {
        mContext = context;
        mPhotoList = photoList;
        mLayoutInflater = LayoutInflater.from(context);
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = mWindowManager.getDefaultDisplay().getWidth();
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {

        private ImageView mPhotoImageView;

        public PhotoHolder(View itemView) {
            super(itemView);
            mPhotoImageView = (ImageView) itemView.findViewById(R.id.imageView_photo);
        }

        public void bindHolder(final Photo photo) {
            //ImageUtil.loadImage(imageUrl,mPhotoImageView);
            final String imageUrl = photo.url;
            Glide.with(mContext).load(imageUrl).override(width / 2, 300).into(mPhotoImageView);
            mPhotoImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = PhotoPagerActivity.getIntent(mContext, mPhotoList, photo.url);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_photo, parent, false);
        return new PhotoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        Photo photo = mPhotoList.get(position);
        holder.bindHolder(photo);
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }


}
