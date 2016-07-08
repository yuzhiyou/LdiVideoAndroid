package com.ldi.android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ldi.android.Beans.Banner;
import com.ldi.android.R;

import java.util.ArrayList;

/**
 * Created by kangyawei on 2016/7/8.
 */

public class BannerAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Banner> bannerList;
    private View.OnClickListener bannerClickListenter;

    public BannerAdapter(Context c,ArrayList<Banner> list){
        this.mContext = c;
        this.bannerList = list;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (bannerList == null) {
            return 0;
        } else if (bannerList.size() == 1) {
            return 1;
        }
        // 返回很大的值使得getView中的position不断增大来实现循环
        return Integer.MAX_VALUE;
    }

    public void setBannerClickListenter(View.OnClickListener bannerClickListenter) {
        this.bannerClickListenter = bannerClickListenter;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewCache holder;
        if (convertView == null) {
            holder = new ViewCache();
            convertView = mInflater.inflate(R.layout.banner_item, null);
            holder.iv_img = (ImageView) convertView.findViewById(R.id.imgView);
            convertView.setTag(holder);
        } else {
            holder = (ViewCache) convertView.getTag();
        }
        final Banner banner = bannerList.get(position
                % bannerList.size());
        holder.iv_img.setImageResource(R.mipmap.banner_default);
        holder.iv_img.setOnClickListener(bannerClickListenter);
        return convertView;
    }

    private final class ViewCache {
        ImageView iv_img;
    }
}
