package com.example.hp.mycloudmusic.fragment.view;

import com.example.hp.mycloudmusic.mvp.view.IBaseView;

public interface IPlayMusicView extends IBaseView {
    void loadLrc(String path);
    void showNotLrc();
}
