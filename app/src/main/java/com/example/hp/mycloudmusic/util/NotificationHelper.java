package com.example.hp.mycloudmusic.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.musicInfo.AbstractMusic;
import com.example.hp.mycloudmusic.musicInfo.merge.Song;
import com.example.hp.mycloudmusic.service.broadcast.NotificationBroadcast;

import static android.app.Notification.VISIBILITY_SECRET;

public class NotificationHelper {

    private NotificationManager manager;
    private Context context;
    private RemoteViews remoteViews;
    private NotificationCompat.Builder builder;

    public NotificationHelper(Context context){
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
        if(builder!=null){
            return builder;
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("channel_id","channel_name",NotificationManager.IMPORTANCE_LOW);
            channel.setLockscreenVisibility(VISIBILITY_SECRET);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        //设置状态栏上的小图标，否则会报错
        builder.setSmallIcon(R.drawable.ic_icon_quantum_statistical);
        this.builder = builder;
        return this.builder;
    }

    public Notification getPlayMusicNotification(AbstractMusic mPlayingMusic, String song, String singer, int progress, boolean isPlaying){
        NotificationCompat.Builder builder = getNotificationBuilder();

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_play_music);
        remoteViews.setTextViewText(R.id.notify_song, TextUtils.isEmpty(song)?"未知":song);
        remoteViews.setTextViewText(R.id.notify_singer,TextUtils.isEmpty(singer)?"未知":singer);
        remoteViews.setProgressBar(R.id.notify_progress_bar,100,progress,false);

        if(isPlaying) {
            remoteViews.setViewVisibility(R.id.notify_pause,View.VISIBLE);
            remoteViews.setViewVisibility(R.id.notify_start,View.GONE);
            Intent intent_pause = new Intent(NotificationBroadcast.Companion.getSTART_OR_PAUSE());
            PendingIntent pending_pause = PendingIntent.getBroadcast(context, 0, intent_pause, 0);
            remoteViews.setOnClickPendingIntent(R.id.notify_pause, pending_pause);
        }else {
            remoteViews.setViewVisibility(R.id.notify_start, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.notify_pause,View.GONE);
            Intent intent_play = new Intent(NotificationBroadcast.Companion.getSTART_OR_PAUSE());
            PendingIntent pending_play = PendingIntent.getBroadcast(context, 0, intent_play, 0);
            remoteViews.setOnClickPendingIntent(R.id.notify_start, pending_play);
        }

        Intent intent_last = new Intent(NotificationBroadcast.Companion.getLAST_SONG());
        PendingIntent pending_last = PendingIntent.getBroadcast(context,0,intent_last,0);
        remoteViews.setOnClickPendingIntent(R.id.notify_last_song,pending_last);

        Intent intent_next = new Intent(NotificationBroadcast.Companion.getNEXT_SONG());
        PendingIntent pending_next = PendingIntent.getBroadcast(context,0,intent_next,0);
        remoteViews.setOnClickPendingIntent(R.id.notify_next_song,pending_next);

        builder.setCustomContentView(remoteViews);
        builder.setCustomBigContentView(remoteViews);

        Notification notification = builder.build();
        if(mPlayingMusic instanceof Song){
            Song music = (Song) mPlayingMusic;
            NotificationTarget notificationTarget = new NotificationTarget(
                    context,remoteViews,R.id.notify_cover,notification,1);
            Glide.with(context)
                    .load(music.pic_small)
                    .asBitmap()
                    .placeholder(R.drawable.default_cover)
                    .error(R.drawable.default_cover)
                    .into(notificationTarget);
        }
        return notification;
    }

    public void updateNotification(AbstractMusic mPlayingMusic, String song, String singer, int progress, boolean isPlaying){
        Notification newNotification = getPlayMusicNotification(mPlayingMusic, song,singer,progress,isPlaying);
        manager.notify(1,newNotification);
    }

    public void cancelNotification(){

    }

}
