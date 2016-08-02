package com.zhenaixuanyan.app.videos.Activitys;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

import com.zhenaixuanyan.app.videos.Activitys.Base.BaseActivity;
import com.zhenaixuanyan.app.videos.Adapter.VideoListAdapter;
import com.zhenaixuanyan.app.videos.Beans.Video;
import com.zhenaixuanyan.app.videos.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import tcking.github.com.giraffeplayer.GiraffePlayerActivity;

@EActivity(R.layout.activity_search_list)
public class SearchListActivity extends BaseActivity {

    @ViewById(android.R.id.list)
    RecyclerView mRecyclerView;

    @ViewById(R.id.et_search)
    EditText et_search;

    @ViewById(R.id.swipe_refresh_widget)
    SwipeRefreshLayout mSwipeRefreshWidget;

    private ArrayList<Video> mDatas = new ArrayList<Video>();

    @AfterViews
    void afterViews(){
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
        initDatas();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        VideoListAdapter adapter = new VideoListAdapter();
        adapter.setList(mDatas);
        mRecyclerView.setAdapter(adapter);
        mSwipeRefreshWidget.setRefreshing(false);
        adapter.setOnItemClickLitener(new VideoListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                goPlay("");
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private void initDatas(){
        for (int i=0;i<20;i++){
            Video v = new Video();
            v.v_image = "http://pic2.ooopic.com/11/11/41/37b1OOOPIC87.jpg";
            v.v_name = "v2";
            v.v_describe="ddd1";
            mDatas.add(v);
        }
    }
    private void goPlay(String url){
        GiraffePlayerActivity.configPlayer(this).play("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
    }

    @Click({R.id.ib_clear})
    void click(View v){
        switch (v.getId()){
            case R.id.ib_clear:
                et_search.setText("");
                break;
        }
    }
}
