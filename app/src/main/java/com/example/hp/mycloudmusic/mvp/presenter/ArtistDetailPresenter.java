package com.example.hp.mycloudmusic.mvp.presenter;

import android.util.Log;

import com.example.hp.mycloudmusic.api.RetrofitFactory;
import com.example.hp.mycloudmusic.api.RxSchedulers;
import com.example.hp.mycloudmusic.fragment.view.IActivityDetailView;
import com.example.hp.mycloudmusic.musicInfo.artistDetail.ArtistInfoResp;
import com.example.hp.mycloudmusic.musicInfo.merge.Artist;
import com.example.hp.mycloudmusic.musicInfo.merge.QueryMergeResp;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ArtistDetailPresenter extends BasePresenter<IActivityDetailView> {

    public ArtistDetailPresenter(){

    }

    public void getArtistInfo(Artist artist){
        if((artist.artist_id != null && !artist.artist_id.equals("")) || (artist.ting_uid!=null && !artist.ting_uid.equals(""))){
            getArtistById(artist);
        }else if(artist.author != null && !artist.author.equals("")){
            getArtistListByName(artist);
        }
    }

    private void getArtistListByName(Artist artist) {
        RetrofitFactory.provideBaiduApi()
                .queryMerge(artist.author,1,20)
                .compose(RxSchedulers.Companion.compose())
                .subscribe(new Observer<QueryMergeResp>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(QueryMergeResp queryMergeResp) {
                        if(queryMergeResp!=null && queryMergeResp.isValid()){
                            Artist artist2 = queryMergeResp.result.artist_info.artist_list.get(0);
                            getArtistById(artist2);
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

    private void getArtistById(Artist artist) {
        RetrofitFactory.provideBaiduApi()
                .getArtistInfo(artist.artist_id,artist.ting_uid)
                .compose(RxSchedulers.Companion.compose())
                .subscribe(new Observer<ArtistInfoResp>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArtistInfoResp artistInfoResp) {
                        if(artistInfoResp!=null && artistInfoResp.isValid()){
                            mView.obtainInfoSuccess(artistInfoResp);
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
