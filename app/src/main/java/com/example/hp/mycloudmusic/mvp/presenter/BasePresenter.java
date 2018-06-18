package com.example.hp.mycloudmusic.mvp.presenter;

import com.example.hp.mycloudmusic.mvp.view.IBaseView;

public interface BasePresenter<V extends IBaseView> {
    void subscribe();

    void unSubcribe();
}
