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
import com.javxu.notelite.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeChatFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<WeChat> mWeChatList = new ArrayList<>();
    private WeChatAdapter mWeChatAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_wechat);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_wechat);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    private void initData() {
        String url = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WeChat_Key;
        Utils.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parseJSON(s);
                    }
                });
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
                mWeChatList.add(data);
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
