package com.example.hp.mycloudmusic.fragment.instance;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.fragment.callback.OnProgressChangedListener;
import com.example.hp.mycloudmusic.fragment.presenter.PlayMusicPresenter;
import com.example.hp.mycloudmusic.fragment.view.IPlayMusicView;
import com.example.hp.mycloudmusic.musicInfo.AbstractMusic;
import com.example.hp.mycloudmusic.service.listener.OnPlayerEventListener;
import com.example.hp.mycloudmusic.util.CoverLoader;
import com.example.hp.mycloudmusic.util.LyricManager;
import com.example.hp.mycloudmusic.util.PlayerFormatUtils;
import com.example.hp.mycloudmusic.view.LyricView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.Bind;

/**
 * fragment添加后，生命周期方法不会重复调用
 * 当activity不可见时，fragment会调用pause->stop-》start->resume
 */
public class PlayMusicFragment extends BaseFragment<PlayMusicPresenter> implements View.OnClickListener, OnProgressChangedListener ,IPlayMusicView {

    private static final String TAG = "playmusicfragment";

    @Bind(R.id.playing_back)
    ImageView iv_back;
    @Bind(R.id.bg_image)
    ImageView ivPlayPageBg;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_artist)
    TextView tvArtist;
    @Bind(R.id.iv_share)
    ImageView ivShare;
    @Bind(R.id.iv_playing_fav)
    ImageView ivPlayingFav;
    @Bind(R.id.iv_playing_down)
    ImageView ivPlayingDown;
    @Bind(R.id.iv_playing_cmt)
    ImageView ivPlayingCmt;
    @Bind(R.id.iv_playing_more)
    ImageView ivPlayingMore;
    @Bind(R.id.ll_music_tool)
    LinearLayout llMusicTool;
    @Bind(R.id.tv_current_time)
    TextView tvCurrentTime;
    @Bind(R.id.sb_progress)
    SeekBar sbProgress;
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    @Bind(R.id.iv_mode)
    ImageView ivMode;
    @Bind(R.id.iv_prev)
    ImageView ivPrev;
    @Bind(R.id.iv_play)
    ImageView ivPlay;
    @Bind(R.id.iv_next)
    ImageView ivNext;
    @Bind(R.id.iv_other)
    ImageView ivOther;
    @Bind(R.id.ll_content)
    LinearLayout llContent;
    @Bind(R.id.lyricview)
    LyricView lyricView;
    @Bind(R.id.empty)
    TextView emptyView;
    @Bind(R.id.sb_volume)
    SeekBar sbVolume;
    @Bind(R.id.iv_playing_velocity)
    ImageView ivPlayingVelocity;

    private int mLastProgress;
    private LyricManager lyricManager ;
    private int mLinePosition = -1;
    private AbstractMusic mPlayingMusic;

    @Override
    protected int getContentView() {
        return R.layout.playmusic_fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter = new PlayMusicPresenter(getActivity());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        getPlayService().setOnPlayerEventListener(new OnPlayerEventListener(){

            @Override
            public void onUpdateProgress(int currentPosition) {
                updateProgress(currentPosition);
            }

            @Override
            public void onPlayerStart() {

            }

            @Override
            public void onBufferingUpdate(int percent) {

            }

            @Override
            public void onChange(AbstractMusic music) {
                if(music != null){
                    onchange(music);
                }
            }

            @Override
            public void onPlayerPause() {
                ivPlay.setSelected(true);
            }
        });
    }

    @Override
    protected void initView() {
        ivPlay.setOnClickListener(this);
        lyricManager = new LyricManager();
        lyricManager.setOnProgressChangedListener(this);
        iv_back.setOnClickListener(this);
        mPlayingMusic = getPlayService().getPlayingMusic();
        onchange(mPlayingMusic);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.playing_back:
                onBackPressed();
                break;
            case R.id.iv_play:
                play();
                break;
            default:
                activityListener.onClick(v);
                break;
        }

    }

    private void play() {
        if(getPlayService() != null){
            getPlayService().playPause();
        }
    }

    public void onchange(AbstractMusic music) {
        Log.e(TAG, "onchange: 调用onchange方法1");
        if(music != null) {
            Log.e(TAG, "onchange: 调用onchange方法2");
            tvTitle.setText(music.getTitle());
            tvArtist.setText(music.getArtist());
            sbProgress.setProgress(getPlayService().getCurrentPosition());
            sbProgress.setSecondaryProgress(0);
            sbProgress.setMax((int) music.getDuration());         //最大值不显示,仍以毫秒为单位
            mLastProgress = 0;
            tvCurrentTime.setText("00:00");
            tvTotalTime.setText(PlayerFormatUtils.formatTime(music.getDuration()));
            ivPlayPageBg.setImageBitmap(CoverLoader.getInstance().loadBlur(music));
            lyricView.setEmptyView(emptyView);
            mPresenter.setLrc(music);
        }
    }

    @Override
    public void loadLrc(String lrcPath) {
        lyricView.hideEmptyView();
        InputStream is = null;
        try {
            is = new FileInputStream(new File(lrcPath));
            //027613
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            lyricManager.setFileStream(is);
        }
    }

    @Override
    public void showNotLrc() {
        lyricView.showEmptyView();
    }

    @Override
    public void onProgressChanged(SpannableStringBuilder builder, int position, boolean refresh) {
        Log.e(TAG, "onProgressChanged: position="+position+"refresh="+refresh);
        if(mLinePosition!=position || refresh){
            mLinePosition = position;
            lyricView.setText(builder);
            lyricView.setCurrentPosition(position);
        }
    }

    public void updateProgress(int currentPosition) {
        if(currentPosition>0){
            lyricManager.setCurrentTimeMillis(currentPosition);
        }
    }

    private void onBackPressed() {
        getActivity().onBackPressed();
        iv_back.setEnabled(false);      //避免频繁点击
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_back.setEnabled(true);
            }
        },300);
    }
}
