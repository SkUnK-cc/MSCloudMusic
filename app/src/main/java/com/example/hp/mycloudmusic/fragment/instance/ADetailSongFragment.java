package com.example.hp.mycloudmusic.fragment.instance;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.adapter.recyclerview.CommonAdapter;
import com.example.hp.mycloudmusic.adapter.recyclerview.CommonViewHolder;
import com.example.hp.mycloudmusic.custom.ArtistDetailScrollView;
import com.example.hp.mycloudmusic.custom.FullyLinearLayoutManager;
import com.example.hp.mycloudmusic.ui.onLine.ArtistInfoActivity;
import com.example.hp.mycloudmusic.util.DisplayUtil;

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
        mRecyclerView.setLayoutManager(new FullyLinearLayoutManager(getContext()));
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mRecyclerView.setLayoutManager(new FullyLinearLayoutManager0(getContext()));
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
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("recyclerview", "50dp = "+ DisplayUtil.dip2px(getContext(),50));
                Log.e("recyclerview", "recyclerview height = "+mRecyclerView.getHeight());
            }
        },2000);
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
