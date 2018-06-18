package com.example.hp.mycloudmusic.musicInfo.merge;

import com.example.hp.mycloudmusic.musicInfo.IQueryResult;

public class Album implements IQueryResult{

    /**
     * 周杰伦
     "resource_type_ext":"0",
     "all_artist_id":"29",
     "publishtime":"2016-06-24",
     "company":"杰威尔JVR音乐有限公司",
     "album_desc":"",
     "title":"<em>周杰伦</em>的床边故事",
     "album_id":"266322553",
     "pic_small":"http://qukufile2.qianqian.com/data2/pic/98c6dc2454b0b891951652bb29c16cf2/266322553/266322553.jpg@s_1,w_90,h_90",
     "hot":3574,
     "author":"<em>周杰伦</em>",
     "artist_id":"29"
     */

    public String resource_type_ext;
    public String all_artist_id;
    public String publishtime;
    public String company;
    public String album_desc;
    public String title;
    public String album_id;
    public String pic_small;
    public String hot;
    public String author;
    public String artist_id;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int getSearchResultType() {
        return 0;
    }
}
