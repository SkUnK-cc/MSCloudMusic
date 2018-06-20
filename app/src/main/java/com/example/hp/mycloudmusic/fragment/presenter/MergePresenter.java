package com.example.hp.mycloudmusic.fragment.presenter;

import android.util.Log;

import com.example.hp.mycloudmusic.api.RetrofitFactory;
import com.example.hp.mycloudmusic.fragment.view.IMergeView;
import com.example.hp.mycloudmusic.musicInfo.merge.QueryMergeResp;
import com.example.hp.mycloudmusic.mvp.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MergePresenter extends BasePresenter<IMergeView> {
    public static final String TAG = "MergePresenter";

    @Inject
    public MergePresenter(){

    }

    public void getMergeData(String search_word){
        RetrofitFactory.provideBaiduApi()
                .queryMerge(search_word,1,50)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QueryMergeResp>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(QueryMergeResp queryMergeResp) {
                        if(queryMergeResp!=null){
                            Log.e(TAG, "onNext: result != null");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
