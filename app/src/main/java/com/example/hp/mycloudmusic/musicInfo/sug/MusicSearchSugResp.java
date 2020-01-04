package com.example.hp.mycloudmusic.musicInfo.sug;

import com.example.hp.mycloudmusic.api.baidu.net.ApiData;
import com.example.hp.mycloudmusic.musicInfo.BaseResp;

import java.util.List;

public class MusicSearchSugResp extends BaseResp implements ApiData{
    public List<SongSug> song;
    public List<AlbumSug> album;
    public List<ArtistSug> artist;
    public String order;
}
