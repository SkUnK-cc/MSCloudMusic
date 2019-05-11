package com.example.hp.mycloudmusic.fragment.view

import com.example.hp.mycloudmusic.musicInfo.mv.FirstMvList
import com.example.hp.mycloudmusic.mvp.view.IBaseView

interface ISearchView : IBaseView {
    fun showMvs(data :FirstMvList)
    fun loadMvsFail(code: Int)
}