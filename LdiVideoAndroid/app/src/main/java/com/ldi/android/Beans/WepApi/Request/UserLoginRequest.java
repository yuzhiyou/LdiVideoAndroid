package com.ldi.android.Beans.WepApi.Request;

/**
 * Created by Forrest on 16/6/28.
 * @author forrest
 */
public class UserLoginRequest {

    private String u_phone;
    private String u_password;

    public String getU_phone() {
        return u_phone;
    }

    public void setU_phone(String u_phone) {
        this.u_phone = u_phone;
    }

    public String getU_password() {
        return u_password;
    }

    public void setU_password(String u_password) {
        this.u_password = u_password;
    }
}
