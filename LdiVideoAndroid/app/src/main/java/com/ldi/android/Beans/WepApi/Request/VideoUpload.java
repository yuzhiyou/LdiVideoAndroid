package com.ldi.android.Beans.WepApi.Request;

import java.io.File;

/**
 * Created by kangyawei on 2016/7/3.
 */
public class VideoUpload {
    private int v_userid;
    private String v_name;
    private File myfiles;
    private String v_describe;

    public int getV_userid() {
        return v_userid;
    }

    public void setV_userid(int v_userid) {
        this.v_userid = v_userid;
    }

    public String getV_name() {
        return v_name;
    }

    public void setV_name(String v_name) {
        this.v_name = v_name;
    }

    public File getMyfiles() {
        return myfiles;
    }

    public void setMyfiles(File myfiles) {
        this.myfiles = myfiles;
    }

    public String getV_describe() {
        return v_describe;
    }

    public void setV_describe(String v_describe) {
        this.v_describe = v_describe;
    }
}
