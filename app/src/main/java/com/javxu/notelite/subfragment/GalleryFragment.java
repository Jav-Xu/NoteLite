package com.javxu.notelite.subfragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.javxu.notelite.R;
import com.javxu.notelite.adapter.PhotoAdapter;
import com.javxu.notelite.bean.Photo;
import com.javxu.notelite.utils.StaticUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PhotoAdapter mPhotoAdapter;
    private List<Photo> mPhotoList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_gallery);
        initData();
        return view;
    }

    private void initData() {
        RxVolley.get(StaticUtil.GALLERY_URL, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                parseJSONAndSetAdapter(t);
            }

            @Override
            public void onFailure(VolleyError error) {
                super.onFailure(error);
                Toast.makeText(getActivity(), "图片数据请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //  "count": 50,
    //          "error": false,
    //          "results": [
    //    {
    //        "desc": "10.27",
    //            "ganhuo_id": "56cc6d1d421aa95caa70755f",
    //            "publishedAt": "2015-10-27T02:43:16.906000",
    //            "readability": "",
    //            "type": "\u798f\u5229",
    //            "url": "http://ww2.sinaimg.cn/large/7a8aed7bjw1exfffnlf2gj20hq0qoju9.jpg",
    //            "who": "\u5f20\u6db5\u5b87"
    //    },
    //    {
    //        "desc": "5.20\u3002\n520\u7231\u4f60\uff0c\u5c31\u7ed9\u4f60\u751c\u751c\u7684\u7b11\u3002\u4eca\u65e5\u7279\u63a8\uff01~~\uff08\u3065\uffe33\uffe3\uff09\u3065\u256d\u2764\uff5e",
    //            "ganhuo_id": "56cc6d1d421aa95caa7075c5",
    //            "publishedAt": "2015-05-21T10:05:06.527000",
    //            "readability": "",
    //            "type": "\u798f\u5229",
    //            "url": "http://ww1.sinaimg.cn/large/7a8aed7bgw1esahpyv86sj20hs0qomzo.jpg",
    //            "who": "\u5f20\u6db5\u5b87"
    //    }]

    private void parseJSONAndSetAdapter(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray array = jsonObject.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);

                Photo photo = new Photo();

                String photoUrl = object.getString("url");
                String photoWho = object.getString("who");
                String photoPub = object.getString("publishedAt");

                photo.setUrl(photoUrl);
                photo.setWho(photoWho);
                photo.setPubtime(photoPub);

                mPhotoList.add(0,photo);
                mPhotoAdapter = new PhotoAdapter(getActivity(), mPhotoList);
                mPhotoAdapter.notifyDataSetChanged();

                RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(manager);
                mRecyclerView.setAdapter(mPhotoAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "图片数据解析失败", Toast.LENGTH_SHORT).show();
        }
    }

}
