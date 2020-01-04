package com.example.hp.mycloudmusic.ui.mv

import android.view.SurfaceHolder
import com.example.hp.mycloudmusic.R
import com.example.hp.mycloudmusic.mvp.presenter.PlayMvPresenter
import com.example.hp.mycloudmusic.mvp.view.IPlayMvView
import com.example.hp.mycloudmusic.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_play_mv.*
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class PlayMvActivity : BaseActivity<PlayMvPresenter>(),IPlayMvView {


    var mIMediaPlayer: IjkMediaPlayer ?= null

    var mvPath = ""

    var mvid = 0
//    var listener: VideoPlayerListener ?=null

    override fun getContentView(): Int {
        return R.layout.activity_play_mv
    }

    override fun initView() {
        mvid = intent.getIntExtra("mvid",0)
        if(mvid==0)finish()
        svMvPlay.holder.addCallback(SurfaceCallback())
    }

    override fun initPresenter() {
        mPresenter = PlayMvPresenter()
    }

    override fun initData() {
    }

    override fun initListener() {

    }

    inner class SurfaceCallback : SurfaceHolder.Callback{
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            load()
        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
        }

    }

    private fun load() {
        createPlayer()
        mIMediaPlayer?.setDisplay(svMvPlay.holder)
        (mIMediaPlayer as IjkMediaPlayer).prepareAsync()
    }

    private fun createPlayer() {
        mIMediaPlayer?.stop()
        mIMediaPlayer?.setDisplay(null)
        mIMediaPlayer?.release()
//        IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG)
        val ijkMediaPlayer: IjkMediaPlayer = IjkMediaPlayer()
        mIMediaPlayer = ijkMediaPlayer
        (mIMediaPlayer as IjkMediaPlayer).dataSource = "https://v1.itooi.cn/netease/mvUrl?id=$mvid&quality=1080"
        (mIMediaPlayer as IjkMediaPlayer).setOnPreparedListener { v ->

        }

    }

    override fun onDestroy() {
        release()
        super.onDestroy()
    }

    fun release(){
        if(mIMediaPlayer!=null){
            mIMediaPlayer?.stop()
            mIMediaPlayer?.setDisplay(null)
            mIMediaPlayer?.release()
        }
        IjkMediaPlayer.native_profileEnd()
    }
}
