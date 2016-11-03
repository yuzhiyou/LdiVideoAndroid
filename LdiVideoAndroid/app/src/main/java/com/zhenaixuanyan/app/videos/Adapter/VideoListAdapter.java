package com.zhenaixuanyan.app.videos.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public List<Video> getList() {
        return list;
    }

    public void setList(List<Video> list) {
        this.list = list;
    }

    public VideoListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).set(list.get(position));
        }
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder = new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item,parent,false));
        return holder;
    }

    class ItemViewHolder extends ViewHolder {
        ImageView videoGridItemImageIv;
        TextView videoGridItemTitleTv;
        TextView videoGridItemDescTv;

        public ItemViewHolder(View view) {
            super(view);
            videoGridItemImageIv = (ImageView) view.findViewById(R.id.videoGridItemImageIv);
            videoGridItemTitleTv = (TextView)view.findViewById(R.id.videoGridItemTitleTv);
            videoGridItemDescTv =(TextView) view.findViewById(R.id.videoGridItemDescTv);
        }
        public void set(Video video){
            //图片
            Picasso.with(mContext)
                    .load(video.getV_image_thum())
                    .placeholder(R.mipmap.no_video_image)
                    .into(videoGridItemImageIv);
            //标题
            videoGridItemTitleTv.setText(video.getV_name());
            //描述
            videoGridItemDescTv.setText(video.getV_describe());
        }
    }


}
