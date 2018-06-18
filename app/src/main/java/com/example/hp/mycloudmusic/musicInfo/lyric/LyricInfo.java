package com.example.hp.mycloudmusic.musicInfo.lyric;

import java.util.ArrayList;
import java.util.List;

public class LyricInfo {

    private List<LineInfo> lines;
    private String title;
    private String artist;
    private String album;
    private long offset;


    public List<LineInfo> getLines() {
        if(lines==null){
            lines = new ArrayList<>();
        }
        return lines;
    }

    public void setLines(List<LineInfo> lines) {
        this.lines = lines;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}
