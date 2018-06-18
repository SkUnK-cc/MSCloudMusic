package com.example.hp.mycloudmusic.service.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.example.hp.mycloudmusic.service.PlayService;

public class NoisyAudioStreamReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
                PlayService.startCommand(context,PlayService.TYPE_START_PAUSE);
                break;
            default:
                break;
        }
    }
}
