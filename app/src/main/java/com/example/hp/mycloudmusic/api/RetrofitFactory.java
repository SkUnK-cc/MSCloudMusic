package com.example.hp.mycloudmusic.api;

import com.example.hp.mycloudmusic.api.baidu.BaiduMusicApi;
import com.example.hp.mycloudmusic.api.cloudmusic.CloudMusicApi;
import com.example.hp.mycloudmusic.api.netease.MessApi;
import com.example.hp.mycloudmusic.api.netease.NeteaseApi;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    private static final Map<String,Object> serviceMap = new ConcurrentHashMap<>();

    public static BaiduMusicApi provideBaiduApi(){
        Object value;
        if(serviceMap.containsKey(BaiduMusicApi.HOST)){
            Object obj = serviceMap.get(BaiduMusicApi.HOST);
            if(obj==null){
                value = createBaiduApi();
                serviceMap.put(BaiduMusicApi.HOST,value);
            }else{
                value = obj;
            }
        }else{
            value = createBaiduApi();
            serviceMap.put(BaiduMusicApi.HOST,value);
        }
        return (BaiduMusicApi) value;
    }
    public static NeteaseApi provideNeteaseApi(){
        Object value;
        if(serviceMap.containsKey(NeteaseApi.Companion.getBaseUrl())){
            Object obj = serviceMap.get(NeteaseApi.Companion.getBaseUrl());
            if(obj==null){
                value = createNeteaseApi();
                serviceMap.put(NeteaseApi.Companion.getBaseUrl(),value);
            }else{
                value = obj;
            }
        }else{
            value = createNeteaseApi();
            serviceMap.put(NeteaseApi.Companion.getBaseUrl(),value);
        }
        return (NeteaseApi) value;
    }
    public static CloudMusicApi provideCloudMusicApi(){
        Object value;
        if(serviceMap.containsKey(CloudMusicApi.host)){
            Object obj = serviceMap.get(CloudMusicApi.host);
            if(obj==null){
                value = createCloudMusicApi();
                serviceMap.put(CloudMusicApi.host,value);
            }else{
                value = obj;
            }
        }else{
            value = createCloudMusicApi();
            serviceMap.put(CloudMusicApi.host,value);
        }
        return (CloudMusicApi) value;
    }

    public static MessApi provideMessApi(){
        Object value;
        if(serviceMap.containsKey(MessApi.Companion.getMessApiHost())){
            Object obj = serviceMap.get(MessApi.Companion.getMessApiHost());
            if(obj==null){
                value = createMessApi();
                serviceMap.put(MessApi.Companion.getMessApiHost(),value);
            }else{
                value = obj;
            }
        }else{
            value = createMessApi();
            serviceMap.put(MessApi.Companion.getMessApiHost(),value);
        }
        return (MessApi) value;
    }

    private static Object createMessApi() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .removeHeader("User-Agent")
                        .addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                        .build();
                return chain.proceed(newRequest);
            }
        });
        OkHttpClient client = builder.build();
        return new Retrofit.Builder()
                .baseUrl(MessApi.Companion.getMessApiHost())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(MessApi.class);
    }

    private static CloudMusicApi createCloudMusicApi() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .removeHeader("User-Agent")
                        .addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                        .build();
                return chain.proceed(newRequest);
            }
        });
        OkHttpClient client = builder.build();
        return new Retrofit.Builder()
                .baseUrl(CloudMusicApi.host)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(CloudMusicApi.class);
    }
    private static NeteaseApi createNeteaseApi() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .removeHeader("User-Agent")
                        .addHeader("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1")
                        .build();
                return chain.proceed(newRequest);
            }
        });
        OkHttpClient client = builder.build();
        return new Retrofit.Builder()
                .baseUrl(NeteaseApi.Companion.getBaseUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(NeteaseApi.class);
    }

    public static BaiduMusicApi createBaiduApi(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .removeHeader("User-Agent")
                        .addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                        .build();
                return chain.proceed(newRequest);
            }
        });
        OkHttpClient client = builder.build();
        return new Retrofit.Builder()
                .baseUrl(BaiduMusicApi.HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build().create(BaiduMusicApi.class);
    }
}
