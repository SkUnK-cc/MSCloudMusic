package com.example.hp.mycloudmusic.service.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.hp.mycloudmusic.base.BaseAppHelper
import com.example.hp.mycloudmusic.service.PlayService

class NotificationBroadcast : BroadcastReceiver() {

    companion object {
        val START_OR_PAUSE = "com.example.hp.MSCloudMusic.StartOrPause"
        val LAST_SONG = "com.example.hp.MSCloudMusic.LAST_SONG"
        val NEXT_SONG = "com.example.hp.MSCloudMusic.NEXT_SONG"
    }

    /**
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("Broadcast: ","receive broadcast!")
        var service: PlayService = BaseAppHelper.get().playService ?: return
        when(intent?.action){
            START_OR_PAUSE -> {
                service.playPause()
            }
            LAST_SONG -> {
                service.prev()
            }
            NEXT_SONG -> {
                service.next()
            }
            else -> {

            }
        }

    }
}