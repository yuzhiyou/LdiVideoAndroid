package com.zhenaixuanyan.app.videos.Net;

import com.zhenaixuanyan.app.videos.Beans.WepApi.Request.CheckCodeRequest;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Request.IndexVideoRequest;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Request.UserFindRequest;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Request.UserLoginRequest;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Request.UserRegisterRequest;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Response.IndexVideoResponse;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Response.StatusResponse;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Response.UserLoginResponse;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Response.UserRegisterResponse;
import com.zhenaixuanyan.app.videos.Beans.WepApi.Response.VideoResponse;
import com.zhenaixuanyan.app.videos.Constants;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.RequiresHeader;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientHeaders;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Created by Forrest on 16/5/17.
 * @author forrest
 */
@Rest(rootUrl = Constants.kWebApiUrl, converters = {GsonHttpMessageConverter.class},requestFactory = OkHttp3ClientHttpRequestFactory.class)
public interface MyRestClient extends RestClientHeaders {
    /**
     发送验证码
     */
    @Post("/v1/user/sendLinkCode")
    StatusResponse getCheckCode(@Body CheckCodeRequest action);
    /**
     用户注册
     */
    @Post("/v1/user/regist")
    UserRegisterResponse userRegister(@Body UserRegisterRequest action);
    /**
     用户登录
     */
    @Post("/v1/user/login")
    UserLoginResponse userLogin(@Body UserLoginRequest action);
    /**
     用户修改
     */
    @Post("/v1/user/edit")
    @RequiresHeader("Content-Type")
    UserLoginResponse userEdit(@Body MultiValueMap<String,Object> data);
    /**
     获取用户
     */
    @Post("/v1/user/find")
    UserLoginResponse userFind(@Body UserFindRequest action);

    /**
     搜索视频
     */
    @Post("/v1/video/query")
    String queryVideo(@Body MultiValueMap<String, Object> data);

    /**
     添加观看记录
     */
    @Post("/v1/video/watch/save")
    String saveWatch(@Body MultiValueMap<String, Object> data);

    /**
     * 获取首页数据
     * */
    @Post("/v1/video/index")
    IndexVideoResponse getHomeVideoList(@Body LinkedMultiValueMap<String,String> request);
    /**
     * 获取所有推荐视频
     * */
    @Post("/v1/video/hot")
    VideoResponse getHotVideoList(@Body IndexVideoRequest data);
    /**
     * 获取所有示例视频
     * */
    @Post("/v1/video/sample")
    VideoResponse getSampleVideoList(@Body IndexVideoRequest data);
    /**
     * 获取所有我的视频
     * */
    @Post("/v1/video/myvideo")
    VideoResponse getMyVideoList(@Body IndexVideoRequest data);
    /**
     * 获取所有历史播放视频
     * */
    @Post("/v1/watch/query")
    VideoResponse getHistoryVideoList(@Body IndexVideoRequest data);
 }