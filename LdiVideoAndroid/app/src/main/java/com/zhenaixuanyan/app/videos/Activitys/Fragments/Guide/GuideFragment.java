package com.zhenaixuanyan.app.videos.Activitys.Fragments.Guide;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhenaixuanyan.app.videos.Activitys.Fragments.Guide.GuideFragment_;
import com.zhenaixuanyan.app.videos.Activitys.UserEnterActivity_;
import com.zhenaixuanyan.app.videos.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ztb on 16/4/21.
 * @author fr
 */
@EFragment(R.layout.fragment_guide_page)
public class GuideFragment extends Fragment {
    @ViewById(R.id.fragmentGuidePageBgView)
    LinearLayout fragmentGuidePageBgView;

    @ViewById(R.id.fragmentGuidePageLogoIV)
    ImageView fragmentGuidePageLogoIV;

    @FragmentArg("isLastPage")
    Boolean isLastPage;

    @FragmentArg("logoId")
    Integer logoId;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProductFragment.
     */
    public static GuideFragment newInstance(int logoId,Boolean isLastPage) {
        GuideFragment fragment = new GuideFragment_();
        Bundle bundle = new Bundle();
        bundle.putInt("logoId",logoId);
        bundle.putBoolean("isLastPage",isLastPage);
        fragment.setArguments(bundle);
        return fragment;
    }
    @AfterViews
    void afterViews(){
        fragmentGuidePageLogoIV.setImageResource(logoId);
        fragmentGuidePageLogoIV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isLastPage) {
                    UserEnterActivity_.intent(getActivity()).start();
                    getActivity().finish();
                }
                return false;
            }
        });
    }

}
