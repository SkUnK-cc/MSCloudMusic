package com.example.hp.mycloudmusic.download
import com.example.hp.mycloudmusic.rx.BaseObserver

abstract class DownloadObserver(info: DownloadInfo) : BaseObserver<DownloadInfo>() {

    protected var mInfo: DownloadInfo? = info

    override fun onNext(resp: DownloadInfo) {
        if(mInfo == null)this.mInfo = resp
    }
}