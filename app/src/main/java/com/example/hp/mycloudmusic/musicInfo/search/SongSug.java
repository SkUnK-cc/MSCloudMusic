package com.example.hp.mycloudmusic.musicInfo.search;

import android.os.Parcel;

import com.example.hp.mycloudmusic.musicInfo.AbstractMusic;

public class SongSug extends AbstractMusic{

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


    /**                 get set                         */
    public String getSongid() {
        return songid;
    }

    public void setSongid(String songid) {
        this.songid = songid;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getArtistname() {
        return artistname;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
    }

    public String getHas_mv() {
        return has_mv;
    }

    public void setHas_mv(String has_mv) {
        this.has_mv = has_mv;
    }

    public String getEncrypted_songid() {
        return encrypted_songid;
    }

    public void setEncrypted_songid(String encrypted_songid) {
        this.encrypted_songid = encrypted_songid;
    }

    public String getYyr_artist() {
        return yyr_artist;
    }

    public void setYyr_artist(String yyr_artist) {
        this.yyr_artist = yyr_artist;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public String getResource_type_ext() {
        return resource_type_ext;
    }

    public void setResource_type_ext(String resource_type_ext) {
        this.resource_type_ext = resource_type_ext;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getResource_provider() {
        return resource_provider;
    }

    public void setResource_provider(String resource_provider) {
        this.resource_provider = resource_provider;
    }
}
