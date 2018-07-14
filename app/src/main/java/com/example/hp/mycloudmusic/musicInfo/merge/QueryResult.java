package com.example.hp.mycloudmusic.musicInfo.merge;

public class QueryResult {
    public String syn_words;
    public String query;
    public Album_info album_info;
    public Song_info song_info;
    public Artist_info artist_info;

    public String getQuery() {
        return query;
    }

    public Album_info getAlbum_info() {
        return album_info;
    }

    public Song_info getSong_info() {
        return song_info;
    }

    public Artist_info getArtist_info() {
        return artist_info;
    }
}
