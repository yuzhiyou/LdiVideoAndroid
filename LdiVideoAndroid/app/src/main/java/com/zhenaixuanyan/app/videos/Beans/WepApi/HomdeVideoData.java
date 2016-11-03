package com.zhenaixuanyan.app.videos.Beans.WepApi;

import com.zhenaixuanyan.app.videos.Beans.Video;

import java.util.List;

/**
 * Created by kangyawei on 2016/8/1.
 */
public class HomdeVideoData {
    private List<Video> ad_items;
    private List<Video> hot_items;
    private List<Video> sample_items;


    public List<Video> getAd_items() {
        return ad_items;
    }

    public void setAd_items(List<Video> ad_items) {
        this.ad_items = ad_items;
    }

    public List<Video> getHot_items() {
        return hot_items;
    }

    public void setHot_items(List<Video> hot_items) {
        this.hot_items = hot_items;
    }

    public List<Video> getSample_items() {
        return sample_items;
    }

    public void setSample_items(List<Video> sample_items) {
        this.sample_items = sample_items;
    }
}
