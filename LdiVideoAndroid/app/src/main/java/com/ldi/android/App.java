package com.ldi.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.ldi.android.Beans.User;
import org.androidannotations.annotations.EApplication;
/**
 * Created by Forrest on 16/4/26.
 * @author forrest
 */

@EApplication
public class App extends Application {
    public User mUser = null;   //当前用户
    public boolean isNetworkConnected;
    @Override
    public void onCreate() {
        super.onCreate();
        //网络状态
        isNetworkConnected = getNetWorkStatus();
        //获取用户
        mUser = User.getUser(this);
    }
    /**
     * 获取默认
     * */
    protected boolean getNetWorkStatus() {
        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnected()) {
            if (netinfo.getState() == NetworkInfo.State.CONNECTED) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }
    /**
     * 保存第一次引导页信息
     * **/
    public void saveFirstEnter(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                "ldi_first_enter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("first", false);
        editor.apply();
    }

    public Boolean getFirstEnter(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                "ldi_first_enter", Context.MODE_PRIVATE);
        Boolean isFirst = preferences.getBoolean("first", true);
        return isFirst;
    }
}
