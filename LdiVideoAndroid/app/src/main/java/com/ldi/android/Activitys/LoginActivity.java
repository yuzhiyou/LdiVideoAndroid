package com.ldi.android.Activitys;

import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ldi.android.Activitys.Base.BaseActivity;
import com.ldi.android.App_;
import com.ldi.android.Beans.User;
import com.ldi.android.Beans.WepApi.Request.UserLoginRequest;
import com.ldi.android.Beans.WepApi.Response.UserLoginResponse;
import com.ldi.android.Constants;
import com.ldi.android.EventBus.MessageEvent;
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
import org.greenrobot.eventbus.EventBus;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    //网络请求
    @RestService
    MyRestClient restClient;

    //账号输入框相关
    @ViewById(R.id.loginAccountET)
    EditText loginAccountEditText;

    @ViewById(R.id.loginAccountClearIB)
    ImageButton loginAccountClearIB;

    Drawable[] loginAccountIcons;
    //密码相关
    @ViewById(R.id.loginPasswordET)
    EditText loginPasswordEditText;

    @ViewById(R.id.loginPasswordVisibleIB)
    ImageButton loginPasswordVisibleIB;

    Drawable[] loginPasswordIcons;

    //登录按钮
    @ViewById(R.id.loginBtn)
    Button loginBtn;
    //忘记密码
    @ViewById(R.id.loginForgetPasswordTv)
    TextView loginForgetPasswordTv;
    //立即注册
    @ViewById(R.id.loginRegisterTv)
    TextView loginRegisterTv;

    @AfterViews
    void afterViews(){
        //title
        setTitle(R.id.navigation_bar_back_tv,R.string.login);
        //icon加载
        loginAccountIcons = new Drawable[2];
        loginAccountIcons[0] = ActivityCompat.getDrawable(this, R.mipmap.account_normal);
        loginAccountIcons[1] = ActivityCompat.getDrawable(this, R.mipmap.account_light);
        loginPasswordIcons = new Drawable[2];
        loginPasswordIcons[0] = ActivityCompat.getDrawable(this, R.mipmap.password_normal);
        loginPasswordIcons[1] = ActivityCompat.getDrawable(this, R.mipmap.password_light);
    }
    /**
     * 监听EditText变化
     * */
    @TextChange({R.id.loginAccountET,R.id.loginPasswordET})
    void accountInfoTextChange(TextView v){
        //账号是否为空
        Drawable drawable = TextUtils.isEmpty(loginAccountEditText.getText())?loginAccountIcons[0]:loginAccountIcons[1];
        loginAccountEditText.setCompoundDrawablesWithIntrinsicBounds(drawable, null,
                null, null);
        loginAccountClearIB.setVisibility(TextUtils.isEmpty(loginAccountEditText.getText()) ? View.GONE : View.VISIBLE);

        //密码是否为空
        drawable = TextUtils.isEmpty(loginPasswordEditText.getText())?loginPasswordIcons[0]:loginPasswordIcons[1];
        loginPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(drawable, null,
                null, null);
        //登录是否可用
        loginBtn.setEnabled(ValidateUtil.isMobileNumber(loginAccountEditText.getText().toString()) && loginPasswordEditText.getText().length() > 5);
    }

    /**
     * 点击事件
     * */
    @Click({R.id.navigation_bar_back_ib,R.id.loginBtn,R.id.loginPasswordVisibleIB,R.id.loginAccountClearIB,R.id.loginForgetPasswordTv,R.id.loginRegisterTv})
    void click(View v){
        switch (v.getId()){
            case R.id.navigation_bar_back_ib: {  //导航条返回
                finish();
                break;
            }
            case R.id.loginBtn: {  //用户登录
                //隐藏键盘
                hideSoftKeyboard(v);
                //进度指示
                showProcessHUD(null);
                //登录
                userLoginInBackground(loginAccountEditText.getText().toString(),loginPasswordEditText.getText().toString());
                break;
            }
            case R.id.loginPasswordVisibleIB: {  //密码可见\不可见
                loginPasswordEditText.setInputType(loginPasswordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ? (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD) : InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                loginPasswordEditText.setSelection(loginPasswordEditText.length());
                loginPasswordVisibleIB.setImageResource(loginPasswordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ? R.mipmap.password_visible : R.mipmap.password_invisible);
                break;
            }
            case R.id.loginAccountClearIB: {  //清空账号
                loginAccountEditText.setText("");
                loginPasswordEditText.setText("");
                break;
            }
            case R.id.loginForgetPasswordTv:  //忘记密码
                ForgetPassActivity_.intent(this).start();
                break;
            case R.id.loginRegisterTv:  //立即注册
                RegisterActivity_.intent(this).start();
                break;
        }
    }
    /***
     * 用户登录
     * */
    @Background
    void userLoginInBackground(String mobile,String password){
        //参数设置
        UserLoginRequest action = new UserLoginRequest();
        action.setU_phone(mobile);
        action.setU_password(password);
        try {
            UserLoginResponse response = restClient.userLogin(action);
            //status为0时请求成功
            if (response.getStatus().equalsIgnoreCase(Constants.STATUS_OK)) {
                userLoginResult(response.getData(),"登录成功!");
            }else{
                userLoginResult(null,response.getMessage());
            }
        }catch (Exception e){
            userLoginResult(null,e.getLocalizedMessage());
        }
    }
    @UiThread
    void userLoginResult(User user,String msg){
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
