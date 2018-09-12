package com.example.hp.mycloudmusic.download

class DownloadInfo {

    companion object {
        final val TOTAL_ERROR: Long = -1
    }

    var internal: DownloadInfo? = null
    var url: String = ""
    var total: Long = -1
    var progress: Long = 0
    var dir: String = ""
    var fileName: String = ""

    constructor(url:String, internal: DownloadInfo?){
        this.url = url
        this.internal = internal
    }
}