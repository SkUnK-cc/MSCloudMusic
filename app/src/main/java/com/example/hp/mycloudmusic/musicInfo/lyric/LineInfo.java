package com.example.hp.mycloudmusic.musicInfo.lyric;

public class LineInfo {

    private String content;
    //毫秒为单位
    private long start;
    private long end;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
