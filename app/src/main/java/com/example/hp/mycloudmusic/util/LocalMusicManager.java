package com.example.hp.mycloudmusic.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;

import com.example.hp.mycloudmusic.musicInfo.AudioBean;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.parseLong;

public class LocalMusicManager {

    private static final String TAG = "LocalMusicManager";

    private LiteOrm mLiteOrm;
    private static LocalMusicManager mInstance;
    //当没有明确的对象作为锁，只是想让一段代码同步时，可以创建一个特殊的对象来充当锁
    private static final Object mLock = new Object();
    private static final Object wLock = new Object();


    public static LocalMusicManager getInstance(LiteOrm liteOrm){
        if(mInstance == null){
            synchronized (mLock){
                if(mInstance==null) {
                    mInstance = new LocalMusicManager(liteOrm);
                }
            }
        }
        return mInstance;
    }

    private LocalMusicManager(LiteOrm liteOrm){
        this.mLiteOrm = liteOrm;
    }


    private static final String SELECTION = MediaStore.Audio.AudioColumns.SIZE+">=? AND "+
            MediaStore.Audio.AudioColumns.DURATION+">=?";

    public List<AudioBean> scanMusic(Context context) {
        List<AudioBean> musicList = new ArrayList<>();

        String mFilterSize = SpUtils.getLocalFilterSize();
        String mFilterTime = SpUtils.getLocalFilterTime();

        long filterSize = parseLong(mFilterSize)*1024;
        long filterTime = parseLong(mFilterTime)*1000;

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] {
                        BaseColumns._ID,
                        MediaStore.Audio.AudioColumns.IS_MUSIC,
                        MediaStore.Audio.AudioColumns.TITLE,
                        MediaStore.Audio.AudioColumns.ARTIST,
                        MediaStore.Audio.AudioColumns.ALBUM,
                        MediaStore.Audio.AudioColumns.ALBUM_ID,
                        MediaStore.Audio.AudioColumns.DATA,         //文件路径
                        MediaStore.Audio.AudioColumns.DISPLAY_NAME, //文件名称
                        MediaStore.Audio.AudioColumns.SIZE,
                        MediaStore.Audio.AudioColumns.DURATION
                },
                SELECTION,
                new String[]{String.valueOf(filterSize),String.valueOf(filterTime)},
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if(cursor == null){
            return musicList;
        }

        while(cursor.moveToNext()){
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.IS_MUSIC));
            if(isMusic==0){
                continue;
            }
            AudioBean music = new AudioBean();
            music.setLocal_id(cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)));
            music.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)));
            music.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST)));
            music.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM)));
            music.setAlbumIdLocal(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID)));
            music.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)));
            music.setFileName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME)));
            music.setFileSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.SIZE)));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));
            Log.e(TAG, "scanMusic: duration = "+duration);
            music.setDuration(duration);

            musicList.add(music);

            mLiteOrm.insert(music);
        }
        cursor.close();

        Log.e(TAG, "scanMusic------------------------------------------------------>");
        return musicList;
    }

    public List<AudioBean> getAudioFromDb() {
        List<AudioBean> musicList = new ArrayList<>();
        QueryBuilder<AudioBean> qb = new QueryBuilder<>(AudioBean.class);
        musicList = mLiteOrm.query(qb);
        Log.e(TAG, "getAudioFromDb------------------------------------------------->");
        return musicList;
    }

    public void deleteAudio(AudioBean audioBean,Context context){
        mLiteOrm.delete(audioBean);
        deleteFromLocal(audioBean,context);
    }

    private void deleteFromLocal(AudioBean audioBean,Context context){
        String path = audioBean.getPath();
        if(path!=null && !path.equals("")) {
            File file = new File(path);
            if (file.exists()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        file.delete();
                        Log.e(TAG, "run: delete file successful!");
                    }
                }).start();
            }
        }
    }
}
