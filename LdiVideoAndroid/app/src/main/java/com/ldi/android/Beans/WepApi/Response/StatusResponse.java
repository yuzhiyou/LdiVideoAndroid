package com.ldi.android.Beans.WepApi.Response;

import com.ldi.android.Beans.User;

/**
 * Created by Forrest on 16/6/28.
 * @author Forrest
 */
public class StatusResponse {
    private String status;
    private String message;
    private User data;

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

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
