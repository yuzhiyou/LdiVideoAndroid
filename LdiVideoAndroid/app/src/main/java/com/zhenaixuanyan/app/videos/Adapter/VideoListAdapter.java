package com.zhenaixuanyan.app.videos.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhenaixuanyan.app.videos.Beans.Video;
import com.zhenaixuanyan.app.videos.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by kangyawei on 2016/7/3.
 */
public class VideoListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Video> list;
    private Context mContext;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public VideoListAdapter(Context ctx){
        this.mContext = ctx;
    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public List<Video> getList() {
        return list;
    }

    public void setList(List<Video> list) {
        this.list = list;
    }

    public VideoListAdapter() {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (position + 1 == getItemCount()) {
//            return TYPE_FOOTER;
//        } else {
//            return TYPE_ITEM;
//        }
//    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            Video video = list.get(position);
            Picasso.with(mContext).load(video.v_image).into(((ItemViewHolder) holder).iv_video);
            ((ItemViewHolder) holder).tv_first_title.setText(video.v_name);
            ((ItemViewHolder) holder).tv_second_title.setText(video.v_describe);
        }
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    v.setTag(list.get(position));
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(v, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //if (viewType == TYPE_ITEM) {
            ItemViewHolder holder = new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item,parent,false));

            return holder;
        //}
//        else if (viewType == TYPE_FOOTER) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footerview, null);
//            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//            return new FooterViewHolder(view);
//        }

        //return null;
    }

    class FooterViewHolder extends ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }

    class ItemViewHolder extends ViewHolder {
        TextView tv_first_title;
        TextView tv_second_title;
        RoundedImageView iv_video;

        public ItemViewHolder(View view) {
            super(view);
            tv_first_title = (TextView) view.findViewById(R.id.tv_first_title);
            tv_second_title = (TextView)view.findViewById(R.id.tv_second_title);
            iv_video =(RoundedImageView) view.findViewById(R.id.iv_video);
        }

    }


}
