package com.example.hp.mycloudmusic.api.netease

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MessApi {
    companion object {
        var MessApiHost = "https://v1.itooi.cn"
    }

    @GET("/netease/mvUrl")
    fun getMvPlayAddr(@Query("id") id: Int, @Query("quality") quality: Int =480): Observable<String>


//    @GET("/netease/mv")
//    fun getMvInfo(@Query("id") id:Int):Observable<>
}