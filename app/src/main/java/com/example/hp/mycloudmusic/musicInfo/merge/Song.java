package com.example.hp.mycloudmusic.musicInfo.merge;

import android.net.Uri;
import android.os.Parcel;

import com.example.hp.mycloudmusic.musicInfo.AbstractMusic;
import com.example.hp.mycloudmusic.musicInfo.IQueryResult;

public class Song extends AbstractMusic implements IQueryResult{

    /**
     "resource_type_ext":"2",
     "has_filmtv":"0",
     "resource_type":0,
     "mv_provider":"0000000000",
     "del_status":"0",
     "havehigh":2,
     "si_proxycompany":"北京众水之音文化传播有限公司",
     "versions":"",
     "toneid":"0",
     "info":"",
     "has_mv":0,
     "album_title":"周杰伦",
     "content":"",
     "piao_id":"0",
     "artist_id":"591517581",
     "lrclink":"http://qukufile2.qianqian.com/data2/lrc/c2d8d0ff9094db62bcb41cac307227fd/591517629/591517629.lrc",
     "data_source":0,
     "relate_status":0,
     "learn":0,
     "album_id":"591517650",
     "biaoshi":"vip,lossless",
     "bitrate_fee":"{"0":"0|0","1":"-1|-1"}",
     "song_source":"web",
     "is_first_publish":0,
     "cluster_id":0,
     "charge":0,
     "copy_type":"1",
     "korean_bb_song":"0",
     "all_rate":"96,128,224,320,flac",
     "title":"周杰伦",
     "has_mv_mobile":0,
     "author":"山弟",
     "pic_small":"http://qukufile2.qianqian.com/data2/pic/a1d2f25a0fd6ac3b93027a53b82e414f/591517598/591517598.jpg@s_1,w_90,h_90",
     "song_id":"591517652",
     "all_artist_id":"591517581",
     "ting_uid":"340422426"
     */
    public String resource_type_ext;
    public String has_filmtv;
    public int resource_type;
    public String mv_provider;
    public String del_status;
    public int havehigh;
    public String si_proxycompany;
    public String versions;
    public String toneid;
    public String info;
    public int has_mv;
    public String album_title;
    public String content;
    public String piao_id;
    public String artist_id;    //目前暂不能使用
    public String lrclink;
    public int data_source;
    public int relate_status;
    public int learn;
    public String album_id;     //专辑id，与Bitrate中的相同
    public String biaoshi;
    public String bitrate_fee;
    public String song_source;
    public int is_first_publish;
    public int cluster_id;
    public int charge;
    public String copy_type;
    public String korean_bb_song;
    public String all_rate;
    public String title;
    public int has_mv_mobile;
    public String author;
    public String pic_small;      //专辑封面
    public String song_id;        //歌曲id
    public String all_artist_id;
    public String ting_uid;       //歌手id



    @Override
    public String getName() {
        return title;
    }


    @Override
    public int getSearchResultType() {
        return SONG;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getArtist() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
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
        return pic_small;
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
