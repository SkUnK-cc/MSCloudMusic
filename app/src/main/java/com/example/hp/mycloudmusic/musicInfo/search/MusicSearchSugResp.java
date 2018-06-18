package com.example.hp.mycloudmusic.musicInfo.search;

import com.example.hp.mycloudmusic.musicInfo.BaseResp;

import java.util.List;

public class MusicSearchSugResp extends BaseResp {
    public List<SongSug> song;
    public List<AlbumSug> album;
    public String order;
    public List<ArtistSug> artist;
}
