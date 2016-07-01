package com.ldi.android.Activitys.Fragments.Tabbar;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.ldi.android.Activitys.ChangePasswordActivity_;
import com.ldi.android.Activitys.ChangeProfileActivity_;
import com.ldi.android.Activitys.Fragments.BaseFragment;
import com.ldi.android.Activitys.UserEnterActivity_;
import com.ldi.android.App_;
import com.ldi.android.Beans.User;
import com.ldi.android.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_profile)
public class ProfileFragment extends BaseFragment {
    @ViewById(R.id.btn_logout)
    Button btn_logout;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment_();
        return fragment;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }

    @AfterViews
    void afterViews() {
        //title
        setTitle(R.id.navigation_bar_title_tv, R.string.tabar_profile);
    }

    @Click({R.id.btn_logout, R.id.rl_appeal, R.id.iv_photo, R.id.rl_change_pass, R.id.rl_gift, R.id.rl_identity_verification, R.id.rl_my_video, R.id.rl_change_info, R.id.rl_recode})
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_logout: {
                App_.getInstance().mUser = null;
                User.clearUser(getContext());
                //注册登录页
                UserEnterActivity_.intent(this).start();
                getActivity().finish();
                break;
            }
            case R.id.rl_appeal: {
                break;
            }
            case R.id.iv_photo: {
                break;
            }
            case R.id.rl_change_pass: {
                ChangePasswordActivity_.intent(this).start();
                break;
            }
            case R.id.rl_gift: {
                break;
            }
            case R.id.rl_identity_verification: {
                break;
            }
            case R.id.rl_my_video: {
                break;
            }
            case R.id.rl_change_info: {
                ChangeProfileActivity_.intent(this).start();
                break;
            }
            case R.id.rl_recode: {
                break;
            }
            default:break;
        }
    }
}
