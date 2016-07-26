package com.zhenaixuanyan.app.videos.Activitys;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhenaixuanyan.app.videos.Activitys.Base.BaseActivity;
import com.zhenaixuanyan.app.videos.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_change_password)
public class ChangePasswordActivity extends BaseActivity {
    @ViewById(R.id.et_old_pass)
    EditText et_old_pass;

    @ViewById(R.id.et_new_pass)
    EditText et_new_pass;

    @ViewById(R.id.et_confirm_pass)
    EditText et_confirm_pass;

    @ViewById(R.id.btn_submit_change)
    Button btn_submit_change;

    @AfterViews
    void afterview() {
        setTitle(R.id.navigation_bar_back_tv, R.string.change_pass);
    }

    @Click({R.id.navigation_bar_back_ib, R.id.btn_submit_change})
    void click(View v) {
        switch (v.getId()) {
            case R.id.navigation_bar_back_ib: {  //导航条返回
                finish();
                break;
            }
            case R.id.btn_submit_change: {
                if(et_new_pass.getText().toString().equalsIgnoreCase(et_confirm_pass.getText().toString())){

                    //隐藏键盘
                    hideSoftKeyboard(et_confirm_pass);
                    //进度指示
                    //showProcessHUD(null);
                    //请求修改密码的接口

                }else{
                    showToast(R.string.pass_not_same);
                }
                break;
            }
            default:break;
        }
    }

    @TextChange({R.id.et_old_pass, R.id.et_new_pass, R.id.et_confirm_pass})
    void textChange() {
        btn_submit_change.setEnabled(et_old_pass.getText().toString().length() > 5 && et_new_pass.getText().toString().length() > 5 && et_confirm_pass.getText().toString().length() > 5);
    }
}
