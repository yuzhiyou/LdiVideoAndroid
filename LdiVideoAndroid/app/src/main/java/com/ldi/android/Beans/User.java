package com.ldi.android.Beans;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Forrest on 16/6/27.
 * @author forrest
 */
public class User implements Serializable{
    //存储KEY
    public static final String USER_STORAGE_KEY = "user_info";


    private int u_id;           //用户ID
    private String u_name;      //用户名称
    private String u_password;  //用户密码
    private String u_card_id;   //身份证编号
    private String u_card_front;    //身份证正面
    private String u_card_back;     //身份证背面
    private boolean u_verify;       //是否已认证
    private String u_verify_date;  //认证日期

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_password() {
        return u_password;
    }

    public void setU_password(String u_password) {
        this.u_password = u_password;
    }

    public String getU_card_id() {
        return u_card_id;
    }

    public void setU_card_id(String u_card_id) {
        this.u_card_id = u_card_id;
    }

    public String getU_card_front() {
        return u_card_front;
    }

    public void setU_card_front(String u_card_front) {
        this.u_card_front = u_card_front;
    }

    public String getU_card_back() {
        return u_card_back;
    }

    public void setU_card_back(String u_card_back) {
        this.u_card_back = u_card_back;
    }

    public boolean isU_verify() {
        return u_verify;
    }

    public void setU_verify(boolean u_verify) {
        this.u_verify = u_verify;
    }

    public String getU_verify_date() {
        return u_verify_date;
    }

    public void setU_verify_date(String u_verify_date) {
        this.u_verify_date = u_verify_date;
    }

    /**
     * 保存用户信息
     * **/
    public static void saveUser(Context context, User user) {
        SharedPreferences preferences = context.getSharedPreferences(
                USER_STORAGE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user", new Gson().toJson(user,User.class));
        editor.apply();
    }

    public static User getUser(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                USER_STORAGE_KEY, Context.MODE_PRIVATE);
        User user = null;
        String jsonStr = preferences.getString("user", "");
        if (!TextUtils.isEmpty(jsonStr)) {
            user = new Gson().fromJson(jsonStr,User.class);
        }

        return user;
    }
    public static void clearUser(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                USER_STORAGE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
