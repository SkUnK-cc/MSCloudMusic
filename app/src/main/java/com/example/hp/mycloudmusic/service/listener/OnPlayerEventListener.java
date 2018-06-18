package com.example.hp.mycloudmusic.service.listener;

import com.example.hp.mycloudmusic.musicInfo.AudioBean;

public interface OnPlayerEventListener {
    void onUpdateProgress(int currentPosition);

    void onPlayerStart();

    void onBufferingUpdate(int percent);

    void onChange(AudioBean mPlayingMusic);
}
