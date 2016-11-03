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

public class HistoryVideoSection extends StatelessSection{

    private String title;
    private List<Video> list;
    private Context mContext;

    public HistoryVideoSection(Context context, String title, List<Video> list) {
        super(R.layout.history_video_list_section, R.layout.history_video_list_item);

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

        itemHolder.historyVideoGridItemTitleTv.setText(list.get(position).getV_name());
        itemHolder.historyVideoGridItemDescTv.setText(list.get(position).getV_describe());
        //图片
        Picasso.with(mContext)
                .load(list.get(position).getV_image_thum())
                .placeholder(R.mipmap.no_video_image)
                .into(itemHolder.historyVideoGridItemImageIv);
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
            sectionTitle = (TextView) view.findViewById(R.id.historyVideoGridSectionTitleTv);
        }
    }
    class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView historyVideoGridItemImageIv;
        TextView historyVideoGridItemTitleTv;
        TextView historyVideoGridItemDescTv;

        public ItemViewHolder(View view) {
            super(view);

            historyVideoGridItemTitleTv = (TextView) view.findViewById(R.id.historyVideoGridItemTitleTv);
            historyVideoGridItemDescTv = (TextView)view.findViewById(R.id.historyVideoGridItemDescTv);
            historyVideoGridItemImageIv =(ImageView) view.findViewById(R.id.historyVideoGridItemImageIv);
        }
    }

}
