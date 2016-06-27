package com.ldi.android.Activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.ldi.android.Activitys.Base.BaseActivity;
import com.ldi.android.Net.MyRestClient;
import com.ldi.android.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.rest.spring.annotations.RestService;

@EActivity(R.layout.activity_user_guide)
public class UserGuideActivity extends BaseActivity {

    @RestService
    MyRestClient restClient;

    @AfterViews
    void afterViews(){
        setSystemTintColorBar(R.color.launch_bg_color);
    }


    @Click({R.id.userGuideLoginBtn,R.id.userGuideRegisterBtn})
    void click(View v){
        switch (v.getId()){
            case R.id.userGuideLoginBtn:        //注册

                break;
            case R.id.userGuideRegisterBtn:     //登录

                break;
            default:
                break;
        }
    }

}
