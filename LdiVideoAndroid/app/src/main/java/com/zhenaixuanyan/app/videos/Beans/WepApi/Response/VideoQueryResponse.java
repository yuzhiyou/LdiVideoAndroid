package com.zhenaixuanyan.app.videos.Beans.WepApi.Response;

import com.zhenaixuanyan.app.videos.Beans.Video;

import java.util.ArrayList;

/**
 * Created by kangyawei on 2016/7/3.
 */
public class VideoQueryResponse{
    private String status;
    private String message;
    private int total;
    private int totalPage;
    private int pageSize;
    private int pageIndex;
    private ArrayList<Video> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public ArrayList<Video> getData() {
        return data;
    }

    public void setData(ArrayList<Video> data) {
        this.data = data;
    }
}
