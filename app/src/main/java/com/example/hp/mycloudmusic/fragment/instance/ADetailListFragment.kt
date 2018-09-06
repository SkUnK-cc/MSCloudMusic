package com.example.hp.mycloudmusic.fragment.instance

import android.view.View
import android.view.ViewGroup
import com.example.hp.mycloudmusic.R
import com.example.hp.mycloudmusic.adapter.recyclerview.CommonAdapter
import com.example.hp.mycloudmusic.adapter.recyclerview.CommonOnItemClickListener
import com.example.hp.mycloudmusic.adapter.recyclerview.CommonViewHolder
import com.example.hp.mycloudmusic.api.RetrofitFactory
import com.example.hp.mycloudmusic.api.baidu.BaiduMusicApi
import com.example.hp.mycloudmusic.custom.FullyLinearLayoutManager
import com.example.hp.mycloudmusic.musicInfo.artistDetail.ArtistInfoResp
import com.example.hp.mycloudmusic.mvp.presenter.BasePresenter
import kotlinx.android.synthetic.main.fragment_adetail_song.*

abstract class ADetailListFragment<T : BasePresenter<*>?,D>: BaseFragment<T>() {
    companion object{
        var ARTIST = "ARTIST"
    }
    protected var artist:ArtistInfoResp? = null
    protected var adapter: CommonAdapter<D>? = null
    protected var mPagenum = 0
    protected var mData = ArrayList<D>()
    protected var visible: Boolean = false

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun initView() {
        rv_detail_song.layoutManager = FullyLinearLayoutManager(context)
        adapter = object : CommonAdapter<D>(context,getItemLayout(),mData){
            override fun convert(holder: CommonViewHolder, data: D) {
                mConvert(holder,data)
            }
        }
        (adapter as CommonAdapter<D>).setOnItemClickListener(object : CommonOnItemClickListener<D>{
            override fun onItemClick(parent: ViewGroup?, view: View?, t: D, position: Int) {
                mOnItemClick(position)
            }

            override fun onItemLongClick(parent: ViewGroup?, view: View?, t: D, position: Int): Boolean {
                return false
            }
        })
        rv_detail_song.adapter = adapter
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(userVisibleHint){
            visible = true
            onVisible()
        }else{
            visible = false
            onInvisible()
        }
    }

    abstract fun onVisible()

    abstract fun onInvisible()

    abstract fun mOnItemClick(position: Int)

    abstract fun mConvert(holder: CommonViewHolder, data: D)

    override fun getContentView(): Int {
        return R.layout.fragment_adetail_song
    }

    abstract fun getItemLayout(): Int

    protected fun getBaiduApi() : BaiduMusicApi{
        return RetrofitFactory.provideBaiduApi()
    }

    abstract fun getListFromNet(artist: ArtistInfoResp)
}