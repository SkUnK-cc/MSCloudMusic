package com.example.hp.mycloudmusic.api.cloudmusic;

import com.example.hp.mycloudmusic.userinfo.LoginInfo;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CloudMusicApi {

    String host = "http://127.0.0.1:8080/";

    @FormUrlEncoded
    @POST("CloudMusicProject/servlet/LoginDataServlet")
    Observable<LoginInfo> login(@Field("username") String username,
                                @Field("password") String password,
                                @Field("phonenum") String phonenum);
}
