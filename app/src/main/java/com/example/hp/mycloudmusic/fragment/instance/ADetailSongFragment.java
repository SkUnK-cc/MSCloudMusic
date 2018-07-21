package com.example.hp.mycloudmusic.fragment.instance;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.adapter.recyclerview.CommonAdapter;
import com.example.hp.mycloudmusic.adapter.recyclerview.CommonViewHolder;
import com.example.hp.mycloudmusic.custom.ArtistDetailScrollView;
import com.example.hp.mycloudmusic.ui.onLine.ArtistInfoActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

@SuppressLint("ValidFragment")
public class ADetailSongFragment extends BaseFragment {

//    @Bind(R.id.ll_detail_song)
//    LinearLayout llSong;
    @Bind(R.id.rv_detail_song)
    RecyclerView mRecyclerView;
    private int height=0;
    private List<String> mDatas = new ArrayList<>();

    private ArtistInfoActivity activity;
    private ArtistDetailScrollView parentScrollView;

    @SuppressLint("ValidFragment")
    public ADetailSongFragment(){
    }

    public static ADetailSongFragment newInstance() {
        ADetailSongFragment fragment = new ADetailSongFragment();
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
//        List<String> list = new ArrayList<>();
//        for(int i=0;i<25;i++){
//            list.add("text"+i);
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,list);
//        rvList.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        for (int i = 0; i < 50; i++)
        {
            mDatas.add("mtitle" + " -> " + i);
        }
        mRecyclerView.setAdapter(new CommonAdapter<String>(getActivity(), R.layout.item, mDatas)
        {
            @Override
            public void convert(CommonViewHolder holder, String s) {
                holder.setText(R.id.id_info,s);
            }
        });
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

//    public void setData(Song_info song_info) {
//        List<Song> list  = song_info.getSong_list();
//        int count = list.size();
//        height = count * 165;
//        activity.setCheck(0);
//    }

}
