package com.zhenaixuanyan.app.videos.Activitys;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.widget.ImageView;

import com.zhenaixuanyan.app.videos.Activitys.Base.BaseActivity;
import com.zhenaixuanyan.app.videos.App_;
import com.zhenaixuanyan.app.videos.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_launch)
public class LaunchActivity extends BaseActivity implements  Animator.AnimatorListener{
    //动画时间
    public static final int DURATION = 2000;
    @ViewById(R.id.launchLogoImageView)
    ImageView launchLogoImageView;

    @AfterViews
    void afterViews(){

        setSystemTintColorBar(R.color.launch_bg_color);

        //动画
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(launchLogoImageView, "alpha", 0.0f, 1.0f).setDuration(DURATION)
        );
        set.addListener(this);
        set.start();
    }
    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        startEnter();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
    @UiThread
    void startEnter(){
        if (App_.getInstance().mUser == null) { //未登录
            if (true) { //都一次进入软件
            //if (App_.getInstance().getFirstEnter(this)) { //都一次进入软件
                UserGuideActivity_.intent(this).start();
                App_.getInstance().saveFirstEnter(this);
            }else {
                //注册登录页
                UserEnterActivity_.intent(this).start();
            }
        }else{                                  //已登录,调到主界面
            MainActivity_.intent(this).start();
            //播放视频
           // GiraffePlayerActivity.configPlayer(this).play("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        }

        finish();
    }
}
