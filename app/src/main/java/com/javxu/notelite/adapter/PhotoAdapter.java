package com.javxu.notelite.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.javxu.notelite.R;

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

    private List<String> mUrlList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private WindowManager mWindowManager;
    private int width;

    public PhotoAdapter(Context context, List<String> urlList) {
        mContext = context;
        mUrlList = urlList;
        mLayoutInflater = LayoutInflater.from(context);
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = mWindowManager.getDefaultDisplay().getWidth();
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {

        private ImageView mPhotoImageView;

        public PhotoHolder(View itemView) {
            super(itemView);
            this.mPhotoImageView = (ImageView) itemView.findViewById(R.id.imageView_photo);
        }

        public void bindHolder(String imageUrl) {
            //Utils.loadImage(imageUrl,mPhotoImageView);
            Glide.with(mContext).load(imageUrl).override(width/2,300).into(mPhotoImageView);
        }
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_photo, parent, false);
        return new PhotoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        String url = mUrlList.get(position);
        holder.bindHolder(url);
    }

    @Override
    public int getItemCount() {
        return mUrlList.size();
    }


}
