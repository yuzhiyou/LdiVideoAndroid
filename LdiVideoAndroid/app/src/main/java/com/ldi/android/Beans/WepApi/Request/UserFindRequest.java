package com.ldi.android.Beans.WepApi.Request;

/**
 * Created by kangyawei on 2016/6/28.
 */

public class UserFindRequest {
    private int u_id;
    private String u_password;

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public String getU_password() {
        return u_password;
    }

    public void setU_password(String u_password) {
        this.u_password = u_password;
    }
}
