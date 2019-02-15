package com.example.hp.mycloudmusic.api;

import com.example.hp.mycloudmusic.api.baidu.BaiduMusicApi;

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
