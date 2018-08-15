package com.example.hp.mycloudmusic.musicInfo.songPlay

import com.example.hp.mycloudmusic.musicInfo.BaseResp

data class SongPlayResp(val songinfo: SongInfo, val bitrate: Bitrate): BaseResp()