package com.example.hp.mycloudmusic.fragment.instance;

import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.executor.SearchLyric;
import com.example.hp.mycloudmusic.fragment.callback.OnProgressChangedListener;
import com.example.hp.mycloudmusic.musicInfo.AudioBean;
import com.example.hp.mycloudmusic.util.CoverLoader;
import com.example.hp.mycloudmusic.util.FileMusicUtils;
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
public class PlayMusicFragment extends BaseFragment implements View.OnClickListener, OnProgressChangedListener {

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


//    @Override
//    protected void setupActivityComponent(AppComponent appComponent) {
//        DaggerActivityComponent.builder()
//                .appComponent(appComponent)
//                .build()
//                .inject(this);
//    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {
        lyricManager = new LyricManager();
        lyricManager.setOnProgressChangedListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.playmusic_fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.playing_back:
                onBackPressed();
                break;
            default:
                activityListener.onClick(v);
                break;
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

    public void onchange(AudioBean music) {
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
            setLrc(music);
        }
    }

    private void setLrc(AudioBean music) {
        Log.e(TAG, "setLrc: 调用setLrc方法");
        Log.e(TAG, "setLrc: "+music==null?"music= null":"music!=null" );
        Log.e(TAG, "setLrc: "+music.getType());
        lyricView.setEmptyView(emptyView);
        if(music.getType().equals(AudioBean.TYPE_LOCAL)){       //此处字符串应该用equals，而不是==
            Log.e(TAG, "歌曲的类型是本地Local类型" );
            String lrcPath = FileMusicUtils.getLrcFilePath(music);
            Log.e(TAG, "lrcPath : "+lrcPath);
            if(lrcPath!=null && !TextUtils.isEmpty(lrcPath)){
                //从本地(已下载或歌曲文件夹)
                Log.e(TAG, "setLrc: 无法从本地获取歌词");
                lyricView.setEmptyView(emptyView);
                loadLrc(lrcPath);
            }else{
                new SearchLyric(music.getTitle(),music.getArtist()){
                    @Override
                    public void getLrcSuccess(String filePath) {
                        //下载完成后,如果获取歌词失败或者为空，则不调用该方法
                        Log.e(TAG, "getLrcSuccess: 下载歌词完成后");
                        lyricView.setEmptyView(emptyView);
                        loadLrc(filePath);
                    }
                }.execute();
            }
        }
    }

    private void loadLrc(String lrcPath) {
        InputStream is = null;
        try {
            is = new FileInputStream(new File(lrcPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            lyricManager.setFileStream(is);
        }

    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.e(TAG, "onResume:------------------------------------------------- ");
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.e(TAG, "onPause: -------------------------------------------------");
//    }

    @Override
    public void onProgressChanged(SpannableStringBuilder builder, int position, boolean refresh) {
        Log.e(TAG, "onProgressChanged: position="+position+"refresh="+refresh);
        if(mLinePosition!=position || refresh){
            mLinePosition = position;
            lyricView.setText(builder);
            lyricView.setCurrentPosition(position);
        }
    }

    public void onUpdateProgress(int currentPosition) {
        if(currentPosition>0){
            lyricManager.setCurrentTimeMillis(currentPosition);
        }
    }
}
