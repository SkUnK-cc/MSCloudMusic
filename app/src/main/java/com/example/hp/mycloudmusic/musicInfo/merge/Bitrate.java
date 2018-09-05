package com.example.hp.mycloudmusic.musicInfo.merge;

import android.net.Uri;
import android.os.Parcel;

import com.example.hp.mycloudmusic.musicInfo.AbstractMusic;

import java.util.List;

public class Bitrate extends AbstractMusic{

    public int errorCode;
    public BitrateData data;
    public BSong firstBSong;

    public boolean isValid(){
        return errorCode==0 || errorCode==22000;
    }

    private BSong getFirstItem(){
        if(firstBSong==null) {
            List<BSong> list = data.getSongList();
            if (list.size() != 0) {
                firstBSong = list.get(0);
            }
        }
        return firstBSong;
    }

    @Override
    public String getTitle() {
        return getFirstItem().getSongName();
    }

    @Override
    public String getArtist() {
        return getFirstItem().getArtistName();
    }

    @Override
    public Uri getDataSource() {
        return null;
    }

    @Override
    public long getDuration() {
        return getFirstItem().getTime();
    }

    @Override
    public String getType() {
        return TYPE_ONLINE;
    }

    @Override
    public String getAlbumTitle() {
        return getFirstItem().getAlbumName();
    }

    @Override
    public Artist obtainArtist() {
        return null;
    }

    @Override
    public String getAlbumPic() {
        return getFirstItem().getSongPicSmall();
    }

    @Override
    public String getAlbumPicHuge() {
        return getFirstItem().getSongPicBig();
    }

    @Override
    public String getAlbumPicPremium() {
        return getFirstItem().getSongPicRadio();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public AbstractMusic createFromParcel(Parcel source) {
        return null;
    }

    @Override
    public AbstractMusic[] newArray(int size) {
        return new AbstractMusic[0];
    }
}
