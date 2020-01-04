package com.example.hp.mycloudmusic.mvp.presenter;

import com.example.hp.mycloudmusic.api.RetrofitFactory;
import com.example.hp.mycloudmusic.api.RxSchedulers;
import com.example.hp.mycloudmusic.fragment.view.IActivityDetailView;
import com.example.hp.mycloudmusic.musicInfo.artistDetail.ArtistInfoResp;
import com.example.hp.mycloudmusic.musicInfo.merge.Artist;
import com.example.hp.mycloudmusic.musicInfo.merge.QueryMergeResp;
import com.example.hp.mycloudmusic.rx.BaseObserver;

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
                .subscribe(new BaseObserver<QueryMergeResp>() {
                    @Override
                    public void onNext(QueryMergeResp queryMergeResp) {
                        if(queryMergeResp!=null && queryMergeResp.isValid()){
                            Artist artist2 = queryMergeResp.result.artist_info.artist_list.get(0);
                            getArtistById(artist2);
                        }
                    }
                });
    }

    private void getArtistById(Artist artist) {
        RetrofitFactory.provideBaiduApi()
                .getArtistInfo(artist.artist_id,artist.ting_uid)
                .compose(RxSchedulers.Companion.compose())
                .subscribe(new BaseObserver<ArtistInfoResp>() {
                    @Override
                    public void onNext(ArtistInfoResp artistInfoResp) {
                        if(artistInfoResp!=null && artistInfoResp.isValid()){
                            mView.obtainInfoSuccess(artistInfoResp);
                        }
                    }
                });
    }

}
