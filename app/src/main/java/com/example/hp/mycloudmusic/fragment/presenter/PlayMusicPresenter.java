package com.example.hp.mycloudmusic.fragment.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.example.hp.mycloudmusic.api.RetrofitFactory;
import com.example.hp.mycloudmusic.api.RxSchedulers;
import com.example.hp.mycloudmusic.fragment.view.IPlayMusicView;
import com.example.hp.mycloudmusic.musicInfo.AbstractMusic;
import com.example.hp.mycloudmusic.musicInfo.AudioBean;
import com.example.hp.mycloudmusic.musicInfo.lyric.Lrc;
import com.example.hp.mycloudmusic.musicInfo.sug.MusicSearchSugResp;
import com.example.hp.mycloudmusic.musicInfo.sug.SongSug;
import com.example.hp.mycloudmusic.mvp.presenter.BasePresenter;
import com.example.hp.mycloudmusic.util.FileMusicUtils;
import com.example.hp.mycloudmusic.util.LrcUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PlayMusicPresenter extends BasePresenter<IPlayMusicView> {
    public static final String TAG = "PlayMusicPresenter";
    private Activity activity;

    public PlayMusicPresenter(Activity activity){
        this.activity = activity;
    }

    public void setLrc(AbstractMusic music) {
        Log.e(TAG, "setLrc: 调用setLrc方法");
        Log.e(TAG, "setLrc: "+music==null?"music= null":"music!=null" );
        Log.e(TAG, "setLrc: "+music.getType());
        if(music.getType() == AbstractMusic.TYPE_LOCAL){       //此处字符串应该用equals，而不是==
            Log.e(TAG, "歌曲的类型是本地Local类型" );
            AudioBean audioMusic = (AudioBean) music;
            String lrcPath = FileMusicUtils.getLrcFilePath(audioMusic);
            Log.e(TAG, "lrcPath : "+lrcPath);
            if(lrcPath != null && !TextUtils.isEmpty(lrcPath)){
                //从本地(已下载或歌曲文件夹)
                Log.e(TAG, "setLrc: 找到歌词路径");
                mView.loadLrc(lrcPath);
            }else{
                getLrcFromNet(music);
            }
        }else{
            getLrcFromNet(music);
        }
    }

    private void getLrcFromNet(AbstractMusic music){
        RetrofitFactory.provideBaiduApi()
                .querySug(music.getTitle()+" "+music.getArtist())
                .compose(RxSchedulers.Companion.compose())
                .subscribe(new Observer<MusicSearchSugResp>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(MusicSearchSugResp musicSearchSugResp) {
                        Log.e(TAG, "onNext: 得到sug：");
                        if(musicSearchSugResp != null && musicSearchSugResp.isValid()){
                            Log.e(TAG, "onNext: errorcode= "+musicSearchSugResp.getError_code());
                            List<SongSug> songList = musicSearchSugResp.song;
                            findLrc(songList,0);
                        }else{
                            mView.showNotLrc();
                        }
                        Log.e(TAG, "onNext: "+(musicSearchSugResp.isValid()?"有效":"无效"));
                        Log.e(TAG, "onNext: " + musicSearchSugResp.error_code);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void findLrc(List<SongSug> songList, int index) {
        if(songList==null || songList.isEmpty()){
            return;
        }
        final SongSug songSug = songList.get(index);
        String songid = songSug.songid;
        Log.e(TAG, "findLrc: songSug:"+songid);
        RetrofitFactory.provideBaiduApi()
                .queryLrc(songid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Lrc>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final Lrc lrc) {
                        if(lrc != null && lrc.isValid()){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    List<String> lrcList = LrcUtil.parseLrcStr(lrc.getLrcContent());
                                    final File file = FileMusicUtils.createLrcFile(songSug);
                                    FileWriter writer = null;
                                    try {
                                        writer = new FileWriter(file);
                                        for(int i=0;i<lrcList.size();i++){
                                            writer.write(lrcList.get(i)+"\n");
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }finally {
                                        try {
                                            if(writer != null) {
                                                writer.close();
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mView.loadLrc(file.getPath());
                                        }
                                    });
                                }
                            }).start();
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
}
