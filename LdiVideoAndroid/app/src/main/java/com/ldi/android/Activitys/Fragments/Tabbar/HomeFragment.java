package com.ldi.android.Activitys.Fragments.Tabbar;


import android.support.v4.app.Fragment;
import android.view.View;

import com.ldi.android.Activitys.Fragments.BaseFragment;
import com.ldi.android.Activitys.SearchListActivity_;
import com.ldi.android.Activitys.VideoListActivity_;
import com.ldi.android.Activitys.VideoPayActivity_;
import com.ldi.android.Adapter.BannerAdapter;
import com.ldi.android.Beans.Banner;
import com.ldi.android.R;
import com.ldi.android.flowwidget.CircleFlowIndicator;
import com.ldi.android.flowwidget.ViewFlow;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import tcking.github.com.giraffeplayer.GiraffePlayerActivity;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {

    private BannerAdapter bannerAdapter;
    private ArrayList<Banner> bannerList = new ArrayList<Banner>();


    @ViewById(R.id.banner_flow)
    ViewFlow banner_flow;

    @ViewById(R.id.viewflowindic)
    CircleFlowIndicator viewflowindic;


    public HomeFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
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
        setTitle(R.id.navigation_bar_title_tv,R.string.tabar_home);
        addBnner();
        updateBanner();
    }

    private void addBnner(){
        for (int i = 0; i < 5; i++) {
            Banner b = new Banner();
            b.setImg_url("");
            bannerList.add(b);
        }
    }

    private void updateBanner(){
        if (bannerAdapter == null){
            bannerAdapter = new BannerAdapter(getActivity(),bannerList);
        }
        bannerAdapter.setBannerClickListenter(bannerClick);
        banner_flow.setAdapter(bannerAdapter);
        banner_flow.setmSideBuffer(bannerList.size()); // 实际图片张数，我的ImageAdapter实际图片张数
        if (bannerList.size() > 1) {
            banner_flow.setFlowIndicator(viewflowindic);
        }
        banner_flow.setTimeSpan(4500);
        banner_flow.setSelection(3 * 1000); // 设置初始位置
        banner_flow.startAutoFlowTimer(); // 启动自动播放

    }

    @Click({R.id.iv_demo_video1,R.id.iv_demo_video2,R.id.iv_recommend_video1,R.id.iv_recommend_video2,R.id.tv_video_more,R.id.tv_recommend_more,R.id.ll_search})
    void click(View v){
        switch (v.getId()){
            case R.id.iv_demo_video1:{
                VideoPayActivity_.intent(this).start();
                break;
            }
            case R.id.iv_demo_video2:{
                VideoPayActivity_.intent(this).start();
                break;
            }
            case R.id.iv_recommend_video1:{
                VideoPayActivity_.intent(this).start();
                break;
            }
            case R.id.iv_recommend_video2:{
                VideoPayActivity_.intent(this).start();
                break;
            }

            case R.id.ll_search:
                SearchListActivity_.intent(this).start();
                break;
            case R.id.tv_recommend_more:
            case R.id.tv_video_more:
                VideoListActivity_.intent(this).start();
                break;
            default:break;
        }
    }

    private void goPlay(String url){
        GiraffePlayerActivity.configPlayer(getActivity()).play("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
    }

    View.OnClickListener bannerClick = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
           goPlay(null);
        }
    };

}
