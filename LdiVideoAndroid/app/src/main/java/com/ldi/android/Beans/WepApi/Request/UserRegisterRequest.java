package com.ldi.android.Beans.WepApi.Request;

/**
 * Created by Forrest on 16/6/28.
 * @author forrest
 */
public class UserRegisterRequest {
    private String u_phone;
    private String u_password;
    private String verify_code;

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

    public String getVerify_code() {
        return verify_code;
    }

    public void setVerify_code(String verify_code) {
        this.verify_code = verify_code;
    }
}
