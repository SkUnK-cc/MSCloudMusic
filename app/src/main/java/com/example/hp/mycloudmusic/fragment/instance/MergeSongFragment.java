package com.example.hp.mycloudmusic.fragment.instance;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.adapter.merge.MSongRecyclerAdapter;
import com.example.hp.mycloudmusic.musicInfo.merge.Song;
import com.example.hp.mycloudmusic.musicInfo.merge.Song_info;

import java.util.List;

import butterknife.Bind;

public class MergeSongFragment extends BaseFragment {
    public static final String TAG = "MergeSongFragment";

    @Bind(R.id.merge_song_recyclerview)
    RecyclerView rvSong;

    private Song_info songInfo;
    private MSongRecyclerAdapter adapter;

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
        if (getArguments() != null) {

        }
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
        adapter = new MSongRecyclerAdapter(getContext());
        adapter.setOnClickListener(new MSongRecyclerAdapter.IClickMSPopupMenuItem(){
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
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvSong.setLayoutManager(mLayoutManager);
        rvSong.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        rvSong.setAdapter(adapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_merge_song;
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
    }

}
