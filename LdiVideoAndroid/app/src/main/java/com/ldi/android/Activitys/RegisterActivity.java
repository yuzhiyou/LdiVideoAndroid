package com.ldi.android.Activitys;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ldi.android.Activitys.Base.BaseActivity;
import com.ldi.android.Beans.WepApi.Request.CheckCodeRequest;
import com.ldi.android.Beans.WepApi.Response.StatusResponse;
import com.ldi.android.Net.MyRestClient;
import com.ldi.android.R;
import com.ldi.android.Utils.ValidateUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

    //网络请求
    @RestService
    MyRestClient restClient;

    //手机号
    @ViewById(R.id.registerMobileET)
    EditText registerMobileET;
    //验证码
    @ViewById(R.id.registerCheckCodeET)
    EditText registerCheckCodeET;
    //推荐码
    @ViewById(R.id.registerRecommendCodeET)
    EditText registerRecommendCodeET;
    //发送验证码
    @ViewById(R.id.registerSendCheckCodeBtn)
    Button loginSendCheckCodeBtn;
    //下一步
    @ViewById(R.id.registerNextStepBtn)
    Button registerNextStepBtn;


    @AfterViews
    void afterViews(){
        //title
        setTitle(R.id.navigation_bar_back_tv,R.string.register);
    }


    @Click({R.id.navigation_bar_back_ib,R.id.registerNextStepBtn,R.id.registerSendCheckCodeBtn})
    void click(View v){
        switch (v.getId()){
            case R.id.navigation_bar_back_ib: {    //Back
                finish();
                break;
            }
            case R.id.registerSendCheckCodeBtn: {    //发送验证码
                //显示进度指示
                showProcessHUD(null);
                //请求验证码
                sendCheckCodeInBackground(registerMobileET.getText().toString());
                break;
            }
            case R.id.registerNextStepBtn: {    //下一步
                break;
            }
            default:
                break;
        }
    }
    /**
     * 监听文本变化
     * */
    @TextChange({R.id.registerMobileET, R.id.registerCheckCodeET})
    void onLoginTextChange(){
        loginSendCheckCodeBtn.setEnabled(ValidateUtil.isMobileNumber(registerMobileET.getText().toString()));
        registerNextStepBtn.setEnabled(ValidateUtil.isMobileNumber(registerMobileET.getText().toString()) && ValidateUtil.isCheckCode(registerCheckCodeET.getText().toString()));
    }
    /***
     * 获取验证码
     * */
    @Background
    void sendCheckCodeInBackground(String mobile){
        CheckCodeRequest action = new CheckCodeRequest();
        action.setU_phone(mobile);
        try {
            StatusResponse response = restClient.getCheckCode(action);
            if (response.getStatus() == 0) {
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
