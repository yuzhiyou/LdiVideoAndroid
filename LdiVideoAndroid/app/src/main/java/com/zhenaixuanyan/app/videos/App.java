package com.zhenaixuanyan.app.videos;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zhenaixuanyan.app.videos.Beans.User;

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
        //UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        //
        //// Set the HTTP stack to use. The default is HurlStack which uses HttpURLConnection.
        //// To use OkHttp for example, you have to add the required dependency in your gradle file
        //// and then you can simply un-comment the following line. Read the wiki for more info.
        //OkHttpClient client = new OkHttpClient.Builder()
        //        .followRedirects(true)
        //        .followSslRedirects(true)
        //        .retryOnConnectionFailure(true)
        //        .connectTimeout(15, TimeUnit.SECONDS)
        //        .writeTimeout(30, TimeUnit.SECONDS)
        //        .readTimeout(30, TimeUnit.SECONDS)
        //        .cache(null)
        //        .build();
        //UploadService.HTTP_STACK = new OkHttpStack(client);
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
