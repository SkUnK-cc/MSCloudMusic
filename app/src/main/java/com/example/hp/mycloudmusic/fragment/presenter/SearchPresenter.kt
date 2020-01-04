package com.example.hp.mycloudmusic.fragment.presenter

import com.example.hp.mycloudmusic.api.RetrofitFactory
import com.example.hp.mycloudmusic.api.RxSchedulers
import com.example.hp.mycloudmusic.api.netease.NeteaseApi
import com.example.hp.mycloudmusic.fragment.view.ISearchView
import com.example.hp.mycloudmusic.musicInfo.mv.FirstMvList
import com.example.hp.mycloudmusic.mvp.presenter.BasePresenter
import com.example.hp.mycloudmusic.rx.BaseObserver

class SearchPresenter : BasePresenter<ISearchView>() {
    init {
    }

    fun getFirstMvs(){
        val mNeteaseApi: NeteaseApi = RetrofitFactory.provideNeteaseApi()
        mNeteaseApi.getFirstMvs(10)
                .compose(RxSchedulers.compose())
                .subscribe(object: BaseObserver<FirstMvList>() {
                    override fun onNext(data: FirstMvList) {
                        if (data.code==200)mView.showMvs(data)
                        else mView.loadMvsFail(data.code)
                    }
                })
    }

}