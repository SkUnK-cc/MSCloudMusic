package com.example.hp.mycloudmusic.musicInfo.merge;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.hp.mycloudmusic.musicInfo.IQueryResult;

public class Artist implements IQueryResult,Parcelable{
    /**
     "ting_uid":"60867779",
     "song_num":126,
     "country":"中国",
     "avatar_middle":"http://qukufile2.qianqian.com/data2/pic/8730017bc87219244ca6234fb748fc0e/591238101/591238101.jpg@s_0,w_120",
     "album_num":18,
     "artist_desc":"",
     "author":"刘瑞琦",
     "artist_source":"web",
     "artist_id":"43888407"
     */
    public String ting_uid;
    public int song_num;
    public String country;
    public String avatar_middle;
    public int album_num;
    public String author;
    public String artist_source;
    public String artist_id;
    public String artist_desc;


    @Override
    public String getName() {
        return author;
    }

    public String getAvatar_middle() {
        return avatar_middle;
    }

    @Override
    public int getSearchResultType() {
        return ARTIST;
    }

    public String getTing_uid() {
        return ting_uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ting_uid);
        dest.writeInt(song_num);
        dest.writeString(country);
        dest.writeString(avatar_middle);
        dest.writeInt(album_num);
        dest.writeString(author);
        dest.writeString(artist_source);
        dest.writeString(artist_id);
        dest.writeString(artist_desc);
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    protected Artist(Parcel in) {
        ting_uid = in.readString();
        song_num = in.readInt();
        country = in.readString();
        avatar_middle = in.readString();
        album_num = in.readInt();
        author = in.readString();
        artist_source = in.readString();
        artist_id = in.readString();
        artist_desc = in.readString();
    }
}
