package com.example.hp.mycloudmusic.executor;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.hp.mycloudmusic.api.kugou.KuGouLyricApi;
import com.example.hp.mycloudmusic.rx.BaseObserver;
import com.example.hp.mycloudmusic.util.FileMusicUtils;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public abstract class SearchLyric implements IExecutor {

    public static final String TAG = "SearchLyric";
    private String title;
    private String artist;
    private static final String KUGOULYRIC = "http://m.kugou.com/";

    public SearchLyric(String title,String artist){
        this.title = title;
        this.artist = artist;
    }

    public void execute(){
        searchLyric();
    }

    private KuGouLyricApi getKugouLyricApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KUGOULYRIC)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(KuGouLyricApi.class);
    }

    /**
     * 访问网络权限
     */
    private void searchLyric() {
        Log.e(TAG, "searchLyric: 网络搜索歌词");
        KuGouLyricApi lyricApi = getKugouLyricApi();
        lyricApi.getKuGouLyric(title+"-"+artist)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            //此处responseBody.string()只能调用一次
//                            Log.e(TAG, "搜索结果： "+responseBody.string());
                            downloadLyric(responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        Log.e(TAG, "onError: search Lyric failed!\n",e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        Log.e(TAG, "onComplete: search Lyric complete!" );
                    }
                });
    }
    private void downloadLyric(String result){
        Log.e(TAG, "downloadLyric: 搜索结果:"+result);
        Log.e(TAG, "downloadLyric: 开始下载到文件");
        if(result == null || TextUtils.isEmpty(result)){
            Log.e(TAG, "result is empty");
            return;
        }
        String filePath = FileMusicUtils.getLrcDir()+FileMusicUtils.getLrcFileName(title,artist);
        FileMusicUtils.saveLrcFile(filePath,result);
        getLrcSuccess(filePath);
    }
}
