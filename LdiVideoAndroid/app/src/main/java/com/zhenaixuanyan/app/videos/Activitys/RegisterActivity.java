package com.zhenaixuanyan.app.videos.Activitys;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.zhenaixuanyan.app.videos.Activitys.Base.BaseActivity;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Request.CheckCodeRequest;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Response.StatusResponse;
import com.zhenaixuanyan.app.videos.Constants;
import com.zhenaixuanyan.app.videos.EventBus.MessageEvent;
import com.zhenaixuanyan.app.videos.Net.MyRestClient;
import com.zhenaixuanyan.app.videos.R;
import com.zhenaixuanyan.app.videos.Utils.ValidateUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

    @ViewById(R.id.registerConditionCb)
    CheckBox registerConditionCb;

    MyCountDownTimer mc;


    @AfterViews
    void afterViews(){
        //Eventbus
        EventBus.getDefault().register(this);
        //title
        setTitle(R.id.navigation_bar_back_tv,R.string.register);
        mc = new MyCountDownTimer(60000,1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Click({R.id.navigation_bar_back_ib,R.id.registerConditionTv,R.id.registerNextStepBtn,R.id.registerSendCheckCodeBtn})
    void click(View v){
        switch (v.getId()){
            case R.id.navigation_bar_back_ib: {    //Back
                finish();
                break;
            }
            case R.id.registerConditionTv: {    //注册条款
                WebViewClientActivity_.intent(this)
                        .title(getString(R.string.register_condition_title))
                        .url(Constants.kRegisterConditionUrl)
                        .start();
                break;
            }
            case R.id.registerSendCheckCodeBtn: {    //发送验证码
                //显示进度指示
                showProcessHUD(null);
                mc.start();
                loginSendCheckCodeBtn.setEnabled(false);
                //请求验证码
                sendCheckCodeInBackground(registerMobileET.getText().toString(),registerRecommendCodeET.getText().toString());
                break;
            }
            case R.id.registerNextStepBtn: {    //下一步
                String mobile = registerMobileET.getText().toString();
                String check_code = registerCheckCodeET.getText().toString();
                String invitation_code = registerRecommendCodeET.getText().toString();

                RegisterPassActivity_
                        .intent(this)
                        .mobile(mobile)
                        .chekcode(check_code)
                        .invitation_code(invitation_code)
                        .start();
                break;
            }
            default:
                break;
        }
    }
    /**
     *
     * 监听选中事件
     */
    @CheckedChange(R.id.registerConditionCb)
    void check(CompoundButton checkbox, boolean isChecked){
        registerNextStepBtn.setEnabled(ValidateUtil.isMobileNumber(registerMobileET.getText().toString()) && ValidateUtil.isCheckCode(registerCheckCodeET.getText().toString()) && registerConditionCb.isChecked());
    }

    /**
     * 监听文本变化
     * */
    @TextChange({R.id.registerMobileET, R.id.registerCheckCodeET})
    void onLoginTextChange(){
        loginSendCheckCodeBtn.setEnabled(ValidateUtil.isMobileNumber(registerMobileET.getText().toString()));
        registerNextStepBtn.setEnabled(ValidateUtil.isMobileNumber(registerMobileET.getText().toString()) && ValidateUtil.isCheckCode(registerCheckCodeET.getText().toString()) && registerConditionCb.isChecked());
    }
    /***
     * 获取验证码
     * */
    @Background
    void sendCheckCodeInBackground(String mobile,String invitation_code){
        CheckCodeRequest action = new CheckCodeRequest();
        action.setU_phone(mobile);
        action.setU_invitation_code(invitation_code);
        try {
            StatusResponse response = restClient.getCheckCode(action);
            if (response.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                sendCheckCodeResult("发送验证码成功!");
            }else{
                sendCheckCodeResult(response.getMessage());
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
            loginSendCheckCodeBtn.setEnabled(true);
            loginSendCheckCodeBtn.setText(R.string.send_again);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            loginSendCheckCodeBtn.setText(millisUntilFinished / 1000 + "秒后重发");
        }

    }
    @Subscribe
    public void onEvent(MessageEvent event) {
        if (event.getId() == MessageEvent.LOGIN_SUCCESS_EVENT){
            finish();
        }
    }
}
