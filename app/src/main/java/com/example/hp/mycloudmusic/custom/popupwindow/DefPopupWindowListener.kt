package com.example.hp.mycloudmusic.custom.popupwindow

import android.app.Activity
import com.example.hp.mycloudmusic.R
import com.example.hp.mycloudmusic.base.BaseAppHelper
import com.example.hp.mycloudmusic.download.MusicDownloadManager
import com.example.hp.mycloudmusic.musicInfo.AbstractMusic
import com.example.hp.mycloudmusic.musicInfo.merge.Artist
import com.example.hp.mycloudmusic.musicInfo.merge.Song
import com.example.hp.mycloudmusic.ui.onLine.ArtistDetailActivity

open class DefPopupWindowListener(activity: Activity): PopupWindowManager.PopupWindowListener {

    private var mActivity: Activity = activity

    override fun onItemClick(imageId: Int, music: AbstractMusic?) {
        when (imageId) {
            R.drawable.ic_icon_add_playback -> {
                val service = BaseAppHelper.get().playService
                service?.addNextPlayMusic(music)
            }
            R.drawable.ic_icon_add_list -> {
            }
            R.drawable.ic_icon_artist2 -> {
                val create : Artist = music?.obtainArtist() ?: return
                ArtistDetailActivity.toArtistDetailActivity(mActivity, create)
            }
            R.drawable.ic_icon_download -> MusicDownloadManager.getInstance().downloadSong(music as Song,MusicDownloadManager.TYPE_DOWNLOAD)
            else -> {
            }
        }
    }
}