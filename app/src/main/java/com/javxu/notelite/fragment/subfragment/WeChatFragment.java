package com.javxu.notelite.fragment.subfragment;


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
import com.javxu.notelite.bean.WeChat;
import com.javxu.notelite.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_wechat);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(++page); //不合适，应该在请求成功后再加，或者在失败后减一
            }
        });
    }

    private void initData(int page) {
        String url = "http://v.juhe.cn/weixin/query?pno=" + page + "&key=" + StaticClass.WeChat_Key;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onFailure(VolleyError error) {
                super.onFailure(error);
                Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                parseJSON(t);
            }
        });
    }

    private void parseJSON(String str) {
        try {
            JSONObject object = new JSONObject(str);
            JSONObject result = object.getJSONObject("result");
            JSONArray array = result.getJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                JSONObject news = array.getJSONObject(i);
                WeChat data = new WeChat();
                data.setTitle(news.getString("title"));
                data.setSource(news.getString("source"));
                data.setFirstImg(news.getString("firstImg"));
                data.setNewsUrl(news.getString("url"));
                mWeChatList.add(0, data);
                mWeChatAdapter = new WeChatAdapter(getActivity(), mWeChatList);
                mWeChatAdapter.notifyDataSetChanged();
                RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
                mRecyclerView.setLayoutManager(manager);
                mRecyclerView.setAdapter(mWeChatAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
