package com.ldi.android.Activitys;

import android.view.View;
import android.widget.Button;

import com.ldi.android.Activitys.Base.BaseActivity;
import com.ldi.android.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_change_profile)
public class ChangeProfileActivity extends BaseActivity {

    @ViewById(R.id.btn_ok)
    Button btn_ok;

    @AfterViews
    void afterview() {
        setTitle(R.id.navigation_bar_back_tv, R.string.change_info);
    }

    @Click({R.id.navigation_bar_back_ib,R.id.btn_ok,R.id.iv_photo})
    void click(View v){
        switch (v.getId()){
            case R.id.navigation_bar_back_ib: {  //导航条返回
                finish();
                break;
            }
            case R.id.btn_ok:{
                break;
            }
            case R.id.iv_photo:{
                break;
            }
            default:break;
        }
    }
}
