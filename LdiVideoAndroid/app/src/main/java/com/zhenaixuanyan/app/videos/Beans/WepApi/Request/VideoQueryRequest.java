package com.zhenaixuanyan.app.videos.Beans.WepApi.Request;

/**
 * Created by kangyawei on 2016/7/3.
 */
public class VideoQueryRequest {
    private String v_name;
    private int pageIndex;
    private int pageSize;

    public String getV_name() {
        return v_name;
    }

    public void setV_name(String v_name) {
        this.v_name = v_name;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
