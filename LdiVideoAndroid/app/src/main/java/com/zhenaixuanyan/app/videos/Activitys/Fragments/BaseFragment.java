package com.zhenaixuanyan.app.videos.Activitys.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenaixuanyan.app.videos.R;

/**
 * Created by Forrest on 16/5/18.
 * @author forrest
 */
public class BaseFragment extends Fragment {
    //Dialog
    private Dialog mPD = null;
    /**
     * 设置标题
     * */
    protected void setTitle(int resID,int strID){
        TextView titleTv = (TextView)getView().findViewById(resID);
        if (titleTv != null) {
            titleTv.setText(strID);
        }
    }
    protected void setTitle(int resID,String title){
        TextView titleTv = (TextView)getView().findViewById(resID);
        if (titleTv != null) {
            titleTv.setText(title);
        }
    }
    /***
     * showProcessHUD
     * 			显示进度指示
     * @param title 显示提示title
     * */
    protected void showProcessHUD(String title) {
        // 加载进度条
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.ztb_custom_dialog, null);
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.dialog_loading_img);
        TextView titleTV = (TextView) v.findViewById(R.id.dialog_loading_title);
        if (!TextUtils.isEmpty(title)){
            titleTV.setText(title);
            titleTV.setVisibility(View.VISIBLE);
        }else{
            titleTV.setVisibility(View.GONE);
        }
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.rotate_anim);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);

        mPD = new Dialog(getActivity(), R.style.loading_dialog);
        mPD.addContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        mPD.setCancelable(true);
        mPD.setCanceledOnTouchOutside(false);
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
}
