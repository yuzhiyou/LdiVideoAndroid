package com.zhenaixuanyan.app.videos.Activitys.Fragments.Tabbar;


import android.support.v4.app.Fragment;
import android.view.View;

import com.zhenaixuanyan.app.videos.Activitys.Fragments.BaseFragment;
import com.zhenaixuanyan.app.videos.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_video)
public class VideoFragment extends BaseFragment {


    public VideoFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment_();
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
        setTitle(R.id.navigation_bar_title_tv,R.string.tabar_video);
    }
}
