package com.example.hp.mycloudmusic.api.netease

import com.example.hp.mycloudmusic.musicInfo.mv.FirstMvList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface NeteaseApi {
    companion object {
        var BaseUrl = "http://music.163.com"
    }

    @GET("/api/mv/first")
    fun getFirstMvs(@Query("limit") limit: Int): Observable<FirstMvList>
}