package com.example.hp.mycloudmusic.api;

import com.example.hp.mycloudmusic.api.baidu.BaiduMusicApi;
import com.example.hp.mycloudmusic.util.ConstantValue;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
    public static BaiduMusicApi provideBaiduApi(){
        return new Retrofit.Builder()
                .baseUrl(ConstantValue.HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(BaiduMusicApi.class);
    }
}
