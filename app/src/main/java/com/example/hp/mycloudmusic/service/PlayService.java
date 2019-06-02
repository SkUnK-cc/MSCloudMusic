package com.example.hp.mycloudmusic.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.hp.mycloudmusic.api.RetrofitFactory;
import com.example.hp.mycloudmusic.musicInfo.AbstractMusic;
import com.example.hp.mycloudmusic.musicInfo.AudioBean;
import com.example.hp.mycloudmusic.musicInfo.merge.Song;
import com.example.hp.mycloudmusic.musicInfo.songPlay.SongPlayResp;
import com.example.hp.mycloudmusic.service.broadcast.NotificationBroadcast;
import com.example.hp.mycloudmusic.service.listener.OnPlayerEventListener;
import com.example.hp.mycloudmusic.service.receiver.NoisyAudioStreamReceiver;
import com.example.hp.mycloudmusic.util.AudioFocusManager;
import com.example.hp.mycloudmusic.util.NotificationHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PlayService extends Service {
    public static final String TAG = "PlayService";
    private boolean mReceiverTag = false;

    public int mPlayState = STATE_IDLE;
    private WifiManager.WifiLock wifiLock;

    private NoisyAudioStreamReceiver mNoisyReceiver = new NoisyAudioStreamReceiver();
    private IntentFilter filter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    /**
     * 方法名
     */
    public static final String ON_UPDATE_PROGRESS = "onUpdateProgress";
    public static final String ON_PLAYER_START = "onPlayerStart";
    public static final String ON_BUFFERING_UPDATE = "onBufferingUpdate";
    public static final String ON_CHANGE = "onChange";
    public static final String ON_PLAYER_PAUSE = "onPlayerPause";
    /**
     * 播放状态
     */
    public static final int STATE_IDLE = 100;
    public static final int STATE_PREPARING = 101;
    public static final int STATE_PLAYING = 102;
    public static final int STATE_PAUSE = 103;
    /**
     * 点击按钮类型
     */
    public static final String TYPE_PRE = "TYPE_PRE";
    public static final String TYPE_NEXT = "TYPE_NEXT";
    public static final String TYPE_START_PAUSE = "TYPE_START_PAUSE";

    public static final int UPDATE_PLAY_PROGRESS_SHOW = 0;

    private final Handler handler = new MyServiceHandler(this);

    /**
     * Notification
     */
    private NotificationHelper notificationHelper;
    Notification notification;

    public AbstractMusic getPlayingMusic() {
        return mPlayingMusic;
    }

    //将MyServiceHandler声明为静态类(不持有对外部类的隐式引用)，防止内存泄漏
    private static class MyServiceHandler extends Handler{

        private final WeakReference<PlayService> mPlayService;
        public MyServiceHandler(PlayService playService){
            mPlayService = new WeakReference<PlayService>(playService);
        }

        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_PLAY_PROGRESS_SHOW:
                    mPlayService.get().updatePlayProgressShow();    //先调用get()得到引用
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * 正在播放的歌曲列表
     */
    private ArrayList<AbstractMusic> audioMusics;

    /**
     * 正在播放的歌曲序号
     */
    private int mPlayingPosition = -1;


    /**
     * 正在播放的歌曲
     */
    private AbstractMusic mPlayingMusic;
    /**
     * 播放器
     */
    private OnPlayerEventListener mPlayerEventListener;
    private MediaPlayer mPlayer;
    private AudioFocusManager mAudioFocusManager;
    private HashMap<String,OnPlayerEventListener> listenerMap = new HashMap<>();
    private PlayBinder mBinder = new PlayBinder();

    /**
     * Notification广播接受者
     */
    private NotificationBroadcast broadcast;
    public void setOnPlayerEventListener(String className,OnPlayerEventListener listener) {
//        mPlayerEventListener = playerEventListener;
        if(listener!=null) {
            Log.e(TAG, "setOnPlayerEventListener: "+className);
            listenerMap.put(className, listener);
            listener.onChange(mPlayingMusic);
        }
    }

    public void detachOnPlayerEventListener(String className){
        Log.e(TAG, "detachOnPlayerEventListener: "+className);
        listenerMap.remove(className);
    }

    /**
     * 显示意图
     * 耳机，蓝牙断开
     * @param context
     * @param type
     */
    public static void startCommand(Context context, String type) {
        Intent intent = new Intent(context,PlayService.class);
        intent.setAction(type);
        context.startService(intent);
    }

    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    public class PlayBinder extends Binder{

        public PlayService getPlayService(){
            return PlayService.this;
        }


    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: service is on create!");
        audioMusics = new ArrayList<>();
        acquireWifi();
        registerReceiver();
        setNotification();
        createMediaPlayer();
        initAudioFocusManager();
    }

    private void acquireWifi() {
        wifiLock= ((WifiManager)getApplicationContext().getSystemService(this.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL,"mylock");
        wifiLock.acquire();
    }

    private void registerReceiver() {
        broadcast = new NotificationBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NotificationBroadcast.Companion.getSTART_OR_PAUSE());
        registerReceiver(broadcast,intentFilter);
        Log.e(TAG, "registerReceiver: true");
    }

    private void setNotification(){
        notificationHelper = new NotificationHelper(this);       //初始化NotificationUtils
        String song="",singer="";
        int progress=0;
        if(mPlayingMusic!=null){
            song = mPlayingMusic.getTitle();
            singer = mPlayingMusic.getAlbumPic();
//            progress = (int)(((float)mPlayer.getCurrentPosition() / (float)mPlayer.getDuration())*100);
            progress = 0;
        }
        notification = notificationHelper.getPlayMusicNotification(mPlayingMusic,song,singer,progress,isPlaying());
        startForeground(1,notification);
    }

    private void initAudioFocusManager() {
        mAudioFocusManager = new AudioFocusManager(this);
    }

    private void createMediaPlayer() {
        //先判断是否为空
        if(mPlayer == null){
            mPlayer = new MediaPlayer();
            mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null && intent.getAction()!=null){
            switch (intent.getAction()){
                case TYPE_START_PAUSE:
                    playPause();
                    break;
                default:
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public boolean checkPlayingChange(AbstractMusic music){
        if(mPlayingMusic == null){
            return true;
        }
        if(!music.getType().equals(mPlayingMusic.getType())){
            return true;
        }
        if(mPlayingMusic.getType().equals(AbstractMusic.TYPE_LOCAL)){
            if(((AudioBean)music).getId() != ((AudioBean)mPlayingMusic).getId()){
                return true;
            }
        }else{
            if(((Song)music).getSong_id() != ((Song)mPlayingMusic).getSong_id()){
                return true;
            }
        }
        return false;
    }

    public void playPause() {
        if(isPreparing()){
            stop();
        }else if(isPlaying()){
            pause();
        }else if(isPausing()){
            start();
        }else{
            if(getPlayingPosition()==-1)return;
            play(getPlayingPosition());
        }
    }

    private int getPlayingPosition() {
        return mPlayingPosition;
    }


    public void play(List<AbstractMusic> musicList, int position) {
        Log.e(TAG, "play: 开始设置mediaplayer");
        if(musicList==null || musicList.size()==0 || position<0){
            return;
        }
        //此处应分开写
        if(audioMusics == null){
            audioMusics = new ArrayList<>();
        }
        if(!audioMusics.isEmpty()){
            audioMusics.clear();
        }
        //为止
        audioMusics.addAll(musicList);
        mPlayingPosition = position;

        AbstractMusic music = audioMusics.get(mPlayingPosition);
        if((music.getType()).equals(AbstractMusic.TYPE_LOCAL)){
            play(music);
        }else if((music.getType()).equals(AbstractMusic.TYPE_ONLINE)){
            getSongInfoFromNet((Song) music);
        }
    }

    private boolean checkListChange(List<AbstractMusic> musicList) {
        if(musicList == audioMusics)return false;
        return true;
    }

    private void getSongInfoFromNet(final Song song) {
        RetrofitFactory.provideBaiduApi()
                .querySong(song.getSong_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SongPlayResp>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(SongPlayResp resp) {
                        if(resp != null && resp.isValid()){
                            song.bitrate = resp.getBitrate();
                            song.songInfo = resp.getSonginfo();
                            play(song);
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void play(int position) {
        if(audioMusics.size()==0)return;
        if(position < 0){
            //第一首音乐的上一首
            position = audioMusics.size() - 1;
        }else if(position >= audioMusics.size()){
            //最后一首音乐的下一首
            position = 0;
        }
        mPlayingPosition = position;
        AbstractMusic music = getMusicList().get(mPlayingPosition);
        if((music.getType()).equals(AbstractMusic.TYPE_LOCAL)){
            play(music);
        }else if((music.getType()).equals(AbstractMusic.TYPE_ONLINE)){
            getSongInfoFromNet((Song) music);
        }
    }

    private void play(AbstractMusic music) {
        mPlayingMusic = music;
        Log.e(TAG, "play: "+music.getTitle()+"/"+music.getDuration()+"/");
        createMediaPlayer();
        initPlayer(music);
        handler.removeMessages(UPDATE_PLAY_PROGRESS_SHOW);
        if(listenerMap!=null){
            List<OnPlayerEventListener> list = getListeners();
            for(int i=0;i<list.size();i++){
                list.get(i).onChange(music);
            }
        }
    }

    public void next() {
        handler.removeMessages(UPDATE_PLAY_PROGRESS_SHOW);
        if(mPlayingPosition == audioMusics.size()-1){
            play(0);
        }else{
            play(mPlayingPosition+1);
        }
    }

    public void prev(){
//        handler.removeMessages(UPDATE_PLAY_PROGRESS_SHOW);
        if(mPlayingPosition == 0){
            play(audioMusics.size()-1);
        }else{
            play(mPlayingPosition-1);
        }
    }

    private List<OnPlayerEventListener> getListeners(){
        List<OnPlayerEventListener> list = new ArrayList<>();
        if(listenerMap.size()!= 0){
            Iterator<Map.Entry<String,OnPlayerEventListener>> iterator = listenerMap.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String,OnPlayerEventListener> entry = iterator.next();
                list.add(entry.getValue());
            }
        }
        return list;
    }

    private void initPlayer(AbstractMusic music){
        try {
            mPlayer.reset();
            String dataSource = music.getDataSource().toString();
            Log.e(TAG, "initPlayer: dataSource= "+dataSource);
            mPlayer.setDataSource(getBaseContext(), music.getDataSource());
            mPlayer.prepareAsync();
            mPlayState = STATE_PREPARING;
            mPlayer.setOnPreparedListener(mOnPreparedListener);
            mPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            mPlayer.setOnCompletionListener(mOnCompletionListener);
            mPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
            mPlayer.setOnErrorListener(mOnErrorListener);
            mPlayer.setOnInfoListener(mOnInfoListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNextPlayMusic(AbstractMusic music) {
        if(music==null)return;
        audioMusics.add(mPlayingPosition+1,music);
    }

    public MediaPlayer getmPlayer() {
        return mPlayer;
    }

    /**-------------------------------mediaPlayer监听方法------------------------------------------*/
    /**
     * 流载入完成后调用start方法
     */
    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if(isPreparing()){
                start();
            }
        }
    };
    /**
     * 网络流缓冲更新
     */
    private MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if(listenerMap!=null){
                List<OnPlayerEventListener> list = getListeners();
                for(int i=0;i<list.size();i++){
                    list.get(i).onBufferingUpdate(percent);
                }
            }
        }
    };
    /**
     * 媒体资源播放完成后调用
     */
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.e(TAG, "onCompletion: 被调用");
            next();
        }
    };
    /**
     * seek操作完成后调用
     */
    private MediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {

        }
    };
    /**
     * 当异步操作期间发生错误时调用
     */
    private MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.e(TAG, "onError: media player occured error : "+extra);
            return false;
        }
    };
    /**
     * 传递媒体或其播放的信息或警告
     */
    private MediaPlayer.OnInfoListener mOnInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            return false;
        }
    };


    /**-----------------------------------开始播放，暂停，停止--------------------------------**/
    private void start() {
        Log.e(TAG, "start: 开始播放");
        if(!isPreparing() && !isPausing()){
            return;
        }
        if(mPlayingMusic == null){
            return;
        }
        //获取焦点
        if(mAudioFocusManager.requestAudioFucos()){
            if(mPlayer != null){
                mPlayer.start();
                mPlayState = STATE_PLAYING;
                //开始循环发送消息，更新进度条
                handler.sendEmptyMessage(UPDATE_PLAY_PROGRESS_SHOW);
//                if(mPlayerEventListener != null){
//                    mPlayerEventListener.onPlayerStart();
//                }
                if(listenerMap!=null){
                    List<OnPlayerEventListener> list = getListeners();
                    for(int i=0;i<list.size();i++){
                        list.get(i).onPlayerStart();
                    }
                }
                //注册监听耳机/蓝牙,避免多次注册，且之后在pause中注销
                if(!mReceiverTag){
                    mReceiverTag = true;
                    registerReceiver(mNoisyReceiver,filter);
                }
            }
        }
    }

    private void pause() {
        if(mPlayingMusic == null){
            return;
        }
        if(mPlayer != null){
            mPlayer.pause();
            mPlayState = STATE_PAUSE;
            handler.removeMessages(UPDATE_PLAY_PROGRESS_SHOW);
//            if(mPlayerEventListener != null){
//                mPlayerEventListener.onPlayerPause();
//            }
            if(listenerMap!=null){
                List<OnPlayerEventListener> list = getListeners();
                for(int i=0;i<list.size();i++){
                    list.get(i).onPlayerPause();
                }
            }
            if(mReceiverTag){
                mReceiverTag = false;
                unregisterReceiver(mNoisyReceiver);
            }
            updateNotification(mPlayer.getCurrentPosition(),mPlayer.getDuration());
        }
    }

    private void stop() {
        if(isDefault()){
            return;
        }
        pause();
        if(mPlayer != null){
            mPlayer.reset();
            mPlayState = STATE_IDLE;
        }
    }


    /**-------------------------------------------------------------------------------------------*/
    /**
     * 更新进度条和时间
     */
    private void updatePlayProgressShow() {
        int currentPosition = 0;
        int duration = 0;
        if(isPlaying() && listenerMap!=null){
            //Log.e(TAG, "updatePlayProgressShow: getDuration()");
            currentPosition= mPlayer.getCurrentPosition();
            duration = mPlayer.getDuration();
            //Log.d(TAG, "PlayService 调用activity onUpdateProgress方法 ");
            if(listenerMap!=null){
                List<OnPlayerEventListener> list = getListeners();
                for(int i=0;i<list.size();i++){
                    list.get(i).onUpdateProgress(currentPosition, duration);
                }
            }
        }
        //Log.d(TAG, "updatePlayProgressShow" );
        updateNotification(currentPosition,duration);
        handler.sendEmptyMessageDelayed(UPDATE_PLAY_PROGRESS_SHOW,1000);
    }

    private void updateNotification(int current,int duration) {
        if(notificationHelper==null){
            setNotification();
        } else {
            String song = "", singer = "";
            int progress = 0;
            if (mPlayingMusic != null) {
                song = mPlayingMusic.getTitle();
                singer = mPlayingMusic.getArtist();
                progress = (int)(((float)current / (float)duration)*100);
//                Log.e(TAG, mPlayer.getCurrentPosition()+"/"+mPlayer.getDuration());
//                Log.e(TAG, progress+"");
            }
            notificationHelper.updateNotification(mPlayingMusic,song, singer, progress,isPlaying());
        }
    }

    //默认状态应该是原始状态，STATE_IDLE
    private boolean isDefault() {
        return mPlayState == STATE_IDLE;
    }

    public boolean isPausing() {
        return mPlayState == STATE_PAUSE;
    }

    public boolean isPlaying(){
        return mPlayState == STATE_PLAYING;
    }

    private boolean isPreparing() {
        return mPlayState == STATE_PREPARING;
    }

    public void setMusicList(List<AudioBean> list) {
        audioMusics.clear();
        audioMusics.addAll(list);
    }

    public List<AbstractMusic> getMusicList(){
        return audioMusics;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: service");
//        handler.removeMessages(UPDATE_PLAY_PROGRESS_SHOW);
        handler.removeCallbacksAndMessages(null);
        releaseResource();
        releaseBroadcast();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(true);
        }
        super.onDestroy();
    }

    private void releaseBroadcast() {
        if(broadcast!=null){
            synchronized (broadcast) {
                if (broadcast != null) {
                    unregisterReceiver(broadcast);
                    broadcast=null;
                } else {
                    Log.e(TAG, "broadcast is null");
                }
            }
        }
    }

    /**
     * 在解绑的时候释放MediaPlayer
     */
    @Override
    public boolean onUnbind(Intent intent) {
//        handler.removeMessages(UPDATE_PLAY_PROGRESS_SHOW);
        handler.removeCallbacksAndMessages(null);
        releaseResource();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(true);
        }
        return super.onUnbind(intent);
    }

    private void releaseResource(){
        if(mPlayer!=null){
            mPlayer.release();
            Log.e(TAG, "releaseResource: player is release");
        }

        if(wifiLock.isHeld()){
            wifiLock.release();
        }
    }
}
