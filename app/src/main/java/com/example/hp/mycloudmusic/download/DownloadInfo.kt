package com.example.hp.mycloudmusic.download

class DownloadInfo(var url: String, var internal: DownloadInfo?) {

    companion object {
        const val TOTAL_ERROR: Long = -1
    }

    var total: Long = -1
    var progress: Long = 0
    var dir: String = ""
    var fileName: String = ""

    fun getDown():Long{
        var interDown:Long = 0
        if(internal!=null)interDown = internal!!.getDown()
        return progress+interDown
    }
}