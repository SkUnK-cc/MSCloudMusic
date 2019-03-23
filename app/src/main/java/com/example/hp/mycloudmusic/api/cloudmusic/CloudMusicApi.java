package com.example.hp.mycloudmusic.api.cloudmusic;

import com.example.hp.mycloudmusic.userinfo.LoginInfo;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CloudMusicApi {

    String host = "http://127.0.0.1:8080/";

    @FormUrlEncoded
    @POST("CloudMusicProject/login.do")
    Observable<LoginInfo> login(@Field("phonenum") String phonenum,
                                @Field("password") String password);

    @FormUrlEncoded
    @POST("CloudMusicProject/register.do")
    Observable<LoginInfo> register(@Field("username")String username,
                                   @Field("phonenum")String phonenum,
                                   @Field("password")String password);
}
