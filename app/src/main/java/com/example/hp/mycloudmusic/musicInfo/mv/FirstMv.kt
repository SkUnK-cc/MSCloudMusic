package com.example.hp.mycloudmusic.musicInfo.mv

data class FirstMv(
    val artistId: Int,
    val artistName: String,
    val artists: List<Artist>,
    val briefDesc: String,
    val cover: String,
    val desc: Any,
    val duration: Int,
    val id: Int,
    val mark: Int,
    val name: String,
    val playCount: Int,
    val subed: Boolean
)

data class Artist(
    val id: Int,
    val name: String
)