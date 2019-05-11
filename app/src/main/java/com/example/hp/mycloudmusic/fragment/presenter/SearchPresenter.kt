package com.example.hp.mycloudmusic.fragment.presenter

import com.example.hp.mycloudmusic.api.RetrofitFactory
import com.example.hp.mycloudmusic.api.netease.NeteaseApi
import com.example.hp.mycloudmusic.fragment.view.ISearchView
import com.example.hp.mycloudmusic.musicInfo.mv.FirstMvList
import com.example.hp.mycloudmusic.mvp.presenter.BasePresenter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchPresenter : BasePresenter<ISearchView>() {
    init {
    }

    fun getFirstMvs(){
        var mNeteaseApi: NeteaseApi = RetrofitFactory.provideNeteaseApi()
        mNeteaseApi.getFirstMvs(10)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object: Observer<FirstMvList>{
                    override fun onNext(data: FirstMvList) {
                        if (data!=null && data.code==200)mView.showMvs(data)
                        else mView.loadMvsFail(data.code)
                    }
                    override fun onComplete() {
                    }
                    override fun onSubscribe(d: Disposable) {
                    }
                    override fun onError(e: Throwable) {
                    }
                })
    }

}