package com.example.hp.mycloudmusic.fragment.presenter;

import android.util.Log;

import com.example.hp.mycloudmusic.api.RetrofitFactory;
import com.example.hp.mycloudmusic.api.RxSchedulers;
import com.example.hp.mycloudmusic.api.baidu.net.ApiService;
import com.example.hp.mycloudmusic.api.baidu.net.bean.ApiServiceImpl;
import com.example.hp.mycloudmusic.fragment.view.IMergeView;
import com.example.hp.mycloudmusic.musicInfo.merge.QueryMergeResp;
import com.example.hp.mycloudmusic.mvp.presenter.BasePresenter;
import com.litesuits.orm.LiteOrm;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.example.hp.mycloudmusic.api.baidu.BaiduMusicApi.QUERY_MERGE;
import static com.example.hp.mycloudmusic.api.baidu.BaiduMusicApi.V1_TING;

public class MergePresenter extends BasePresenter<IMergeView> {
    public static final String TAG = "MergePresenter";

    LiteOrm liteOrm;

    public MergePresenter(LiteOrm liteOrm){
        this.liteOrm = liteOrm;
    }

    public Observable<QueryMergeResp> getMergeData2(final String search_word){
        return Observable.create(observableEmitter -> {
            ApiService apiService = ApiServiceImpl.getINSTANCE();
            Map<String,String> map = new HashMap<>();
            map.put("query",search_word);
            map.put("page_no","1");
            map.put("page_size","50");
            apiService.doGet(V1_TING + "?method=" + QUERY_MERGE,map,QueryMergeResp.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(queryMergeRespApiResponse -> {
                        observableEmitter.onNext(queryMergeRespApiResponse.data);
                    });
        });
    }

    public void getMergeData(final String search_word){
        //http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.search.merge&query=taylor&page_no=1&page_size=50

        RetrofitFactory.provideBaiduApi()
                .queryMerge(search_word,1,50)
                .compose(RxSchedulers.Companion.compose())
                .subscribe(new Observer<QueryMergeResp>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(QueryMergeResp queryMergeResp) {
                        if(queryMergeResp!=null && queryMergeResp.isValid()){
                            mView.showMergeData(queryMergeResp);
                        }else{
                            Log.e(TAG, "onNext: queryMergeResp is invalid" );
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: search = " + search_word);
                        Log.e(TAG, "onError\n"+e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                    }
                });

    }

}
