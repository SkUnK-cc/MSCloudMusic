package com.example.hp.mycloudmusic.musicInfo;

import android.net.Uri;
import android.os.Parcel;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;

import java.io.File;

@Table("tb_audio")
public class AudioBean extends AbstractMusic{
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
    private int type;
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

    public AudioBean(){
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return TYPE_LOCAL;
    }

    public void setType(int type) {
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


    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public String getAlbumPic() {
        return null;
    }


    //AbstractMusic

    @Override
    public Uri getDataSource() {
        return Uri.fromFile(new File(path));
    }

    @Override
    public long getDuration() {
        return duration;
    }

    private int viewId;

    public int getViewId() {
        return viewId;
    }
    public void setViewId(int id){
        viewId = id;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeInt(type);
        dest.writeString(album);
        dest.writeLong(albumId);
        dest.writeString(coverPath);
        dest.writeLong(duration);
        dest.writeString(path);
        dest.writeString(fileName);
        dest.writeLong(fileSize);
    }

    @Override
    public AbstractMusic createFromParcel(Parcel source) {
        return new AudioBean(source);
    }

    public AudioBean(Parcel source) {
        id = source.readLong();
        title = source.readString();
        artist = source.readString();
        type = source.readInt();
        album = source.readString();
        albumId = source.readLong();
        coverPath = source.readString();
        duration = source.readLong();
        path = source.readString();
        fileName = source.readString();
        fileSize = source.readLong();
    }

    @Override
    public AbstractMusic[] newArray(int size) {
        return new AudioBean[size];
    }
}
