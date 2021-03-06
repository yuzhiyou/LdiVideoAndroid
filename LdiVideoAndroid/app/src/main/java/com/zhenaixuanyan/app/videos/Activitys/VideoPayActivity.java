package com.zhenaixuanyan.app.videos.Activitys;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.zhenaixuanyan.app.videos.Activitys.Base.BaseActivity;
import com.zhenaixuanyan.app.videos.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import tcking.github.com.giraffeplayer.GiraffePlayerActivity;

@EActivity(R.layout.activity_video_pay)
public class VideoPayActivity extends BaseActivity {

   @ViewById(R.id.radio_ali_pay)
    RadioButton radio_ali_pay;

    @ViewById(R.id.radio_wetchat_pay)
    RadioButton radio_wetchat_pay;

    @ViewById(R.id.btn_ok)
    Button btn_ok;
    @Extra("videoUrl")
    String videoUrl;

   @AfterViews
    void afterViews(){
       //title
       setTitle(R.id.navigation_bar_back_tv,R.string.video_pay);
   }

   @Click({R.id.navigation_bar_back_ib,R.id.ll_ali_pay,R.id.ll_wechat_pay,R.id.btn_ok})
    void click(View v){
       switch (v.getId()){
           case R.id.navigation_bar_back_ib:{
               finish();
               break;
           }
           case R.id.ll_ali_pay:{
               radio_ali_pay.setChecked(true);
               radio_wetchat_pay.setChecked(false);
               break;
           }
           case R.id.ll_wechat_pay:{
               radio_ali_pay.setChecked(false);
               radio_wetchat_pay.setChecked(true);
               break;
           }
           case R.id.btn_ok:{
               //调用支付的SDK
               GiraffePlayerActivity.configPlayer(this).play(videoUrl);
               break;
           }
           default:break;
       }
   }
}
