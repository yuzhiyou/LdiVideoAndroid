package com.ldi.android.Net;

import com.ldi.android.Beans.WepApi.Request.CheckCodeRequest;
import com.ldi.android.Beans.WepApi.Request.UserLoginRequest;
import com.ldi.android.Beans.WepApi.Request.UserRegisterRequest;
import com.ldi.android.Beans.WepApi.Response.StatusResponse;
import com.ldi.android.Beans.WepApi.Response.UserLoginResponse;
import com.ldi.android.Constants;
import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientHeaders;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

/**
 * Created by Forrest on 16/5/17.
 * @author forrest
 */
@Rest(rootUrl = Constants.kWebApiUrl, converters = {FormHttpMessageConverter.class,GsonHttpMessageConverter.class,StringHttpMessageConverter.class})
public interface MyRestClient extends RestClientHeaders {
    /**
     发送验证码
     */
    @Post("/v1/user/sendSms")
    StatusResponse getCheckCode(@Body CheckCodeRequest action);
    /**
     验证验证码
     */
    @Post("/v1/user/sendSms")
    StatusResponse verificationCheckCode(@Body CheckCodeRequest action);
    /**
     用户注册
    */
    @Post("/v1/user/regist")
    UserLoginResponse userRegister(@Body UserRegisterRequest action);
    /**
     用户登录
     */
    @Post("/v1/user/login")
    UserLoginResponse userLogin(@Body UserLoginRequest action);
 }