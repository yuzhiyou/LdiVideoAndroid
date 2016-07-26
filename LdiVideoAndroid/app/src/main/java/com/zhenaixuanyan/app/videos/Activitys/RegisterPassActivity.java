package com.zhenaixuanyan.app.videos.Activitys;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhenaixuanyan.app.videos.Activitys.Base.BaseActivity;
import com.zhenaixuanyan.app.videos.App_;
import com.zhenaixuanyan.app.videos.Beans.User;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Request.UserRegisterRequest;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Response.UserRegisterResponse;
import com.zhenaixuanyan.app.videos.Constants;
import com.zhenaixuanyan.app.videos.EventBus.MessageEvent;
import com.zhenaixuanyan.app.videos.Net.MyRestClient;
import com.zhenaixuanyan.app.videos.R;
import com.zhenaixuanyan.app.videos.Utils.LogUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.greenrobot.eventbus.EventBus;

@EActivity(R.layout.activity_register_pass)
public class RegisterPassActivity extends BaseActivity {
    //网络请求
    @RestService
    MyRestClient restClient;

    //密码
    @ViewById(R.id.passwordET)
    EditText passwordET;
    //确认密码
    @ViewById(R.id.passwordConfirmET)
    EditText passwordConfirmET;

    @ViewById(R.id.registerSubmit)
    Button registerSubmit;

    @Extra("mobile")
    String mobile;

    @Extra("chekcode")
    String chekcode;

    @Extra("invitation_code")
    String invitation_code;

    @AfterViews
    void afterViews(){
        //title
        setTitle(R.id.navigation_bar_back_tv,R.string.register);
    }

    @Click({R.id.navigation_bar_back_ib,R.id.registerSubmit})
    void click(View v){
        switch (v.getId()){
            case R.id.navigation_bar_back_ib: {  //导航条返回
                finish();
                break;
            }
            case R.id.registerSubmit: {  //注册
                if(passwordET.getText().toString().equalsIgnoreCase(passwordConfirmET.getText().toString())){
                    //隐藏键盘
                    hideSoftKeyboard(passwordConfirmET);
                    //进度指示
                    showProcessHUD(null);
                    LogUtils.putLog(mobile+"=="+chekcode);
                    userRegisterBackground(mobile,passwordET.getText().toString(),chekcode,invitation_code);
                }else{
                    showToast(R.string.pass_not_same);
                }
                break;
            }
            default:
                break;
        }

    }

    @TextChange({R.id.passwordET,R.id.passwordConfirmET})
    void textChange(){
        registerSubmit.setEnabled(passwordET.getText().toString().length() > 5 && passwordConfirmET.getText().toString().length()>5);
    }
    /***
     * 用户注册
     * */
    @Background
    void userRegisterBackground(String mobile,String password,String verify_code,String invitation_code){
        //参数设置
        UserRegisterRequest action = new UserRegisterRequest();
        action.setU_phone(mobile);
        action.setU_password(password);
        action.setVerify_code(verify_code);
        action.setU_invitation_code(invitation_code);
        try {
            UserRegisterResponse response = restClient.userRegister(action);

            //status为0时请求成功
            if (response.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                userRegisterResult(response.getData(),"注册成功!");
            }else{
                userRegisterResult(null,response.getMessage());
            }
        }catch (Exception e){
            e.printStackTrace();
            userRegisterResult(null,e.getLocalizedMessage());
        }
    }

    @UiThread
    void userRegisterResult(User user,String msg){
        //隐藏进度指示
        hideProcessHUD();
        if (user != null) {
            //赋值到全局变量中
            App_.getInstance().mUser = user;
            //保存到SharedPreferences中
            User.saveUser(this,user);
            //通知界面退出
            EventBus.getDefault().post(new MessageEvent(MessageEvent.LOGIN_SUCCESS_EVENT));
            //进入主界面
            MainActivity_.intent(this).start();
            //完成
            finish();
        }
        //提示
        showToast(msg);
    }
}
