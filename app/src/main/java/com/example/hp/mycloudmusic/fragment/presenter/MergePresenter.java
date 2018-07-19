package com.example.hp.mycloudmusic.fragment.presenter;

import android.util.Log;

import com.example.hp.mycloudmusic.api.RetrofitFactory;
import com.example.hp.mycloudmusic.fragment.view.IMergeView;
import com.example.hp.mycloudmusic.musicInfo.merge.QueryMergeResp;
import com.example.hp.mycloudmusic.musicInfo.merge.Song;
import com.example.hp.mycloudmusic.mvp.presenter.BasePresenter;
import com.litesuits.orm.LiteOrm;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MergePresenter extends BasePresenter<IMergeView> {
    public static final String TAG = "MergePresenter";

    LiteOrm liteOrm;

    public MergePresenter(LiteOrm liteOrm){
        this.liteOrm = liteOrm;
    }

    public void getMergeData(final String search_word){
        //http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.search.merge&query=taylor&page_no=1&page_size=50
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
                        if(queryMergeResp!=null && queryMergeResp.isValid()){
                            Song song = queryMergeResp.result.getSong_info().getSong_list().get(0);
                            Log.e(TAG, "onNext: "+song.getTitle()+"\n"+song.getArtist());
                            mView.showMergeData(queryMergeResp);
                        }else{

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
