package com.javxu.notelite.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.javxu.notelite.R;
import com.javxu.notelite.activity.WeChatActivity;
import com.javxu.notelite.bean.WeChat;

import java.util.List;

/**
 * Project Name:  NoteLite
 * Package Name:  com.javxu.notelite.adapter
 * File Name:     WeChatAdapter
 * Creator:       Jav-Xu
 * Create Time:   2017/3/20 14:24
 * Description:   TODO
 */

public class WeChatAdapter extends RecyclerView.Adapter<WeChatAdapter.WeChatHolder> {

    private Context mContext;
    private List<WeChat> mWeChatList;
    private LayoutInflater mLayoutInflater;

    public WeChatAdapter(Context context, List<WeChat> weChatList) {
        this.mContext = context;
        this.mWeChatList = weChatList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public class WeChatHolder extends RecyclerView.ViewHolder {

        private View mItemView;
        private ImageView mWeChatIcon;
        private TextView mWeChatTitle;
        private TextView mWeChatSource;

        public WeChatHolder(View itemView) {
            super(itemView);
            this.mItemView = itemView;
            mWeChatIcon = (ImageView) itemView.findViewById(R.id.iv_wechat_icon);
            mWeChatTitle = (TextView) itemView.findViewById(R.id.tv_wechat_title);
            mWeChatSource = (TextView) itemView.findViewById(R.id.tv_wechat_source);
        }

        public void bindHolder(final WeChat wechat) {
            if (wechat != null && (!TextUtils.isEmpty(wechat.getFirstImg()))) {
                Glide.with(mContext)
                        .load(wechat.getFirstImg())
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .centerCrop()
                        .into(mWeChatIcon);
                mWeChatTitle.setText(wechat.getTitle());
                mWeChatSource.setText(wechat.getSource());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = WeChatActivity.getIntent(mContext, wechat);
                        mContext.startActivity(intent);
                    }
                });
            }
        }

    }

    @Override
    public WeChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_wechat, parent, false);
        return new WeChatHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WeChatHolder holder, int position) {
        WeChat weChat = mWeChatList.get(position);
        holder.bindHolder(weChat);
    }

    @Override
    public int getItemCount() {
        return mWeChatList == null ? 0 : mWeChatList.size();
    }

}
