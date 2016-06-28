package com.ldi.android.Beans.WepApi.Response;

import com.ldi.android.Beans.User;

/**
 * Created by Forrest on 16/6/28.
 */
public class UserRegisterResponse {
    private int status;
    private User data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
