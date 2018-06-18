package com.example.hp.mycloudmusic.base;

import com.example.hp.mycloudmusic.musicInfo.AudioBean;
import com.example.hp.mycloudmusic.service.PlayService;

import java.util.ArrayList;
import java.util.List;

public class BaseAppHelper {
    private PlayService mplayService;
    /**
     * 本地音乐播放列表
     */
    private List<AudioBean> mMusicList = new ArrayList<>();

    public void setPlayService(PlayService playService) {
        mplayService = playService;
    }

    public PlayService getPlayService() {
        return mplayService;
    }

    public List<AudioBean> getMusicList() {
        return mMusicList;
    }

    private static class SingletonHolder{
        public static BaseAppHelper INSTANCE= new BaseAppHelper();
    }

    public static BaseAppHelper get(){
        return SingletonHolder.INSTANCE;
    }
}
