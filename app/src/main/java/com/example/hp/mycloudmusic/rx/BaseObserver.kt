package com.example.hp.mycloudmusic.rx

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class BaseObserver<T>: Observer<T> {
    override fun onComplete() {
    }

    override fun onSubscribe(disposable: Disposable) {
    }

    override fun onNext(resp: T) {
    }

    override fun onError(throwable: Throwable) {
        throwable.printStackTrace()
    }
}