package com.example.hp.mycloudmusic.api.baidu.net.bean;

import com.example.hp.mycloudmusic.api.baidu.net.ApiData;
import com.example.hp.mycloudmusic.api.baidu.net.ApiService;
import com.example.hp.mycloudmusic.api.baidu.net.RetrofitApi;
import com.example.hp.mycloudmusic.util.LogUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceImpl implements ApiService {

    private static ApiServiceImpl INSTANCE;

    private Retrofit retrofit;
    private OkHttpClient okHttpClient;

    private Gson gson;

    public static ApiServiceImpl getINSTANCE(){
        if(INSTANCE==null){
            INSTANCE = new ApiServiceImpl();
        }
        return INSTANCE;
    }

    private ApiServiceImpl(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .removeHeader("User-Agent")
                                .addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
                                .build() ;
                        return chain.proceed(newRequest);
                    }
                });
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://tingapi.ting.baidu.com/")
                .build();
        gson = new Gson();
    }
    @Override
    public <T extends ApiData> Observable<ApiResponse<T>> doGet(String path, Map<String, String> query, Class<T> responseClass) {
        return Observable.create(new ObservableOnSubscribe<ApiResponse<T>>() {
            @Override
            public void subscribe(ObservableEmitter<ApiResponse<T>> observableEmitter) throws Exception {
                RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
                Call<ResponseBody> call = retrofitApi.doRawGet(path,query);
                doRetrofitCall(path,call,observableEmitter,query,responseClass);
            }
        }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io());
    }

    /**
     *
     * @param <T> 此处 T 不再加约束，在doGet 中已经约束？？
     */
    private <T> void doRetrofitCall(String path, Call<ResponseBody> call, ObservableEmitter<ApiResponse<T>> observableEmitter, Map<String, String> query, Class<T> responseClass) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        try {
            Response<ResponseBody> response = call.execute();
            String content;
            if(response.code()==200){
                content = response.body().string();
                apiResponse.data = gson.fromJson(content,responseClass);
                handleCommonError(apiResponse);
            }else{
                LogUtils.INSTANCE.e("response.code != 200");
                content = "response code: " + response.code();
            }

            LogUtils.INSTANCE.e("content: "+content);
            observableEmitter.onNext(apiResponse);
        } catch (IOException e) {
            e.printStackTrace();
            observableEmitter.onNext(apiResponse);
        } catch (Throwable e){
            e.printStackTrace();
            observableEmitter.onNext(apiResponse);
        }
    }

    private <T> void handleCommonError(ApiResponse<T> apiResponse) {

    }
}
