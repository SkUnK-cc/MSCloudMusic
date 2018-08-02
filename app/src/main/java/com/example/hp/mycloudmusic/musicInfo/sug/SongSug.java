package com.example.hp.mycloudmusic.musicInfo.sug;

import android.net.Uri;
import android.os.Parcel;

import com.example.hp.mycloudmusic.musicInfo.AbstractMusic;
import com.example.hp.mycloudmusic.musicInfo.IQueryResult;

public class SongSug extends AbstractMusic implements IQueryResult{

    /**
     "bitrate_fee":"{"0":"0|0","1":"0|0"}",
     "weight":"29669099",
     "songname":"演员",
     "resource_type":"0",
     "songid":"242078437",
     "has_mv":"0",
     "yyr_artist":"0",
     "resource_type_ext":"0",
     "artistname":"薛之谦",
     "info":"",
     "resource_provider":"1",
     "control":"0000000000",
     "encrypted_songid":"00081DEFB5AF085AB8256D"
     */
    public String songid;
    public String songname;
    public String artistname;
    public String has_mv;
    public String encrypted_songid;
    public String yyr_artist;
    public String control;
    public String weight;
    public String resource_type;
    public String resource_type_ext;
    public String info;
    public String resource_provider;

    public SongSug(){
    }

    @Override
    public String getTitle() {
        return songname;
    }

    @Override
    public String getArtist() {
        return artistname;
    }

    @Override
    public Uri getDataSource() {
        return null;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public String getType() {
        return TYPE_ONLINE;
    }

    @Override
    public String getAlbumPic() {
        return null;
    }

    public SongSug(Parcel source) {
        songid = source.readString();
        songname = source.readString();
        artistname = source.readString();
        has_mv = source.readString();
        encrypted_songid = source.readString();
        yyr_artist = source.readString();
        control = source.readString();
        weight = source.readString();
        resource_type = source.readString();
        resource_type_ext = source.readString();
        info = source.readString();
        resource_provider = source.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(songid);
        dest.writeString(songname);
        dest.writeString(artistname);
        dest.writeString(has_mv);
        dest.writeString(encrypted_songid);
        dest.writeString(yyr_artist);
        dest.writeString(control);
        dest.writeString(weight);
        dest.writeString(resource_type);
        dest.writeString(resource_type_ext);
        dest.writeString(info);
        dest.writeString(resource_provider);
    }

    /**             Creator接口方法                 */
    @Override
    public SongSug createFromParcel(Parcel source) {
        return new SongSug(source);
    }

    @Override
    public SongSug[] newArray(int size) {
        return new SongSug[size];
    }

    /** ------------------------------------------  */
    @Override
    public String getName() {
        return songname;
    }

    @Override
    public int getSearchResultType() {
        return SONG;
    }

}
