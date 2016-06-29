package com.ldi.android.Activitys.Fragments;

import android.support.v4.app.Fragment;

import com.ldi.android.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;


@EFragment(R.layout.fragment_home)
public class HomeFragment extends Fragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment_();

        return fragment;
    }

    @AfterViews
    void afterViews(){
    }


}
