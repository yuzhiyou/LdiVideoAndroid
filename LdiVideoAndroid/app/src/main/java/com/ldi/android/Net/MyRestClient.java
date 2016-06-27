package com.ldi.android.Net;

import com.ldi.android.Constants;
import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientHeaders;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;

/**
 * Created by Forrest on 16/5/17.
 * @author forrest
 */
@Rest(rootUrl = Constants.kWebApiUrl, converters = {FormHttpMessageConverter.class,StringHttpMessageConverter.class})
public interface MyRestClient extends RestClientHeaders {

    /**
     用户注册
    */
    @Post("/v1/user/regist")
    String userRegister(@Body MultiValueMap<String, Object> data);
    /**
     用户登录
     */
    @Post("/v1/user/login")
    String userLogin(@Body MultiValueMap<String, Object> data);
 }