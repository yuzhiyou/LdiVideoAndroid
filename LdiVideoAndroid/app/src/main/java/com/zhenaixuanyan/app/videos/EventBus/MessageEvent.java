package com.zhenaixuanyan.app.videos.EventBus;

/**
 * Created by Forrest on 16/5/17.
 * @author forrest
 */
public class MessageEvent {

    public static final int LOGIN_SUCCESS_EVENT = 0x11;  //登录成功通知

    private int id;

    public MessageEvent(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
