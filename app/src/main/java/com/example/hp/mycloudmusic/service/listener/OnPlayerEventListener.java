package com.example.hp.mycloudmusic.service.listener;

import com.example.hp.mycloudmusic.musicInfo.AbstractMusic;

public interface OnPlayerEventListener {
    void onUpdateProgress(int currentPosition);

    void onPlayerStart();

    void onBufferingUpdate(int percent);

    void onChange(AbstractMusic mPlayingMusic);

    void onPlayerPause();
}
