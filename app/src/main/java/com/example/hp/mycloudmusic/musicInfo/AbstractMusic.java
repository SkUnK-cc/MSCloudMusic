package com.example.hp.mycloudmusic.musicInfo;

import android.net.Uri;
import android.os.Parcelable;

import com.example.hp.mycloudmusic.util.PlayerFormatUtils;

import java.io.Serializable;

/**
 * 定义播放实体的抽象类，子类包含Song（在线音乐）
 */
public abstract class AbstractMusic implements Serializable,Parcelable,Parcelable.Creator<AbstractMusic> {
    public static final String TYPE_LOCAL = "LOCAL";
    public static final String TYPE_ONLINE = "ONLINE";

    public static Creator<AbstractMusic> CREATOR;

    public AbstractMusic(){
        CREATOR = this;
    }

    public abstract String getTitle();

    public abstract String getArtist();


    public abstract Uri getDataSource();

    public abstract long getDuration();

    public abstract String getType();

    public abstract String getAlbumTitle();

    /**
     * 获取专辑图片
     * @return
     */
    public abstract String getAlbumPic();

    public String getAlbumPicHuge(){
        return getAlbumPic();
    }

    public String getAlbumPicPremium(){
        return getAlbumPic();
    }

    public String getDurationStr(){
        return PlayerFormatUtils.formatTime(getDuration());
    }

    public boolean isOnlineMusic(){
        return getType()==TYPE_ONLINE;
    }
}
