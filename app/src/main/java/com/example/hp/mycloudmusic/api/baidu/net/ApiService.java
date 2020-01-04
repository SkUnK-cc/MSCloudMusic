package com.example.hp.mycloudmusic.api.baidu.net;

import com.example.hp.mycloudmusic.api.baidu.net.bean.ApiResponse;

import java.util.Map;

import io.reactivex.Observable;

/**
 * 这个 interface的作用：规范？？？
 */
public interface ApiService {

    /**
     * <T extends ApiData> 约束了 第三个参数class<T> 中的 T 需要继承ApiData
     */
    <T extends ApiData> Observable<ApiResponse<T>> doGet(
            String path, Map<String,String> query,final Class<T> responseClass);

}
