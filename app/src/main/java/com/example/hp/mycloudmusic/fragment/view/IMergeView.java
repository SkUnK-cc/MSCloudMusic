package com.example.hp.mycloudmusic.fragment.view;

import com.example.hp.mycloudmusic.musicInfo.merge.QueryMergeResp;
import com.example.hp.mycloudmusic.mvp.view.IBaseView;

public interface IMergeView extends IBaseView {
    void showMergeData(QueryMergeResp queryMergeResp);
}
