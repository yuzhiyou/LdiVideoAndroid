package com.zhenaixuanyan.app.videos.Activitys;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.zhenaixuanyan.app.videos.Activitys.Base.BaseActivity;
import com.zhenaixuanyan.app.videos.Adapter.VideoListAdapter;
import com.zhenaixuanyan.app.videos.Beans.Video;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Request.IndexVideoRequest;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Response.VideoResponse;
import com.zhenaixuanyan.app.videos.Net.MyRestClient;
import com.zhenaixuanyan.app.videos.R;
import com.zhenaixuanyan.app.videos.Utils.LogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;

import tcking.github.com.giraffeplayer.GiraffePlayerActivity;

@EActivity(R.layout.activity_video_list)
public class VideoListActivity extends BaseActivity {
    @RestService
    MyRestClient restClient;

    @ViewById(android.R.id.list)
    RecyclerView mRecyclerView;
    @ViewById(R.id.swipe_refresh_widget)
    SwipeRefreshLayout mSwipeRefreshWidget;

    private ArrayList<Video> mDatas = new ArrayList<>();
    VideoListAdapter videoListAdapter = null;

    @Extra("videoType")
    String videoType;

    @AfterViews
    void afterViews() {
        setTitle(R.id.navigation_bar_back_tv, R.string.all_demo);
        //mSwipeRefreshWidget.setColorScheme(R.color.color1, R.color.color2,
        //        R.color.color3, R.color.color4);
        //mSwipeRefreshWidget.setOnRefreshListener(this);

        // 这句话是为了，第一次进入页面的时候显示加载进度条
        mSwipeRefreshWidget.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

        });
        showProcessHUD(null);
        getVideoData();

    }

    private void initDatas() {
        for (int i = 0; i < 20; i++) {
            Video v = new Video();
            mDatas.add(v);
        }
    }

    private void goPlay(String url) {
        GiraffePlayerActivity.configPlayer(this).play(url);
    }

    @Click({R.id.navigation_bar_back_ib})
    void click(View v) {
        switch (v.getId()) {
            case R.id.navigation_bar_back_ib:
                finish();
                break;
        }
    }

    private void bindDatas() {
        if (videoListAdapter == null) {
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            videoListAdapter = new VideoListAdapter();
            videoListAdapter.setList(mDatas);
            mRecyclerView.setAdapter(videoListAdapter);
            mSwipeRefreshWidget.setRefreshing(false);
            videoListAdapter.setOnItemClickLitener(new VideoListAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    goPlay(mDatas.get(position).v_url);
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });
        } else {
            videoListAdapter.notifyDataSetChanged();
        }
    }

    @Background
    void getVideoData() {
        IndexVideoRequest request = new IndexVideoRequest();
        request.setUserid("1");
        try {
            VideoResponse response;
            if (videoType.equalsIgnoreCase("hot")) {
                response = restClient.getHotVideoList(request);
            } else {
                response = restClient.getSampleVideoList(request);
            }
            LogUtils.i("********" + response.data.size());
            loadVideoList(response);
        } catch (Exception e) {

        }
    }

    @UiThread
    void loadVideoList(VideoResponse response) {
        hideProcessHUD();
        LogUtils.i(">>>>>>>>>>>>>" + response.status + "-" + response.message + "-" + response.data.size());
        mDatas.clear();
        mDatas.addAll(response.data);
        bindDatas();
    }


}
