package com.zhenaixuanyan.app.videos.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhenaixuanyan.app.videos.Beans.Video;
import com.zhenaixuanyan.app.videos.R;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by Forrest on 2016/10/26.
 */

public class MyVideoSection extends StatelessSection{

    private String title;
    private List<Video> list;
    private Context mContext;

    public MyVideoSection(Context context,String title, List<Video> list) {
        super(R.layout.my_video_list_section, R.layout.my_video_list_item);

        this.title = title;
        this.list = list;
        this.mContext = context;
    }

    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;

        itemHolder.myVideoGridItemTitleTv.setText(list.get(position).getV_name());
        itemHolder.myVideoGridItemDescTv.setText(list.get(position).getV_describe());
        //图片
        Picasso.with(mContext)
                .load(list.get(position).getV_image_thum())
                .placeholder(R.mipmap.no_video_image)
                .into(itemHolder.myVideoGridItemImageIv);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        headerHolder.sectionTitle.setText(title);
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView sectionTitle;

        public HeaderViewHolder(View view) {
            super(view);
            sectionTitle = (TextView) view.findViewById(R.id.myVideoGridSectionTitleTv);
        }
    }
    class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView myVideoGridItemImageIv;
        TextView myVideoGridItemTitleTv;
        TextView myVideoGridItemDescTv;

        public ItemViewHolder(View view) {
            super(view);

            myVideoGridItemTitleTv = (TextView) view.findViewById(R.id.myVideoGridItemTitleTv);
            myVideoGridItemDescTv = (TextView)view.findViewById(R.id.myVideoGridItemDescTv);
            myVideoGridItemImageIv =(ImageView) view.findViewById(R.id.myVideoGridItemImageIv);
        }
    }

}
