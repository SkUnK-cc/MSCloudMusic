package com.example.hp.mycloudmusic.musicInfo;

import android.net.Uri;
import android.os.Parcel;

import com.example.hp.mycloudmusic.musicInfo.merge.Artist;
import com.example.hp.mycloudmusic.musicInfo.merge.Song;
import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.io.File;

@Table("tb_audio")
public class AudioBean extends AbstractMusic{
    public static final String COL_ID="_id";
    public static final String COL_LOCAL_ID = "local_id";
    public static final String COL_TYPE="type";
    public static final String COL_TITLE="title";
    public static final String COL_ARTIST="artist";
    public static final String COL_ALBUM="album";
    public static final String COL_ALBUM_ID_LOCAL="album_id_local";
    public static final String COL_COVER_PATH="cover_path";
    public static final String COL_DURATION="duration";
    public static final String COL_PATH = "duration";
    public static final String COL_FILE_NAME = "file_name";
    public static final String COL_FILE_SIZE = "file_size";

    public static final String COL_SONG_ID = "song_id";
    public static final String COL_TING_UID = "ting_uid";
    public static final String COL_ALBUM_ID = "album_id";
    public static final String COL_HAS_MV = "has_mv";
    public static final String COL_ARTIST_ID = "artist_id";
    public static final String COL_ALL_ARTIST_ID = "all_artist_id";
    public static final String COL_LRCLINK = "lrclink";
    public static final String COL_PIC_SMALL = "pic_small";


    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private long id;
    @Column(COL_LOCAL_ID)
    private long local_id;
    @Column(COL_TYPE)
    private int type;
    @Column(COL_TITLE)
    private String title;
    @Column(COL_ARTIST)
    private String artist;
    @Column(COL_ALBUM)
    private String album;
    @Column(COL_ALBUM_ID_LOCAL)
    private long albumIdLocal;
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

    @Column(COL_SONG_ID)
    private String song_id;
    @Column(COL_TING_UID)
    private String ting_uid;
    @Column(COL_ALBUM_ID)
    private String album_id;
    @Column(COL_HAS_MV)
    private int has_mv;
    @Column(COL_ARTIST_ID)
    private String artist_id;
    @Column(COL_ALL_ARTIST_ID)
    private String all_artist_id;
    @Column(COL_LRCLINK)
    private String lrclink;
    @Column(COL_PIC_SMALL)
    private String pic_small;

    public AudioBean(){
    }

    //用Song构造一个AudioBean存入数据库,待完成
    public AudioBean(Song song){
        this.title = song.title;
        this.artist = song.author;
        this.album = song.album_title;
        this.duration = song.getDuration();

        this.song_id = song.song_id;
        this.ting_uid = song.ting_uid;
        this.album_id = song.album_id;
        this.has_mv = song.has_mv;
        this.artist_id = song.artist_id;
        this.all_artist_id = song.all_artist_id;
        this.artist = song.author;
        this.album = song.album_title;
        this.duration = song.getDuration();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLocal_id() {
        return local_id;
    }

    public void setLocal_id(long local_id) {
        this.local_id = local_id;
    }

    public String getType() {
        return TYPE_LOCAL;
    }

    @Override
    public String getAlbumTitle() {
        return album;
    }

    //这里最好新建一个Artist，如果保存该Artist，当AudioBean中变量的值改变时，需要同时修改Artist中的变量
    @Override
    public Artist obtainArtist() {
        Artist create = new Artist();
        create.author = artist;
        create.artist_id = artist_id==null?"":artist_id;
        create.ting_uid = ting_uid==null?"":ting_uid;
        return create;
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

    public long getAlbumIdLocal() {
        return albumIdLocal;
    }

    public void setAlbumIdLocal(long albumIdLocal) {
        this.albumIdLocal = albumIdLocal;
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

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public String getTing_uid() {
        return ting_uid;
    }

    public void setTing_uid(String ting_uid) {
        this.ting_uid = ting_uid;
    }

    public String getPic_small() {
        return pic_small;
    }

    public void setPic_small(String pic_small) {
        this.pic_small = pic_small;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public int getHas_mv() {
        return has_mv;
    }

    public void setHas_mv(int has_mv) {
        this.has_mv = has_mv;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getAll_artist_id() {
        return all_artist_id;
    }

    public void setAll_artist_id(String all_artist_id) {
        this.all_artist_id = all_artist_id;
    }

    public String getLrclink() {
        return lrclink;
    }

    public void setLrclink(String lrclink) {
        this.lrclink = lrclink;
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
        dest.writeLong(local_id);
        dest.writeInt(type);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeLong(albumIdLocal);
        dest.writeString(coverPath);
        dest.writeLong(duration);
        dest.writeString(path);
        dest.writeString(fileName);
        dest.writeLong(fileSize);

        dest.writeString(song_id);
        dest.writeString(ting_uid);
        dest.writeString(album_id);
        dest.writeInt(has_mv);
        dest.writeString(artist_id);
        dest.writeString(all_artist_id);
        dest.writeString(lrclink);
    }

    @Override
    public AbstractMusic createFromParcel(Parcel source) {
        return new AudioBean(source);
    }

    public AudioBean(Parcel source) {
        id = source.readLong();
        local_id = source.readLong();
        type = source.readInt();
        title = source.readString();
        artist = source.readString();
        album = source.readString();
        albumIdLocal = source.readLong();
        coverPath = source.readString();
        duration = source.readLong();
        path = source.readString();
        fileName = source.readString();
        fileSize = source.readLong();

        song_id = source.readString();
        ting_uid = source.readString();
        album_id = source.readString();
        has_mv = source.readInt();
        artist_id = source.readString();
        all_artist_id = source.readString();
        lrclink = source.readString();
    }

    @Override
    public AbstractMusic[] newArray(int size) {
        return new AudioBean[size];
    }
}
