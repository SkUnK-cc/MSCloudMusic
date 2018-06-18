package com.example.hp.mycloudmusic.musicInfo.search;

public class AlbumSug {
    /**
     "albumname":"其实我是一个演员",
     "weight":"1",
     "artistname":"孙小翔",
     "resource_type_ext":"0",
     "artistpic":"http://qukufile2.qianqian.com/data2/pic/e52c353971d66b134c7e4a861b22c6de/587578189/587578189.jpg@s_1,w_40,h_40",
     "albumid":"587578185"
     */
    public String albumname;
    public String weight;
    public String artistname;
    public String resource_type_ext;
    public String artistpic;
    public String albumid;

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getArtistname() {
        return artistname;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
    }

    public String getResource_type_ext() {
        return resource_type_ext;
    }

    public void setResource_type_ext(String resource_type_ext) {
        this.resource_type_ext = resource_type_ext;
    }

    public String getArtistpic() {
        return artistpic;
    }

    public void setArtistpic(String artistpic) {
        this.artistpic = artistpic;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }
}
