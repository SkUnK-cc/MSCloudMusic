package com.example.hp.mycloudmusic.mvp.presenter

import com.example.hp.mycloudmusic.mvp.view.IPlayMvView

class PlayMvPresenter : BasePresenter<IPlayMvView>() {

    fun getMvPlayAddr(id: Int,quality:Int){
//        var messApi = RetrofitFactory.provideMessApi()
//        messApi.getMvPlayAddr(id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .observeOn(object : Observer<String>)
    }
}