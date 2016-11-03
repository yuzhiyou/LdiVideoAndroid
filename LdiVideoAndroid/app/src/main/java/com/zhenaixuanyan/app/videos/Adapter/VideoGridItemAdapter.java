package com.zhenaixuanyan.app.videos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhenaixuanyan.app.videos.Beans.Video;
import com.zhenaixuanyan.app.videos.R;

import java.util.List;

/**
 * Created by Forrest on 2016/10/25.
 */
public class VideoGridItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<Video> videoList;

    public VideoGridItemAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public void setList(List<Video> videoList){
        this.videoList = videoList;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.videoList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return this.videoList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.video_grid_item, null);

            holder = new ViewHolder();
            holder.videoGridItemImageIv = (ImageView) convertView
                    .findViewById(R.id.videoGridItemImageIv);
            holder.videoGridItemTitleTv = (TextView) convertView
                    .findViewById(R.id.videoGridItemTitleTv);
            holder.videoGridItemDescTv = (TextView) convertView
                    .findViewById(R.id.videoGridItemDescTv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //图片
        Picasso.with(mContext)
                .load(videoList.get(position).getV_image_thum())
                .placeholder(R.mipmap.no_video_image)
                .into(holder.videoGridItemImageIv);
        //标题
        holder.videoGridItemTitleTv.setText(videoList.get(position).getV_name());
        //描述
        holder.videoGridItemDescTv.setText(videoList.get(position).getV_describe());
        return convertView;
    }

    final class ViewHolder {
        ImageView videoGridItemImageIv;
        TextView videoGridItemTitleTv;
        TextView videoGridItemDescTv;
    }


}