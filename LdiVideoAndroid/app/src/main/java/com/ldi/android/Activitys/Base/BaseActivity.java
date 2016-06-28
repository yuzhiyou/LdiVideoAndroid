package com.ldi.android.Activitys.Base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ldi.android.R;

/**
 * Created by Forrest on 16/4/26.
 */
public class BaseActivity extends AppCompatActivity {
    //Dialog
    private Dialog mPD = null;
    //system bar
    private SystemBarTintManager tintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create our manager instance after the content view is set
        tintManager = new SystemBarTintManager(this);
        setSystemTintColorBar(R.color.colorPrimary);
    }
    /**
     * 设置标题
     * */
    protected void setTitle(int resID,int strID){
        TextView titleTv = (TextView)findViewById(resID);
        if (titleTv != null) {
            titleTv.setText(strID);
        }
    }
    /***
     * showProcessHUD
     * 			显示进度指示
     * @param title 显示提示title
     * */
    protected void showProcessHUD(String title) {
        // 加载进度条
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.ztb_custom_dialog, null);
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.dialog_loading_img);
        TextView titleTV = (TextView) v.findViewById(R.id.dialog_loading_title);
        if (!TextUtils.isEmpty(title)){
            titleTV.setText(title);
            titleTV.setVisibility(View.VISIBLE);
        }else{
            titleTV.setVisibility(View.GONE);
        }
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this,
                R.anim.rotate_anim);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);

        mPD = new Dialog(this, R.style.loading_dialog);
        mPD.addContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        mPD.setCancelable(true);
        mPD.setCanceledOnTouchOutside(false);
        mPD.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        mPD.show();
    }
    /***
     * hideProcessHUD
     * 			隐藏进度指示
     * */
    protected void hideProcessHUD() {
        if(mPD != null)
            mPD.dismiss();
    }

    /**
     * setSystemTintBar
     * 			设置状态栏颜色
     * */
    protected void setSystemTintColorBar(int colorRes){

        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setStatusBarTintColor(ActivityCompat.getColor(this, colorRes));
    }
    /**
     * setSystemTintBar
     * 			设置状态栏颜色
     * */
    protected void setSystemTintResourceBar(int drawableRes){
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setStatusBarTintResource(drawableRes);
    }
    /***
     * showSoftKeyboard
     * 			显示键盘
     * @param v View
     * */
    protected void showSoftKeyboard(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }
    /***
     * hideSoftKeyboard
     * 			隐藏键盘
     * @param v View
     * */
    protected void hideSoftKeyboard(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }
    /***
     * showToast
     * 			显示提示
     * @param title 显示title
     * */
    protected void showToast(String title){
        Toast.makeText(this, title,Toast.LENGTH_SHORT).show();
    }
}
