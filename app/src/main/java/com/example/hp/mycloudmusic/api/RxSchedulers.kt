package com.example.hp.mycloudmusic.api

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RxSchedulers {
    companion object {
        fun <T> compose(): ObservableTransformer<T, T> {
            return object: ObservableTransformer<T,T>{
                override fun apply(upstream: Observable<T>): ObservableSource<T> {
                    return upstream
                            .subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                }
            }
        }
    }
}