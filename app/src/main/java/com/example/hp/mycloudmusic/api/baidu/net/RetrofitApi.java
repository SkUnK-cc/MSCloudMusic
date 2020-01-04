package com.example.hp.mycloudmusic.api.baidu.net;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface RetrofitApi {

    /**
     *
     * @return 返回值定义为ResponseBody，后面进行转换
     */
    @GET
    Call<ResponseBody> doRawGet(@Url String url, @QueryMap Map<String,String> query);
}
