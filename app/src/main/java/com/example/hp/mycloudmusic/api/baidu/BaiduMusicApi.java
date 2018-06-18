package com.example.hp.mycloudmusic.api.baidu;

import com.example.hp.mycloudmusic.musicInfo.merge.QueryMergeResp;
import com.example.hp.mycloudmusic.musicInfo.search.MusicSearchSugResp;
import com.example.hp.mycloudmusic.util.ConstantValue;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BaiduMusicApi {

    /**
     * 关键词建议
     * @param query
     * @return
     */
    @GET(ConstantValue.V1_TING+"?method="+ConstantValue.SEARCH_CATALOGSUG)
    Observable<MusicSearchSugResp> querySug(@Query("query")String query);

    @GET(ConstantValue.V1_TING+"?method="+ConstantValue.QUERY_MERGE)
    Observable<QueryMergeResp> queryMerge(@Query("query")String query,
                                          @Query("page_no")int pageNo,
                                          @Query("page_size")int pageSize);
}
