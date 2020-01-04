package com.example.hp.mycloudmusic.musicInfo.lyric;

import com.example.hp.mycloudmusic.musicInfo.BaseResp;

public class Lrc extends BaseResp {
    private String title;
    private String lrcContent;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLrcContent() {
        return lrcContent;
    }

    public void setLrcContent(String lrcContent) {
        this.lrcContent = lrcContent;
    }
}
