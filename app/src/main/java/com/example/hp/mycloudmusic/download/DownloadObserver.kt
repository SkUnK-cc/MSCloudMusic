package com.example.hp.mycloudmusic.download
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class DownloadObserver : Observer<DownloadInfo> {

    protected var mInfo: DownloadInfo? = null

    constructor(info : DownloadInfo){
        this.mInfo = info
    }
    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(t: DownloadInfo) {
        if(mInfo == null)this.mInfo = t
    }

    override fun onError(e: Throwable) {
    }
}