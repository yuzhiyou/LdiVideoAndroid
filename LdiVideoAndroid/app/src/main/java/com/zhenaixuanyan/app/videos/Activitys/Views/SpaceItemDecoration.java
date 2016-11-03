package com.zhenaixuanyan.app.videos.Activitys.Views;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhenaixuanyan.app.videos.Utils.LogUtils;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * Created by Forrest on 2016/10/26.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildLayoutPosition(view);
        int type = ((SectionedRecyclerViewAdapter)parent.getAdapter()).getSectionItemViewType(position);
        if (type == SectionedRecyclerViewAdapter.VIEW_TYPE_ITEM_LOADED) {
            outRect.left = space/2;
            outRect.right = space/2;
        }else{
            outRect.left = 0;
            outRect.right = 0;
        }
    }

}