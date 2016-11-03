package com.zhenaixuanyan.app.videos.Activitys;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zhenaixuanyan.app.videos.Activitys.Fragments.Guide.GuideFragment;
import com.zhenaixuanyan.app.videos.Adapter.GuidePageAdapter;
import com.zhenaixuanyan.app.videos.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_user_guide)
public class UserGuideActivity extends AppCompatActivity {
    private List<GuideFragment> mFragments = new ArrayList<>();
    private GuidePageAdapter adapter;

    @ViewById(R.id.guide_viewpager)
    ViewPager mViewpager;

    @AfterViews
    void afterViews(){
        initFragments();
    }


    /**
     *  初始化fragments
     */
    private void initFragments() {
        mFragments.add(GuideFragment.newInstance(R.mipmap.guide1_page,false));
        mFragments.add(GuideFragment.newInstance(R.mipmap.guide2_page,false));
        mFragments.add(GuideFragment.newInstance(R.mipmap.guide3_page,true));
        adapter = new GuidePageAdapter(getSupportFragmentManager(),mFragments);
        mViewpager.setAdapter(adapter);
    }

}
