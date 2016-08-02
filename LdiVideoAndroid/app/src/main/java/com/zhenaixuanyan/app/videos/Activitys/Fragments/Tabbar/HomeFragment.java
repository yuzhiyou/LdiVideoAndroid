package com.zhenaixuanyan.app.videos.Activitys.Fragments.Tabbar;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.zhenaixuanyan.app.videos.Activitys.Fragments.BaseFragment;
import com.zhenaixuanyan.app.videos.Activitys.SearchListActivity_;
import com.zhenaixuanyan.app.videos.Activitys.VideoListActivity_;
import com.zhenaixuanyan.app.videos.Activitys.VideoPayActivity_;
import com.zhenaixuanyan.app.videos.Adapter.BannerAdapter;
import com.zhenaixuanyan.app.videos.Beans.Banner;
import com.zhenaixuanyan.app.videos.Beans.Video;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Response.IndexVideoResponse;
import com.zhenaixuanyan.app.videos.Net.MyRestClient;
import com.zhenaixuanyan.app.videos.R;
import com.zhenaixuanyan.app.videos.Utils.LogUtils;
import com.zhenaixuanyan.app.videos.flowwidget.CircleFlowIndicator;
import com.zhenaixuanyan.app.videos.flowwidget.ViewFlow;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;

import tcking.github.com.giraffeplayer.GiraffePlayerActivity;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {

    private BannerAdapter bannerAdapter;
    private ArrayList<Video> bannerList = new ArrayList<>();


    //网络请求
    @RestService
    MyRestClient restClient;

    @ViewById(R.id.banner_flow)
    ViewFlow banner_flow;

    @ViewById(R.id.viewflowindic)
    CircleFlowIndicator viewflowindic;
    @ViewById
    RoundedImageView iv_demo_video1;
    @ViewById
    RoundedImageView iv_demo_video2;

    @ViewById
    RoundedImageView iv_recommend_video1;

    @ViewById
    RoundedImageView iv_recommend_video2;
    @ViewById
    TextView tv_info;
    @ViewById
    TextView tv_demo_video1_title1;
    @ViewById
    TextView tv_demo_video1_title2;
    @ViewById
    TextView tv_demo_video2_title1;
    @ViewById
    TextView tv_demo_video2_title2;
    @ViewById
    TextView tv_recommend_video1_title1;
    @ViewById
    TextView tv_recommend_video1_title2;
    @ViewById
    TextView tv_recommend_video2_title1;
    @ViewById
    TextView tv_recommend_video2_title2;

    Video sample1 ;
    Video sample2 ;
    Video hot1;
    Video hot2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment_();
        return fragment;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }

    @AfterViews
    void afterViews() {
        //title
        setTitle(R.id.navigation_bar_title_tv, R.string.tabar_home);
        //进度指示
        showProcessHUD(null);
        getIndexData();
    }

    private void addBnner() {
        for (int i = 0; i < 5; i++) {
            Banner b = new Banner();
            b.setImg_url("");
            //bannerList.add(b);
        }
    }

    private void updateBanner() {
        if (bannerAdapter == null) {
            bannerAdapter = new BannerAdapter(getActivity(), bannerList);
            bannerAdapter.setBannerClickListenter(bannerClick);
            banner_flow.setAdapter(bannerAdapter);
            banner_flow.setmSideBuffer(bannerList.size()); // 实际图片张数，我的ImageAdapter实际图片张数
            if (bannerList.size() > 1) {
                banner_flow.setFlowIndicator(viewflowindic);
            }
            banner_flow.setTimeSpan(4500);
            banner_flow.setSelection(3 * 1000); // 设置初始位置
            banner_flow.startAutoFlowTimer(); // 启动自动播放
            banner_flow.setOnViewSwitchListener(new ViewFlow.ViewSwitchListener() {
                @Override
                public void onSwitched(View view, int position) {
                    tv_info.setText(bannerList.get(position % 5).v_name);
                }
            });
        } else {
            bannerAdapter.notifyDataSetChanged();
        }


    }

    @Click({R.id.iv_demo_video1, R.id.iv_demo_video2, R.id.iv_recommend_video1, R.id.iv_recommend_video2, R.id.tv_video_more, R.id.tv_recommend_more, R.id.ll_search})
    void click(View v) {
        switch (v.getId()) {
            case R.id.iv_demo_video1: {
                goPlay(sample1.v_url);
                break;
            }
            case R.id.iv_demo_video2: {
                goPlay(sample2.v_url);
                break;
            }
            case R.id.iv_recommend_video1: {
                VideoPayActivity_.intent(this).videoUrl(hot1.v_url).start();
                break;
            }
            case R.id.iv_recommend_video2: {
                VideoPayActivity_.intent(this).videoUrl(hot1.v_url).start();
                break;
            }

            case R.id.ll_search:
                SearchListActivity_.intent(this).start();
                break;
            case R.id.tv_recommend_more:
                VideoListActivity_.intent(this).videoType("hot").start();
                break;
            case R.id.tv_video_more:
                VideoListActivity_.intent(this).videoType("sample").start();
                break;
            default:
                break;
        }
    }

    private void goPlay(String url) {
        GiraffePlayerActivity.configPlayer(getActivity()).play(url);
    }

    View.OnClickListener bannerClick = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            Video video = (Video) arg0.getTag();
            LogUtils.i("url=" + video.v_url);

            goPlay(video.v_url);
        }
    };

    @Background
    void getIndexData() {
        LinkedMultiValueMap<String, String> paras = new LinkedMultiValueMap<>();
        paras.set("userid", "1");
        try {
            IndexVideoResponse response = restClient.getHomeVideoList(paras);
            loadVideoList(response);
        } catch (Exception e) {

        }
    }

    @UiThread
    void loadVideoList(IndexVideoResponse response) {
        hideProcessHUD();
        LogUtils.i(">>>>>>>>>>>>>" + response.status + "-" + response.message + "-" + response.data.ad_items.size());
        bannerList.clear();
        bannerList.addAll(response.data.ad_items.subList(0, 5));
        updateBanner();
        sample1 = response.data.sample_items.get(0);
        sample2 = response.data.sample_items.get(1);
        hot1 = response.data.hot_items.get(0);
        hot2 = response.data.hot_items.get(1);

        Picasso.with(getContext()).load(sample1.v_image).into(iv_demo_video1);
        Picasso.with(getContext()).load(sample2.v_image).into(iv_demo_video2);
        Picasso.with(getContext()).load(hot1.v_image).into(iv_recommend_video1);
        Picasso.with(getContext()).load(hot2.v_image).into(iv_recommend_video2);
        tv_demo_video1_title1.setText(sample1.v_name);
        tv_demo_video1_title2.setText(sample1.v_describe);
        tv_demo_video2_title1.setText(sample2.v_name);
        tv_demo_video2_title2.setText(sample2.v_describe);
        tv_recommend_video1_title1.setText(hot1.v_name);
        tv_recommend_video1_title2.setText(hot1.v_describe);
        tv_recommend_video2_title1.setText(hot2.v_name);
        tv_recommend_video2_title2.setText(hot2.v_describe);
    }


}
