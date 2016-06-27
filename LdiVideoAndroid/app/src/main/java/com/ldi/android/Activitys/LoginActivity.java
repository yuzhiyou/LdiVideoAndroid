package com.ldi.android.Activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ldi.android.Activitys.Base.BaseActivity;
import com.ldi.android.Net.MyRestClient;
import com.ldi.android.R;
import com.ldi.android.Utils.ValidateUtil;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.json.JSONObject;
import org.springframework.util.LinkedMultiValueMap;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    //网络请求
    @RestService
    MyRestClient restClient;

    //控件引用
    @ViewById(R.id.loginMobileET)
    EditText loginMobileET;

    @ViewById(R.id.loginCheckCodeET)
    EditText loginCheckCodeET;


    @ViewById(R.id.loginSendCheckCodeBtn)
    Button loginSendCheckCodeBtn;



    @AfterViews
    void afterViews(){

    }


    @Click({R.id.loginSendCheckCodeBtn})
    void click(View v){
        switch (v.getId()){
            case R.id.loginSendCheckCodeBtn: {    //发送验证码
                //显示进度指示
                showProcessHUD(null);
                //请求验证码
                sendCheckCodeInBackground(loginMobileET.getText().toString());
                break;
            }
            default:
                break;
        }
    }
    /**
     * 监听文本变化
     * */
    @TextChange({R.id.loginCheckCodeET, R.id.loginSendCheckCodeBtn})
    void onLoginTextChange(){
        loginSendCheckCodeBtn.setEnabled(ValidateUtil.isMobileNumber(loginMobileET.getText().toString()));
    }
    /***
     * 获取验证码
     * */
    @Background
    void sendCheckCodeInBackground(String mobile){
        LinkedMultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("v_phone", mobile);
        try {
            String json = restClient.getCheckCode(param);
            JSONObject response = new JSONObject(json);
            if (response.getBoolean("success")) {
                sendCheckCodeResult("发送验证码成功!");
            }else{
                sendCheckCodeResult("发送验证码失败!");
            }
        }catch (Exception e){
            sendCheckCodeResult("发送验证码失败!");
        }
    }
    @UiThread
    void sendCheckCodeResult(String message){
        //隐藏进度指示
        hideProcessHUD();
        //提示
        showToast(message);
    }
}
