package com.example.hp.mycloudmusic.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.service.broadcast.NotificationBroadcast;

import static android.app.Notification.VISIBILITY_SECRET;

public class NotificationUtils {

    private NotificationManager manager;
    private Context context;

    public NotificationUtils(Context context){
        this.context = context;
        init();
    }

    public void init(){
        if(manager==null) {
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }

    public void create(){
        //NotificationCompat.Builder builder = new NotificationCompat.Builder();
    }

    private NotificationCompat.Builder getNotificationBuilder(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("channel_id","channel_name",NotificationManager.IMPORTANCE_LOW);
            channel.setLockscreenVisibility(VISIBILITY_SECRET);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        builder.setContentTitle("新消息来了");
//        builder.setContentText("内容");
        builder.setSmallIcon(R.drawable.ic_icon_add_list);
        return builder;
    }

    public Notification getPlayMusicNotification(){
        NotificationCompat.Builder builder = getNotificationBuilder();

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_play_music);

        Intent intent = new Intent(NotificationBroadcast.Companion.getSTART_OR_PAUSE());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
        remoteViews.setOnClickPendingIntent(R.id.notify_pause,pendingIntent);

        builder.setCustomContentView(remoteViews);
        builder.setCustomBigContentView(remoteViews);

//        manager.notify(3,builder.build());
        return builder.build();
    }

    public void cancelNotification(){

    }

}
