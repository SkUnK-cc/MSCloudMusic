package com.example.hp.mycloudmusic.api.baidu;

import com.example.hp.mycloudmusic.musicInfo.merge.QueryMergeResp;
import com.example.hp.mycloudmusic.musicInfo.search.MusicSearchSugResp;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BaiduMusicApi {

    String HOST = "http://tingapi.ting.baidu.com/";
    String V1_TING = "v1/restserver/ting";
    String QUERY_MERGE = "baidu.ting.search.merge";
    String SEARCH_CATALOGSUG = "baidu.ting.search.catalogSug";
    /**
     * 关键词建议
     * @param query
     * @return
     */
    @GET(V1_TING+"?method="+SEARCH_CATALOGSUG)
    Observable<MusicSearchSugResp> querySug(@Query("query")String query);

    @GET(V1_TING + "?method=" + QUERY_MERGE)
    Observable<QueryMergeResp> queryMerge(@Query("query") String query,
                                          @Query("page_no") int pageNo,
                                          @Query("page_size") int pageSize);

    @GET(V1_TING + "?method=" + QUERY_MERGE)
    Observable<ResponseBody> queryMergeString(@Query("query") String query,
                                        @Query("page_no") int pageNo,
                                        @Query("page_size") int pageSize);

}
