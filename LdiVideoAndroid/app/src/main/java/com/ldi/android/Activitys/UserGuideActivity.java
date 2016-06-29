package com.ldi.android.Activitys;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ldi.android.Activitys.Fragments.Guide.GuideFragment;
import com.ldi.android.Adapter.GuidePageAdapter;
import com.ldi.android.R;

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
        mFragments.add(GuideFragment.newInstance(R.color.guide_page1_color,R.mipmap.guide1_logo,R.mipmap.guide1_pointer));
        mFragments.add(GuideFragment.newInstance(R.color.guide_page2_color,R.mipmap.guide2_logo,R.mipmap.guide2_pointer));
        mFragments.add(GuideFragment.newInstance(R.color.guide_page3_color,R.mipmap.guide3_logo,R.mipmap.guide3_pointer,true));
        adapter = new GuidePageAdapter(getSupportFragmentManager(),mFragments);
        mViewpager.setAdapter(adapter);
    }

}
