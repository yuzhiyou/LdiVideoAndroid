package com.ldi.android.Activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.ldi.android.Activitys.Base.BaseActivity;
import com.ldi.android.EventBus.MessageEvent;
import com.ldi.android.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

@EActivity(R.layout.activity_user_enter)
public class UserEnterActivity extends BaseActivity {
    @AfterViews
    void afterViews(){
        //Eventbus
        EventBus.getDefault().register(this);
        //statusbar
        setSystemTintColorBar(R.color.launch_bg_color);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Click({R.id.userGuideLoginBtn,R.id.userGuideRegisterBtn})
    void click(View v){
        switch (v.getId()){
            case R.id.userGuideRegisterBtn:        //注册
                RegisterActivity_.intent(this).start();
                break;
            case R.id.userGuideLoginBtn:     //登录
                LoginActivity_.intent(this).start();
                break;
            default:
                break;
        }
    }
    @Subscribe
    public void onEvent(MessageEvent event) {
        if (event.getId() == MessageEvent.LOGIN_SUCCESS_EVENT){
            finish();
        }
    }
}
