package com.example.hp.mycloudmusic.download
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class DownloadObserver : Observer<DownloadInfo> {

    constructor(){

    }
    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(t: DownloadInfo) {
    }

    override fun onError(e: Throwable) {
    }
}