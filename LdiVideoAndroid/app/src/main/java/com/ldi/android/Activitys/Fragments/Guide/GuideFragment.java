package com.ldi.android.Activitys.Fragments.Guide;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ldi.android.Activitys.Fragments.Guide.GuideFragment_;
import com.ldi.android.Activitys.UserEnterActivity_;
import com.ldi.android.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
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

    @ViewById(R.id.fragmentGuidePagePointerIV)
    ImageView fragmentGuidePagePointerIV;

    @FragmentArg("isLastPage")
    Boolean isLastPage;

    @FragmentArg("bgColor")
    Integer bgColor;

    @FragmentArg("logoId")
    Integer logoId;

    @FragmentArg("pointerId")
    Integer pointerId;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProductFragment.
     */
    public static GuideFragment newInstance(int bgColor,int logoId,int pointerId) {
        GuideFragment fragment = new GuideFragment_();
        Bundle bundle = new Bundle();
        bundle.putInt("bgColor",bgColor);
        bundle.putInt("logoId",logoId);
        bundle.putInt("pointerId",pointerId);
        fragment.setArguments(bundle);
        return fragment;
    }
    public static GuideFragment newInstance(int bgColor,int logoId,int pointerId,Boolean isLastPage) {
        GuideFragment fragment = new GuideFragment_();
        Bundle bundle = new Bundle();
        bundle.putInt("bgColor",bgColor);
        bundle.putInt("logoId",logoId);
        bundle.putInt("pointerId",pointerId);
        bundle.putBoolean("isLastPage",isLastPage);
        fragment.setArguments(bundle);
        return fragment;
    }
    @AfterViews
    void afterViews(){
        fragmentGuidePageBgView.setBackgroundColor(ActivityCompat.getColor(getActivity(),bgColor));
        fragmentGuidePageLogoIV.setImageResource(logoId);
        fragmentGuidePagePointerIV.setImageResource(pointerId);
    }

    @Click({R.id.fragmentGuidePageBgView})
    void click(View v){
        if (isLastPage) {
            UserEnterActivity_.intent(this).start();
            getActivity().finish();
        }
    }

}
