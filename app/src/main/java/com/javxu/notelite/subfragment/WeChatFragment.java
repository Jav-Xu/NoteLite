package com.javxu.notelite.subfragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.javxu.notelite.R;
import com.javxu.notelite.adapter.WeChatAdapter;
import com.javxu.notelite.gson.WeChat;
import com.javxu.notelite.gson.WeChatsResult;
import com.javxu.notelite.utils.JSONUtil;
import com.javxu.notelite.utils.StaticUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import java.util.ArrayList;
import java.util.List;

public class WeChatFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<WeChat> mWeChatList = new ArrayList<>();
    private WeChatAdapter mWeChatAdapter;

    private int page = 1; //微信精选页码，首次默认加载第一页

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat, container, false);
        initView(view);
        initData(page);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_wechat);
        mWeChatAdapter = new WeChatAdapter(getActivity(), mWeChatList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mWeChatAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_wechat);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(++page);
                //TODO 不合适，应该在请求成功后再加，或者在失败后减一
            }
        });
    }

    private void initData(int page) {
        String url = "http://v.juhe.cn/weixin/query?pno=" + page + "&key=" + StaticUtil.WeChat_Key;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                WeChatsResult result = JSONUtil.handleWeChatResponse(t);
                if (result != null && (0 == result.error_code)) {
                    showWeChatsResult(result);
                } else {
                    Toast.makeText(getActivity(), "获取文章信息失败", Toast.LENGTH_LONG).show();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(VolleyError error) {
                super.onFailure(error);
                Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showWeChatsResult(WeChatsResult result) {
        for (WeChat weChat : result.weChats.weChatList) {
            mWeChatList.add(0, weChat);
        }
        mWeChatAdapter.notifyDataSetChanged();
    }

}
