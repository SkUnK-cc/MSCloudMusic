package com.example.hp.mycloudmusic.musicInfo.merge;

import com.example.hp.mycloudmusic.musicInfo.IQueryResult;

public class Artist implements IQueryResult{
    /**
     "ting_uid":"60867779",
     "song_num":126,
     "country":"中国",
     "avatar_middle":"http://qukufile2.qianqian.com/data2/pic/8730017bc87219244ca6234fb748fc0e/591238101/591238101.jpg@s_0,w_120",
     "album_num":18,
     "artist_desc":"",
     "author":"刘瑞琦",
     "artist_source":"web",
     "artist_id":"43888407"
     */

    public String ting_uid;
    public int song_num;
    public String country;
    public String avatar_middle;
    public int album_num;
    public String author;
    public String artist_source;
    public String artist_id;


    @Override
    public String getName() {
        return author;
    }

    public String getAvatar_middle() {
        return avatar_middle;
    }

    @Override
    public int getSearchResultType() {
        return ARTIST;
    }

    public String getTing_uid() {
        return ting_uid;
    }
}
