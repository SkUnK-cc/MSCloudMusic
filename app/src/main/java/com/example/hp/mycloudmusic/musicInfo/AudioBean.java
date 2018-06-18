package com.example.hp.mycloudmusic.musicInfo;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;

@Table("tb_audio")
public class AudioBean {
    public static final String TYPE_LOCAL = "local";
    public static final String TYPE_ONLINE = "online";

    public static final String COL_ID="_id";
    public static final String COL_TYPE="type";
    public static final String COL_TITLE="title";
    public static final String COL_ARTIST="artist";
    public static final String COL_ALBUM="album";
    public static final String COL_ALBUM_ID="album_id";
    public static final String COL_COVER_PATH="cover_path";
    public static final String COL_DURATION="duration";
    public static final String COL_PATH = "duration";
    public static final String COL_FILE_NAME = "file_name";
    public static final String COL_FILE_SIZE = "file_size";

    @Column(COL_ID)
    private long id;
    @Column(COL_TYPE)
    private String type;
    @Column(COL_TITLE)
    private String title;
    @Column(COL_ARTIST)
    private String artist;
    @Column(COL_ALBUM)
    private String album;
    @Column(COL_ALBUM_ID)
    private long albumId;
    @Column(COL_COVER_PATH)
    private String coverPath;
    @Column(COL_DURATION)
    private long duration;
    @Column(COL_PATH)
    private String path;
    @Column(COL_FILE_NAME)
    private String fileName;
    @Column(COL_FILE_SIZE)
    private long fileSize;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }
}
