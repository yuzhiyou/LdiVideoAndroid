package com.ldi.android.Activitys;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ldi.android.Activitys.Base.BaseActivity;
import com.ldi.android.App_;
import com.ldi.android.Beans.User;
import com.ldi.android.Beans.WepApi.Request.UserRegisterRequest;
import com.ldi.android.Beans.WepApi.Response.UserLoginResponse;
import com.ldi.android.Net.MyRestClient;
import com.ldi.android.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

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

    @AfterViews
    void afterViews(){
        //title
        setTitle(R.id.navigation_bar_back_tv,R.string.register);
    }

    @Click(R.id.registerSubmit)
    void click(){
        if(passwordET.getText().toString().equalsIgnoreCase(passwordConfirmET.getText().toString())){
            //隐藏键盘
            hideSoftKeyboard(passwordConfirmET);
            //进度指示
            showProcessHUD(null);
            userRegisterBackground("",passwordET.getText().toString(),"");
        }else{
            showToast(R.string.pass_not_same);
        }
    }

    @TextChange({R.id.passwordET,R.id.passwordConfirmET})
    void textChange(View v){
        registerSubmit.setEnabled(passwordET.getText().toString().length() > 5 && passwordConfirmET.getText().toString().length()>5);
    }
    /***
     * 用户注册
     * */
    @Background
    void userRegisterBackground(String mobile,String password,String verify_code){
        //参数设置
        UserRegisterRequest action = new UserRegisterRequest();
        action.setU_phone(mobile);
        action.setU_password(password);
        action.setVerify_code(verify_code);
        try {
            UserLoginResponse response = restClient.userRegister(action);

            //status为0时请求成功
            if (response.getStatus() == 0) {
                userRegeisterResult(response.getData(),"注册成功!");
            }else{
                userRegeisterResult(null,"注册失败!");
            }
        }catch (Exception e){
            userRegeisterResult(null,e.getLocalizedMessage());
        }
    }

    @UiThread
    void userRegeisterResult(User user,String msg){
        //隐藏进度指示
        hideProcessHUD();
        if (user != null) {
            //赋值到全局变量中
            App_.getInstance().mUser = user;
            //保存到SharedPreferences中
            User.saveUser(this,user);
            //完成
            finish();
        }
        //提示
        showToast(msg);
    }
}
