package com.example.hp.mycloudmusic.musicInfo.artistDetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.hp.mycloudmusic.musicInfo.BaseResp;

public class ArtistInfoResp extends BaseResp implements Parcelable{

    /**
     "weight":"62.00",
     "nickname":"",
     "ting_uid":"7994",
     "stature":"175.00",
     "avatar_s500":"http://qukufile2.qianqian.com/data2/pic/046d17bfa056e736d873ec4f891e338f/540336142/540336142.jpg@s_0,w_500",
     "source":"web",
     "listen_num":"392224",
     "collect_num":7944,
     "hot":"7683",
     "comment_num":0,
     "area":"1",
     "info":"0",
     "firstchar":"Z",
     "avatar_s180":"http://qukufile2.qianqian.com/data2/pic/046d17bfa056e736d873ec4f891e338f/540336142/540336142.jpg@s_0,w_180",
     "piao_id":"0",
     "avatar_small":"http://qukufile2.qianqian.com/data2/pic/046d17bfa056e736d873ec4f891e338f/540336142/540336142.jpg@s_0,w_48",
     "albums_total":"29",
     "artist_id":"29",
     "constellation":"摩羯",
     "is_collect":0,
     "intro":"周杰伦..."
     "company":"杰威尔,巨室音乐",
     "country":"台湾",
     "share_num":319,
     "bloodtype":"4",
     "avatar_middle":"http://qukufile2.qianqian.com/data2/pic/046d17bfa056e736d873ec4f891e338f/540336142/540336142.jpg@s_0,w_120",
     "mv_total":4153,
     "songs_total":"355",
     "birth":"1979-01-18",
     "url":"http://music.baidu.com/artist/29",
     "gender":"0",
     "avatar_s1000":"http://qukufile2.qianqian.com/data2/pic/046d17bfa056e736d873ec4f891e338f/540336142/540336142.jpg@s_0,w_1000",
     "avatar_mini":"http://qukufile2.qianqian.com/data2/pic/046d17bfa056e736d873ec4f891e338f/540336142/540336142.jpg@s_0,w_20",
     "avatar_big":"http://qukufile2.qianqian.com/data2/pic/046d17bfa056e736d873ec4f891e338f/540336142/540336142.jpg@s_0,w_240",
     "name":"周杰伦",
     "aliasname":""
     */

    public String weight;
    public String nickname;
    public String ting_uid;
    public String stature;
    public String avatar_s500;
    public String source;
    public String listen_num;
    public long collect_num;
    public String hot;
    public long comment_num;
    public String area;
    public String info;
    public String firstchar;
    public String avatar_s180;
    public String piao_id;
    public String avatar_small;
    public String albums_total;
    public String artist_id;
    public String constellation;
    public long is_collect;
    public String intro;
    public String company;
    public String country;
    public long share_num;
    public String bloodtype;
    public String avatar_middle;
    public long mv_total;
    public String songs_total;
    public String birth;
    public String url;
    public String gender;
    public String avatar_s1000;
    public String avatar_mini;
    public String avatar_big;
    public String name;
    public String aliasname;

    public ArtistInfoResp(Parcel in) {
        weight = in.readString();
        nickname = in.readString();
        ting_uid = in.readString();
        stature = in.readString();
        avatar_s500 = in.readString();
        source = in.readString();
        listen_num = in.readString();
        collect_num = in.readLong();
        hot = in.readString();
        comment_num = in.readLong();
        area = in.readString();
        info = in.readString();
        firstchar = in.readString();
        avatar_s180 = in.readString();
        piao_id = in.readString();
        avatar_small = in.readString();
        albums_total = in.readString();
        artist_id = in.readString();
        constellation = in.readString();
        is_collect = in.readInt();
        intro = in.readString();
        company = in.readString();
        country = in.readString();
        share_num = in.readLong();
        bloodtype = in.readString();
        avatar_middle = in.readString();
        mv_total = in.readLong();
        songs_total = in.readString();
        birth = in.readString();
        url = in.readString();
        gender = in.readString();
        avatar_s1000 = in.readString();
        avatar_mini = in.readString();
        avatar_big = in.readString();
        name = in.readString();
        aliasname = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(weight);
        dest.writeString(nickname);
        dest.writeString(ting_uid);
        dest.writeString(stature);
        dest.writeString(avatar_s500);
        dest.writeString(source);
        dest.writeString(listen_num);
        dest.writeLong(collect_num);
        dest.writeString(hot);
        dest.writeLong(comment_num);
        dest.writeString(area);
        dest.writeString(info);
        dest.writeString(firstchar);
        dest.writeString(avatar_s180);
        dest.writeString(piao_id);
        dest.writeString(avatar_small);
        dest.writeString(albums_total);
        dest.writeString(artist_id);
        dest.writeString(constellation);
        dest.writeLong(is_collect);
        dest.writeString(intro);
        dest.writeString(company);
        dest.writeString(country);
        dest.writeLong(share_num);
        dest.writeString(bloodtype);
        dest.writeString(avatar_middle);
        dest.writeLong(mv_total);
        dest.writeString(songs_total);
        dest.writeString(birth);
        dest.writeString(url);
        dest.writeString(gender);
        dest.writeString(avatar_s1000);
        dest.writeString(avatar_mini);
        dest.writeString(avatar_big);
        dest.writeString(name);
        dest.writeString(aliasname);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ArtistInfoResp> CREATOR = new Creator<ArtistInfoResp>() {
        @Override
        public ArtistInfoResp createFromParcel(Parcel source) {
            return new ArtistInfoResp(source);
        }

        @Override
        public ArtistInfoResp[] newArray(int size) {
            return new ArtistInfoResp[size];
        }
    };


}
