package com.ldi.android.Activitys;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.Window;
import android.widget.ImageView;

import com.ldi.android.Activitys.Base.BaseActivity;
import com.ldi.android.App_;
import com.ldi.android.Beans.User;
import com.ldi.android.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import tcking.github.com.giraffeplayer.GiraffePlayerActivity;

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
            //注册登录页
            UserGuideActivity_.intent(this).start();
        }else{                                  //已登录,调到主界面
            //播放视频
            GiraffePlayerActivity.configPlayer(this).play("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        }


        finish();
    }
}
