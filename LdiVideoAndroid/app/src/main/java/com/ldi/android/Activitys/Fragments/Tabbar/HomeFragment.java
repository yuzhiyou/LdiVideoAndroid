package com.ldi.android.Activitys.Fragments.Tabbar;


import android.support.v4.app.Fragment;
import android.view.View;

import com.ldi.android.Activitys.Fragments.BaseFragment;
import com.ldi.android.Activitys.VideoListActivity_;
import com.ldi.android.Activitys.VideoPayActivity_;
import com.ldi.android.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import tcking.github.com.giraffeplayer.GiraffePlayerActivity;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {


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
    }

    @Click({R.id.iv_demo_video1,R.id.iv_demo_video2,R.id.iv_recommend_video1,R.id.iv_recommend_video2,R.id.tv_video_more,R.id.tv_recommend_more})
    void click(View v){
        switch (v.getId()){
            case R.id.iv_demo_video1:{
                VideoPayActivity_.intent(this).start();
                break;
            }
            case R.id.iv_demo_video2:{
                goPlay("");
                break;
            }
            case R.id.iv_recommend_video1:{
                goPlay("");
                break;
            }
            case R.id.iv_recommend_video2:{
                goPlay("");
                break;
            }
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

}
