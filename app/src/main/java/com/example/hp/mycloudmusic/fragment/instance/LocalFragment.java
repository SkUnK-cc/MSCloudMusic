package com.example.hp.mycloudmusic.fragment.instance;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.adapter.LocalMusicAdapter;
import com.example.hp.mycloudmusic.base.BaseAppHelper;
import com.example.hp.mycloudmusic.custom.popupwindow.DefPopupWindowListener;
import com.example.hp.mycloudmusic.custom.popupwindow.PopupWindowManager;
import com.example.hp.mycloudmusic.fragment.factory.FragmentFactory;
import com.example.hp.mycloudmusic.musicInfo.AbstractMusic;
import com.example.hp.mycloudmusic.musicInfo.AudioBean;
import com.example.hp.mycloudmusic.util.DisplayUtil;
import com.example.hp.mycloudmusic.util.LocalMusicManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class LocalFragment extends BaseFragment implements View.OnClickListener, LocalMusicAdapter.ClickListener {

    @Bind(R.id.load_tip)
    TextView loadTip;
    @Bind(R.id.local_music_back)
    ImageView back;
    @Bind(R.id.local_recycler_view)
    RecyclerView mrecyclerView;
    @Bind(R.id.the_music_playing)
    ImageView iv_playing;


    private boolean prepare = true;

    private LocalMusicAdapter adapter;
    private List<AudioBean> localMusicList;

    private AsyncTask<Void, String, List<AudioBean>> mTask;

    private static final String TAG = "LocalFragment";

    class LocalPopupWindowListener extends DefPopupWindowListener {

        public LocalPopupWindowListener(@NotNull Activity activity) {
            super(activity);
        }

        @Override
        public void onItemClick(int imageId, AbstractMusic music) {
            switch (imageId){
                case R.drawable.ic_icon_delete:
                    removeLocalMusic(imageId,music);
                    break;
                default:
                    super.onItemClick(imageId,music);
                    break;
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.localmusic_fragment;
    }

    @Override
    protected void initData() {
        checkLocalMusic();
    }


    private void checkLocalMusic() {
        if(localMusicList == null || localMusicList.size()==0){
            loadTip.setVisibility(View.VISIBLE);
            startScan();
        }else{
            adapter.setData(localMusicList);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(TAG, "onHiddenChanged: "+hidden);
        if( !hidden && BaseAppHelper.get().getLocalMusicChanged()){
            startScan();
        }
    }

    private void startScan() {
        mTask = new AsyncTask<Void, String, List<AudioBean>>() {
            @Override
            protected List<AudioBean> doInBackground(Void... voids) {
                //先同步到localMusicList,再更新到adapter
                Log.e(TAG, "doInBackground: 从数据库获取音乐");
                localMusicList = LocalMusicManager.getInstance(liteOrm).getAudioFromDb();
                if(localMusicList == null || localMusicList.size()==0){
                    Log.e(TAG, "doInBackground: 从本地扫描获取音乐");
                    localMusicList = LocalMusicManager.getInstance(liteOrm).scanMusic(getContext());
                }
                return localMusicList;
            }
            //可以通过onProgress进行显示歌曲扫描进度

            @Override
            protected void onPostExecute(List<AudioBean> list) {
                if(list.size() == 0){
                    loadTip.setText("没有本地歌曲");
                }else{
                    loadTip.setVisibility(View.GONE);
                    adapter.setData(localMusicList);
                    BaseAppHelper.get().getMusicList().clear();
                    BaseAppHelper.get().getMusicList().addAll(list);
                }
                BaseAppHelper.get().setLocalMusicChanged(false);
            }

        };
        mTask.execute();
        if(liteOrm == null) {
            Log.e(TAG, "liteOrm is null");
        }else{
            Log.e(TAG, "liteOrm is not null");
        }
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initView() {
        back.setOnClickListener(this);
        iv_playing.setOnClickListener(this);
        //recyclerView
        adapter = new LocalMusicAdapter();
        adapter.setOnItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mrecyclerView.setLayoutManager(layoutManager);
        mrecyclerView.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.local_music_back:
                activityListener.onClick(v);
                break;
            case R.id.the_music_playing:
                Fragment playMusicFragment = FragmentFactory.getInstance(null).getmPlayMusicFragment();
                addOrShowFragmentOnActivity(android.R.id.content,playMusicFragment,R.anim.fragment_slide_from_right);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLocalItemClick(int pos) {
        Fragment playMusicFragment = FragmentFactory.getInstance(null).getmPlayMusicFragment();
        addOrShowFragmentOnActivity(android.R.id.content,playMusicFragment,R.anim.fragment_slide_from_right);
        if(BaseAppHelper.get().getPlayService().checkPlayingChange(localMusicList.get(pos))){
            Log.e(TAG, "onLocalItemClick: change");
            List<AbstractMusic> musicList = new ArrayList<>();
            musicList.addAll(localMusicList);
            BaseAppHelper.get().getPlayService().play(musicList,pos);
        }
    }

    @Override
    public void clickMore(int position) {
        AbstractMusic music = localMusicList.get(position);
        PopupWindowManager manager = new PopupWindowManager.Builder(getActivity(),R.layout.song_more_popup, ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(getContext(),300))
                .hasMv(false)
                .hasDelete(true)
                .setMusic(music)
                .setListener(new LocalPopupWindowListener(getActivity()))
                .build()
                .showAtLocation(back, Gravity.BOTTOM,0,0);
    }

    private void removeLocalMusic(int imageId, AbstractMusic music){
        int index = localMusicList.indexOf(music);

        LocalMusicManager.getInstance(liteOrm).deleteAudio(localMusicList.get(index),getContext());

        localMusicList.remove(index);
        adapter.notifyItemRemoved(index);

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTask.cancel(true);
    }
}
