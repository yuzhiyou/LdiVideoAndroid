package com.zhenaixuanyan.app.videos.Activitys;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.zhenaixuanyan.app.videos.Activitys.Base.BaseActivity;
import com.zhenaixuanyan.app.videos.Activitys.Views.SpaceItemDecoration;
import com.zhenaixuanyan.app.videos.Adapter.VideoListAdapter;
import com.zhenaixuanyan.app.videos.App_;
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
import java.util.List;

import tcking.github.com.giraffeplayer.GiraffePlayerActivity;

@EActivity(R.layout.activity_video_list)
public class VideoListActivity extends BaseActivity {
    @RestService
    MyRestClient restClient;

    @ViewById(R.id.videoListRecyclerView)
    RecyclerView mRecyclerView;

    @ViewById(R.id.videoListSwipeRefreshWidget)
    SwipeRefreshLayout mSwipeRefreshWidget;

    private List<Video> mDatas = new ArrayList<>();
    VideoListAdapter videoListAdapter = null;

    @Extra("videoType")
    String videoType;

    @AfterViews
    void afterViews() {
        //标题
        setTitle(R.id.navigation_bar_back_tv, videoType.equalsIgnoreCase("hot")?R.string.all_recommend:R.string.all_demo);
        //初始化控件
        viewSetting();
        //获取数据
        getVideoData();
    }
    /**
     * 播放视频
     * */
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
    /**
     * view setting
     * */
    private void viewSetting(){
        //自动刷新
        mSwipeRefreshWidget.setColorSchemeResources(R.color.red_btn_bg_color);
        mSwipeRefreshWidget.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshWidget.setRefreshing(true);
            }
        });

        //刷新监听
        mSwipeRefreshWidget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getVideoData();
            }
        });

        videoListAdapter = new VideoListAdapter(this);
        videoListAdapter.setList(mDatas);
        videoListAdapter.setOnItemClickListener(new VideoListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                goPlay(mDatas.get(position).getV_url());
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        mRecyclerView.addItemDecoration(new SpaceItemDecoration((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,getResources().getDisplayMetrics())));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(videoListAdapter);

    }

    @Background
    void getVideoData() {
        IndexVideoRequest request = new IndexVideoRequest();
        request.setUserid(String.valueOf(App_.getInstance().mUser.getU_id()));
        try {
            VideoResponse response;
            if (videoType.equalsIgnoreCase("hot")) {
                response = restClient.getHotVideoList(request);
            } else {
                response = restClient.getSampleVideoList(request);
            }
            loadVideoList(response);
        } catch (Exception e) {
            loadVideoList(null);
        }
    }

    @UiThread
    void loadVideoList(VideoResponse response) {
        mSwipeRefreshWidget.setRefreshing(false);
        if (response != null) {
            mDatas.clear();
            mDatas.addAll(response.data);
            videoListAdapter.notifyDataSetChanged();
        }
    }


}
