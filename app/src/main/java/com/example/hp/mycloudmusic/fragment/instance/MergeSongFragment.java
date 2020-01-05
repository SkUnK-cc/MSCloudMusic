package com.example.hp.mycloudmusic.fragment.instance;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.adapter.merge.MSongRecyclerAdapter;
import com.example.hp.mycloudmusic.base.BaseAppHelper;
import com.example.hp.mycloudmusic.fragment.factory.FragmentFactory;
import com.example.hp.mycloudmusic.musicInfo.AbstractMusic;
import com.example.hp.mycloudmusic.musicInfo.merge.Song;
import com.example.hp.mycloudmusic.musicInfo.merge.Song_info;
import com.example.hp.mycloudmusic.service.PlayService;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MergeSongFragment extends BaseFragment {
    public static final String TAG = "MergeSongFragment";

    @Bind(R.id.merge_song_recyclerview)
    RecyclerView rvSong;
    @Bind(R.id.tv_noMore)
    TextView tvNoMore;

    private Song_info songInfo;
    private MSongRecyclerAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_merge_song;
    }

    public MergeSongFragment() {

    }

    public static MergeSongFragment newInstance() {
        MergeSongFragment fragment = new MergeSongFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        if(songInfo!=null){
            updateAdapter();
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {
        Log.e(TAG, "initView: init adapter");
        adapter = new MSongRecyclerAdapter(getContext());

        adapter.setOnClickListener(new MSongRecyclerAdapter.OnMergeSongClickListener(){
            @Override
            public void onClickMenuItem(int itemId, Song song) {
                switch(itemId){
                    case R.id.merge_song_pop_add:
                        Toast.makeText(getContext(),"you click add to songlist!",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.merge_song_pop_artist:
                        Toast.makeText(getContext(),"you click artist!",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onClickItem(int position) {
                PlayService playService = BaseAppHelper.get().getPlayService();
                if(!playService.isSameSongToCurrent(adapter.getData().get(position))){
                    List<AbstractMusic> list = new ArrayList<>();
                    list.addAll(adapter.getData());
                    playService.play(list,position);
                    showPlayMusicFragment();
                }
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvSong.setLayoutManager(mLayoutManager);
        rvSong.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        rvSong.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setData(Song_info song_info) {
        if(song_info!=null) {
            this.songInfo = song_info;
            updateAdapter();
        }
    }

    private void updateAdapter() {
        List<Song> data = songInfo.getSong_list();
        if(adapter == null){
            Log.e(TAG, "updateAdapter: adapter is null");
        }

        adapter.updateData(data);
        adapter.notifyDataSetChanged();
        if (data == null){
            tvNoMore.setVisibility(View.VISIBLE);
        }else if(data.size()==0){
            tvNoMore.setVisibility(View.VISIBLE);
        }else{
            tvNoMore.setVisibility(View.GONE);
        }
    }

    private void showPlayMusicFragment() {
        PlayMusicFragment playMusicFragment = FragmentFactory.getInstance(null).getmPlayMusicFragment();
        addOrShowFragmentOnActivity(android.R.id.content,playMusicFragment,R.anim.fragment_slide_from_right);
    }
}
