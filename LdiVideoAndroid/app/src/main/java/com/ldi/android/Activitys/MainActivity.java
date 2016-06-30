package com.ldi.android.Activitys;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.ldi.android.Activitys.Base.BaseActivity;
import com.ldi.android.Activitys.Fragments.Tabbar.HomeFragment;
import com.ldi.android.Activitys.Fragments.Tabbar.ProfileFragment;
import com.ldi.android.Activitys.Fragments.Tabbar.VideoFragment;
import com.ldi.android.R;
import com.ldi.android.Utils.LogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    //Tabs
    static final int NUM_ITEMS = 3;//一共四个fragment
    static final int REQUEST_CODE = 101;

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
                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        // MediaStore.EXTRA_VIDEO_QUALITY：这个值的范围是0~1，0的时候质量最差且文件最小，1的时候质量最高且文件最大。
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                        startActivityForResult(intent, REQUEST_CODE);
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

    @OnActivityResult(REQUEST_CODE)
    void onResult(int resultCode,Intent data) {
        if (resultCode == RESULT_OK) {
            if (null != data) {
                Uri uri = data.getData();
                if (uri == null) {
                    return;
                } else {

                    // 视频捕获并保存到指定的fileUri意图
                    showToast("Video saved to:\n" + data.getData());
                    try{
                        Cursor cursor = getContentResolver().query(uri,
                                new String[] { MediaStore.Video.Media.DATA },
                                null, null, null);
                        String res="";
                        if(cursor.moveToFirst()){
                            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                            res = cursor.getString(column_index);
                        }
                        cursor.close();
                        LogUtils.i("===="+res);
                        //上传视频
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

        } else if (resultCode == RESULT_CANCELED) {
            // 用户取消了视频捕捉
        } else {
            // 视频捕捉失败,建议用户
        }
    }
}
