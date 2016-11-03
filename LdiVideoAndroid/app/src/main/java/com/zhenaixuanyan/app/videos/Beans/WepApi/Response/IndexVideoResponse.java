package com.zhenaixuanyan.app.videos.Beans.WepApi.Response;

import com.zhenaixuanyan.app.videos.Beans.WepApi.HomdeVideoData;

/**
 * Created by kangyawei on 2016/8/1.
 */
public class IndexVideoResponse {
    private String status;
    private String message;
    private HomdeVideoData data;

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

    public HomdeVideoData getData() {
        return data;
    }

    public void setData(HomdeVideoData data) {
        this.data = data;
    }
}
