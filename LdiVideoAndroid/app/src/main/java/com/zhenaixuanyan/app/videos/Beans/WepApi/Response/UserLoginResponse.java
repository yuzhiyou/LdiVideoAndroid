package com.zhenaixuanyan.app.videos.Beans.WepApi.Response;

import com.zhenaixuanyan.app.videos.Beans.User;

/**
 * Created by Forrest on 16/6/28.
 * @author forrest
 */
public class UserLoginResponse {
    private String status;
    private String message;
    private User data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
