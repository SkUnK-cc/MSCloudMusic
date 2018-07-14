package com.example.hp.mycloudmusic.fragment.instance;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.adapter.LocalMusicAdapter;
import com.example.hp.mycloudmusic.base.BaseAppHelper;
import com.example.hp.mycloudmusic.musicInfo.AudioBean;
import com.example.hp.mycloudmusic.util.FileScanManager;

import java.util.List;

import butterknife.Bind;

public class LocalFragment extends BaseFragment implements View.OnClickListener {

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

    private static final String TAG = "LocalFragment";

//    @Override
//    protected void setupActivityComponent(AppComponent appComponent) {
//        DaggerActivityComponent.builder()
//                .appComponent(appComponent)
//                .build()
//                .inject(this);
//    }

    @Override
    protected void initData() {
        checkLocalMusic();
    }

    private void checkLocalMusic() {
        if(localMusicList == null || localMusicList.size()==0){
            loadTip.setVisibility(View.VISIBLE);
            startScan();
        }
    }

    private void startScan() {
        new AsyncTask<Void, String, List<AudioBean>>() {
            @Override
            protected List<AudioBean> doInBackground(Void... voids) {
                //先同步到localMusicList,再更新到adapter
                localMusicList = FileScanManager.getInstance(liteOrm).getAudioFromDb();
                Log.e(TAG, "doInBackground: 从数据库获取音乐");
                if(localMusicList == null || localMusicList.size()==0){
                    Log.e(TAG, "doInBackground: 从本地扫描获取音乐");
                    localMusicList = FileScanManager.getInstance(liteOrm).scanMusic(getContext());
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
            }
        }.execute();
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
        adapter.setOnItemClickListener(activityListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mrecyclerView.setLayoutManager(layoutManager);
        mrecyclerView.setAdapter(adapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.localmusic_fragment;
    }


    @Override
    public void onClick(View v) {
        activityListener.onClick(v);
    }

}
