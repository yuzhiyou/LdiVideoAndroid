package com.ldi.android.Activitys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.ldi.android.Activitys.Base.BaseActivity;
import com.ldi.android.Activitys.Fragments.Tabbar.HomeFragment;
import com.ldi.android.Activitys.Fragments.Tabbar.ProfileFragment;
import com.ldi.android.Activitys.Fragments.Tabbar.VideoFragment;
import com.ldi.android.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    //Tabs
    static final int NUM_ITEMS = 3;//一共四个fragment

    @ViewById(R.id.mainTabBarViewContainer)
    FrameLayout mainTabBarViewContainer;

    @ViewById(R.id.mainTabBarRadioGroup)
    RadioGroup mainTabBarRadioGroup;

    @AfterViews
    void afterViews(){
        settingViews();
    }
    /**
     * profile view
     *      设置view
     * */
    @UiThread
    void settingViews() {
        mainTabBarRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index;
                switch (checkedId){
                    case R.id.mainProductTabBarItem:
                        index = 0;
                        break;
                    case R.id.mainCardTabBarItem:
                        index = 1;
                        break;
                    default:
                        index = 2;
                        break;
                }
                //通过fragments这个adapter还有index来替换帧布局中的内容
                Fragment fragment = (Fragment) fragments.instantiateItem(mainTabBarViewContainer, index);
                //一开始将帧布局中 的内容设置为第一个
                fragments.setPrimaryItem(mainTabBarViewContainer, 0, fragment);
                fragments.finishUpdate(mainTabBarViewContainer);
            }
        });

        mainTabBarRadioGroup.check(R.id.mainProductTabBarItem);
    }
    //用adapter来管理四个Fragment界面的变化。
    FragmentStatePagerAdapter fragments = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = HomeFragment.newInstance();
                    break;
                case 1:
                    fragment = VideoFragment.newInstance();
                    break;
                default:
                    fragment = ProfileFragment.newInstance();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    };
}
