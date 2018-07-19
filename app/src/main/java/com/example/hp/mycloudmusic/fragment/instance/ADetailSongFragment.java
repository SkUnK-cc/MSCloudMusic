package com.example.hp.mycloudmusic.fragment.instance;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.musicInfo.merge.Song;
import com.example.hp.mycloudmusic.musicInfo.merge.Song_info;
import com.example.hp.mycloudmusic.ui.onLine.ArtistInfoActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

@SuppressLint("ValidFragment")
public class ADetailSongFragment extends BaseFragment {

//    @Bind(R.id.ll_detail_song)
//    LinearLayout llSong;
    @Bind(R.id.lv_song)
    ListView lvSong;
    private int height=0;

    private ArtistInfoActivity activity;

    @SuppressLint("ValidFragment")
    public ADetailSongFragment(ArtistInfoActivity activity){
        this.activity = activity;
    }


    public static ADetailSongFragment newInstance(ArtistInfoActivity artistInfoActivity) {
        ADetailSongFragment fragment = new ADetailSongFragment(artistInfoActivity);
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
        List<String> list = new ArrayList<>();
        for(int i=0;i<30;i++){
            list.add("text"+i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,list);
        lvSong.setAdapter(adapter);

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_adetail_song;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setData(Song_info song_info) {
        List<Song> list  = song_info.getSong_list();
        int count = list.size();
        height = count * 165;
        activity.setCheck(0);
    }

    public int getHeight(){
        return height;
    }
}
