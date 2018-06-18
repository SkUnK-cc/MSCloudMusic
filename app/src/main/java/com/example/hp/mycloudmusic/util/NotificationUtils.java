package com.example.hp.mycloudmusic.util;

import android.app.NotificationManager;
import android.content.Context;

import com.example.hp.mycloudmusic.service.PlayService;

public class NotificationUtils {

    private static NotificationManager manager;
    private static PlayService playService;

    public static void init(PlayService playService){
        NotificationUtils.playService = playService;
        manager = (NotificationManager) playService.getSystemService(Context.NOTIFICATION_SERVICE);
    }

}
