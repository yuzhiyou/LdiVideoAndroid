package com.zhenaixuanyan.app.videos.Activitys.Fragments.Tabbar;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.squareup.picasso.Picasso;
import com.zhenaixuanyan.app.videos.Activitys.Fragments.BaseFragment;
import com.zhenaixuanyan.app.videos.Activitys.SearchListActivity_;
import com.zhenaixuanyan.app.videos.Activitys.VideoListActivity_;
import com.zhenaixuanyan.app.videos.Activitys.Views.NoScrollGridView;
import com.zhenaixuanyan.app.videos.Adapter.VideoGridItemAdapter;
import com.zhenaixuanyan.app.videos.App_;
import com.zhenaixuanyan.app.videos.Beans.Video;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Response.IndexVideoResponse;
import com.zhenaixuanyan.app.videos.Net.MyRestClient;
import com.zhenaixuanyan.app.videos.R;
import com.zhenaixuanyan.app.videos.Utils.LogUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.List;

import tcking.github.com.giraffeplayer.GiraffePlayerActivity;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {
    //网络请求
    @RestService
    MyRestClient restClient;

    @ViewById(R.id.homeRecommendBanner)
    ConvenientBanner homeRecommendBanner;
    //示例
    @ViewById(R.id.homeSimpleVideoGridView)
    NoScrollGridView simpleVideoGridView;

    private VideoGridItemAdapter simpleVideoAdapter;
    //热点
    @ViewById(R.id.homeHotVideoGridView)
    NoScrollGridView hotVideoGridView;

    private VideoGridItemAdapter hotVideoAdapter;

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
        //view set
        viewSetting();
        //进度指示
        showProcessHUD(null);
        getIndexData();
    }
    /**
     * View设置
     * */
    private void viewSetting(){
        //示例
        simpleVideoAdapter = new VideoGridItemAdapter(getActivity());
        simpleVideoAdapter.setList(new ArrayList<Video>());
        simpleVideoGridView.setAdapter(simpleVideoAdapter);
        //热点
        hotVideoAdapter = new VideoGridItemAdapter(getActivity());
        hotVideoAdapter.setList(new ArrayList<Video>());
        hotVideoGridView.setAdapter(hotVideoAdapter);
    }
    /**
     * 更新banner
     * */
    private void updateBanner(final List<Video> data) {
        homeRecommendBanner.setPages(
                new CBViewHolderCreator() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, data)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.banner_page_indicator_activie, R.drawable.banner_page_indicator})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        homeRecommendBanner.startTurning(3000);
        homeRecommendBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Video video = data.get(position);
                LogUtils.i("url=" + video.getV_url());

                goPlay(video.getV_url());
            }
        });
    }

    @Click({ R.id.homeSimpleVideoSectionMore, R.id.homeHotVideoSectionMore, R.id.homeNavigationBarItemSearch})
    void click(View v) {
        switch (v.getId()) {
            case R.id.homeNavigationBarItemSearch:
                SearchListActivity_.intent(this).start();
                break;
            case R.id.homeHotVideoSectionMore:
                VideoListActivity_.intent(this).extra("videoType","hot").start();
                break;
            case R.id.homeSimpleVideoSectionMore:
                VideoListActivity_.intent(this).extra("videoType","sample").start();
                break;
            default:
                break;
        }
    }

    private void goPlay(String url) {
        GiraffePlayerActivity.configPlayer(getActivity()).play(url);
    }

    @Background
    void getIndexData() {
        LinkedMultiValueMap<String, String> paras = new LinkedMultiValueMap<>();
        paras.set("userid", String.valueOf(App_.getInstance().mUser.getU_id()));
        try {
            IndexVideoResponse response = restClient.getHomeVideoList(paras);
            loadVideoList(response);
        } catch (Exception e) {
            loadVideoList(null);
        }
    }

    @UiThread
    void loadVideoList(IndexVideoResponse response) {
        hideProcessHUD();
        if (response != null) {
            //ad
            updateBanner(response.getData().getAd_items());
            //simple videos
            simpleVideoAdapter.setList(response.getData().getSample_items());
            simpleVideoAdapter.notifyDataSetChanged();
            //hot videos
            hotVideoAdapter.setList(response.getData().getHot_items());
            hotVideoAdapter.notifyDataSetChanged();
        }
    }

    /**
     * ad adapter
     *
     * **/
    class LocalImageHolderView implements Holder<Video> {
        private View holder;
        @Override
        public View createView(Context context) {
            holder = LayoutInflater.from(context).inflate(R.layout.banner_item,null);
            return holder;
        }

        @Override
        public void UpdateUI(Context context, final int position, Video data) {
            Picasso.with(context)
                    .load(data.getV_image_thum())
                    .placeholder(R.mipmap.no_video_image)
                    .into((ImageView) holder.findViewById(R.id.bannerItemImageIv));
            ((TextView) holder.findViewById(R.id.bannerItemDescTv)).setText(data.getV_name());
        }
    }
}
