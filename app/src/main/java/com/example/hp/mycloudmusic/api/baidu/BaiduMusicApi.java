package com.example.hp.mycloudmusic.api.baidu;

import com.example.hp.mycloudmusic.musicInfo.artistDetail.ArtistInfoResp;
import com.example.hp.mycloudmusic.musicInfo.artistDetail.ArtistSongListResp;
import com.example.hp.mycloudmusic.musicInfo.lyric.Lrc;
import com.example.hp.mycloudmusic.musicInfo.merge.QueryMergeResp;
import com.example.hp.mycloudmusic.musicInfo.songPlay.SongPlayResp;
import com.example.hp.mycloudmusic.musicInfo.sug.MusicSearchSugResp;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BaiduMusicApi {

    String HOST = "http://tingapi.ting.baidu.com/";
    String V1_TING = "v1/restserver/ting";
    String QUERY_MERGE = "baidu.ting.search.merge";
    String SEARCH_CATALOGSUG = "baidu.ting.search.catalogSug";
    String SONG_LRC = "baidu.ting.song.lry";
    String GET_ARTISTSONGLIST = "baidu.ting.artist.getSongList";
    String SONG_PLAY = "baidu.ting.song.play";
    String GET_ARTISTINFO = "baidu.ting.artist.getinfo";
    int pagenSize = 20;
    String DownloadUrl = "http://ting.baidu.com/data/music/links?songIds=";
    /**
     * 关键词建议
     * v1/restserver/ting?method=baidu.ting.search.catalogSug&query=zhoujielun
     * @param query
     * @return
     */
    @GET(V1_TING + "?method="+SEARCH_CATALOGSUG)
    Observable<MusicSearchSugResp> querySug(@Query("query")String query);

    @GET(V1_TING + "?method=" + QUERY_MERGE)
    Observable<QueryMergeResp> queryMerge(@Query("query") String query,
                                          @Query("page_no") int pageNo,
                                          @Query("page_size") int pageSize);

    /**
     * artistid 和 tinguid 可以只填一个
     */
    @GET(V1_TING + "?method=" + GET_ARTISTSONGLIST)
    Observable<ArtistSongListResp> getArtistSongList(@Query("tinguid") String tinguid,
                                                     @Query("artistid") String artistid,
                                                     @Query("offset") int offset,
                                                     @Query("limits") int limits);

    /**
     * 该方法用于kotlin协程
     */
    @GET(V1_TING + "?method=" + GET_ARTISTSONGLIST)
    Call<ArtistSongListResp> getArtistSongListKt(@Query("tinguid") String tinguid,
                                                 @Query("artistid") String artistid,
                                                 @Query("offset") int offset,
                                                 @Query("limits") int limits);

    @GET(V1_TING + "?method=" + SONG_LRC)
    Observable<Lrc> queryLrc(@Query("songid") String songid);

    @GET(V1_TING + "?method=" + SONG_PLAY)
    Observable<SongPlayResp> querySong(@Query("songid") String songid);

    @GET(V1_TING + "?method=" + GET_ARTISTINFO)
    Observable<ArtistInfoResp> getArtistInfo(@Query("artistid")String artistid,
                                             @Query("tinguid")String tinguid);
}
