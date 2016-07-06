package com.ldi.android.Beans.WepApi.Request;

/**
 * Created by Forrest on 16/6/28.
 * @author Forrest
 */
public class CheckCodeRequest {
    private String u_phone;
    private String u_invitation_code;

    public String getU_phone() {
        return u_phone;
    }

    public void setU_phone(String u_phone) {
        this.u_phone = u_phone;
    }

    public String getU_invitation_code() {
        return u_invitation_code;
    }

    public void setU_invitation_code(String u_invitation_code) {
        this.u_invitation_code = u_invitation_code;
    }
}
