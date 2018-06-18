package com.example.hp.mycloudmusic.util;

import android.content.Context;
import android.media.AudioManager;

import com.example.hp.mycloudmusic.service.PlayService;

public class AudioFocusManager implements AudioManager.OnAudioFocusChangeListener {

    private PlayService mPlayService;
    private AudioManager mAudioManager;


    public AudioFocusManager(PlayService context){
        mPlayService = context;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public boolean requestAudioFucos() {
        return mAudioManager.requestAudioFocus(this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN)
                == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }
}
