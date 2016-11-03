package com.zhenaixuanyan.app.videos.Activitys;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.zhenaixuanyan.app.videos.Activitys.Base.BaseActivity;
import com.zhenaixuanyan.app.videos.Activitys.Views.SpaceItemDecoration;
import com.zhenaixuanyan.app.videos.Adapter.HistoryVideoSection;
import com.zhenaixuanyan.app.videos.Adapter.MyVideoSection;
import com.zhenaixuanyan.app.videos.App_;
import com.zhenaixuanyan.app.videos.Beans.Video;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Request.IndexVideoRequest;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Response.VideoResponse;
import com.zhenaixuanyan.app.videos.Net.MyRestClient;
import com.zhenaixuanyan.app.videos.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import tcking.github.com.giraffeplayer.GiraffePlayerActivity;

@EActivity(R.layout.activity_video_list)
public class HistoryVideoListActivity extends BaseActivity {
    @RestService
    MyRestClient restClient;

    @ViewById(R.id.videoListRecyclerView)
    RecyclerView mRecyclerView;

    @ViewById(R.id.videoListSwipeRefreshWidget)
    SwipeRefreshLayout mSwipeRefreshWidget;

    private SectionedRecyclerViewAdapter videoListAdapter;

    private List<Video> mDatas = new ArrayList<>();

    @AfterViews
    void afterViews(){
        setTitle(R.id.navigation_bar_back_tv,R.string.recode);
        //初始化view
        viewSetting();
        //获取数据
        getVideoData();
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

        videoListAdapter = new SectionedRecyclerViewAdapter();

        mRecyclerView.addItemDecoration(new SpaceItemDecoration((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,getResources().getDisplayMetrics())));
        GridLayoutManager glm = new GridLayoutManager(this, 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(videoListAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(videoListAdapter);

    }

    @Click({R.id.navigation_bar_back_ib})
    void click(View v){
        switch (v.getId()){
            case R.id.navigation_bar_back_ib:
                finish();
                break;
            default:
                break;
        }
    }
    @Background
    void getVideoData() {
        IndexVideoRequest request = new IndexVideoRequest();
        request.setUserid(String.valueOf(App_.getInstance().mUser.getU_id()));
        try {
            VideoResponse response = restClient.getHistoryVideoList(request);
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

            videoListAdapter.removeAllSections();
            videoListAdapter.addSection(new HistoryVideoSection(this,"以前", mDatas));
            videoListAdapter.addSection(new HistoryVideoSection(this,"今天", mDatas));
            videoListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 播放视频
     * */
    private void goPlay(String url) {
        GiraffePlayerActivity.configPlayer(this).play(url);
    }
}
