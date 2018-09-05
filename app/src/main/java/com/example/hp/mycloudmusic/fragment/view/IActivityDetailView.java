package com.example.hp.mycloudmusic.fragment.view;

import com.example.hp.mycloudmusic.musicInfo.artistDetail.ArtistInfoResp;
import com.example.hp.mycloudmusic.mvp.view.IBaseView;

public interface IActivityDetailView extends IBaseView {

    void obtainInfoSuccess(ArtistInfoResp resp);

    void obtainInfoFailed();

}
