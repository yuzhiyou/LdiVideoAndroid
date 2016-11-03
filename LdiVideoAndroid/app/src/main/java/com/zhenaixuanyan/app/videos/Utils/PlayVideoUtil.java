package com.zhenaixuanyan.app.videos.Utils;

import android.app.Activity;

import com.zhenaixuanyan.app.videos.Beans.Video;

import tcking.github.com.giraffeplayer.GiraffePlayerActivity;

/**
 * Created by Forrest on 2016/10/28.
 */

public class PlayVideoUtil {
    public static void playVideo(Activity activity, Video video){
        GiraffePlayerActivity.configPlayer(activity).play(video.getV_url());
    }
}
