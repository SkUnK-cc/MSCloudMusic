package com.example.hp.mycloudmusic.musicInfo.artistDetail

import com.example.hp.mycloudmusic.musicInfo.BaseResp
import com.example.hp.mycloudmusic.musicInfo.merge.Song

data class ArtistSongListResp(var songlist: ArrayList<Song>,
                              var songnums: String,
                              var havemore: Int): BaseResp(){
    /**
     * "songlist":[]
     * songnums : "64"
     * havemore : 0
     */

    fun hasmore(): Boolean{
        return havemore == 1
    }
}