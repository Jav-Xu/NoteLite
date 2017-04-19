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
import com.javxu.notelite.gson.Photo;
import com.javxu.notelite.gson.PhotosResult;
import com.javxu.notelite.utils.JSONUtil;
import com.javxu.notelite.utils.StaticUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

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
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_gallery);
        mPhotoAdapter = new PhotoAdapter(getActivity(), mPhotoList);
        RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mPhotoAdapter);
    }

    private void initData() {
        RxVolley.get(StaticUtil.GALLERY_URL, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                PhotosResult photos = JSONUtil.handleGalleryResponse(t);
                if (photos != null && ("false".equals(photos.error))) {
                    showPhotosResult(photos);
                } else {
                    Toast.makeText(getActivity(), "获取图片信息失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                super.onFailure(error);
                Toast.makeText(getActivity(), "图片数据请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPhotosResult(PhotosResult photos) {
        for (Photo photo : photos.photoList) {
            mPhotoList.add(0, photo);
        }
        mPhotoAdapter.notifyDataSetChanged();
    }
}
