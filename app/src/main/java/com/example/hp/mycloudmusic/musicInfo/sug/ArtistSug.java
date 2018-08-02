package com.example.hp.mycloudmusic.musicInfo.sug;

import com.example.hp.mycloudmusic.musicInfo.IQueryResult;

public class ArtistSug implements IQueryResult{

    /**
     "yyr_artist":"0",
     "artistname":"演员金燕",
     "artistid":"340288273",
     "artistpic":"http://qukufile2.qianqian.com/data2/pic/8e3a996a848d01953c0b9f887744e91a/566697769/566697769.jpg@s_0,w_48",
     "weight":"10"
     */
    public String yyr_artist;
    public String artistname;
    public String artistid;
    public String artistpic;
    public String weight;

    public String getYyr_artist() {
        return yyr_artist;
    }

    public void setYyr_artist(String yyr_artist) {
        this.yyr_artist = yyr_artist;
    }

    public String getArtistname() {
        return artistname;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
    }

    public String getArtistid() {
        return artistid;
    }

    public void setArtistid(String artistid) {
        this.artistid = artistid;
    }

    public String getArtistpic() {
        return artistpic;
    }

    public void setArtistpic(String artistpic) {
        this.artistpic = artistpic;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String getName() {
        return artistname;
    }

    @Override
    public int getSearchResultType() {
        return ARTIST;
    }
}
