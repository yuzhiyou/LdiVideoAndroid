package com.ldi.android.Activitys;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ldi.android.Activitys.Base.BaseActivity;
import com.ldi.android.Beans.WepApi.Request.CheckCodeRequest;
import com.ldi.android.Beans.WepApi.Response.StatusResponse;
import com.ldi.android.Constants;
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

@EActivity(R.layout.activity_forget_pass)
public class ForgetPassActivity extends BaseActivity {
    //网络请求
    @RestService
    MyRestClient restClient;

    @ViewById(R.id.et_mobile)
    EditText et_mobile;

    @ViewById(R.id.et_forgetpass_checkcode)
    EditText et_forgetpass_checkcode;

    @ViewById(R.id.btn_frogetpass_send_checkcode)
    Button btn_frogetpass_send_checkcode;

    @ViewById(R.id.btn_forget_next)
    Button btn_forget_next;

    MyCountDownTimer mc;

    @AfterViews
    void afterViews(){
        //title
        setTitle(R.id.navigation_bar_back_tv,R.string.forget_password);
        mc = new MyCountDownTimer(60000,1000);
    }

    @TextChange({R.id.et_mobile,R.id.et_forgetpass_checkcode})
    void textChange(){
        btn_frogetpass_send_checkcode.setEnabled(ValidateUtil.isMobileNumber(et_mobile.getText().toString()));
        btn_forget_next.setEnabled(ValidateUtil.isMobileNumber(et_mobile.getText().toString()) && ValidateUtil.isCheckCode(et_forgetpass_checkcode.getText().toString()));
    }

    @Click({R.id.navigation_bar_back_ib,R.id.btn_forget_next,R.id.btn_frogetpass_send_checkcode})
    void click(View v){
        switch(v.getId()){
            case R.id.navigation_bar_back_ib:{
                finish();
                break;
            }
            case R.id.btn_forget_next:{
                String mobile = et_mobile.getText().toString();
                String check_code = et_forgetpass_checkcode.getText().toString();
                Intent intent = RegisterPassActivity_.intent(this).mobile(mobile).chekcode(check_code).get();
                startActivity(intent);
                break;
            }
            case R.id.btn_frogetpass_send_checkcode:{
                //显示进度指示
                showProcessHUD(null);
                btn_frogetpass_send_checkcode.setEnabled(false);
                mc.start();
                //请求验证码
                sendCheckCodeInBackground(et_mobile.getText().toString());
                break;
            }
            default:break;
        }
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
            if (response.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
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


    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture
         *            表示以毫秒为单位 倒计时的总数
         *
         *            例如 millisInFuture=1000 表示1秒
         *
         * @param countDownInterval
         *            表示 间隔 多少微秒 调用一次 onTick 方法
         *
         *            例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btn_frogetpass_send_checkcode.setEnabled(true);
            btn_frogetpass_send_checkcode.setText(R.string.send_again);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_frogetpass_send_checkcode.setText(millisUntilFinished / 1000 + "秒后重发");
        }

    }

}
