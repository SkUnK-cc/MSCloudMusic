package com.example.hp.mycloudmusic.fragment.instance;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.adapter.merge.MArtistRecyclerAdapter;
import com.example.hp.mycloudmusic.musicInfo.merge.Artist;
import com.example.hp.mycloudmusic.musicInfo.merge.Artist_info;
import com.example.hp.mycloudmusic.ui.onLine.ArtistDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MergeArtistFragment extends BaseFragment {

    @Bind(R.id.merge_artist_recycler)
    RecyclerView recyclerView;
    private MArtistRecyclerAdapter adapter;

    private Artist_info artistInfo;
    private List<Artist> mList;

    public MergeArtistFragment() {
        mList = new ArrayList<>();
    }


    public static MergeArtistFragment newInstance() {
        MergeArtistFragment fragment = new MergeArtistFragment();
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
        updateData();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {
        adapter = new MArtistRecyclerAdapter(getContext());
        adapter.setOnClickListener(new MArtistRecyclerAdapter.IClickArtistListener() {
            @Override
            public void onClick(String ting_uid) {
                Toast.makeText(getContext(), "you click artist!", Toast.LENGTH_SHORT).show();
                ArtistDetailActivity.toArtistDetailActivity(getContext(),ting_uid);
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_merge_artist;
    }

    public void setData(Artist_info artist_info) {
        if(artist_info != null) {
            this.artistInfo = artist_info;
            mList = artist_info.getArtist_list();
            updateData();
        }
    }

    private void updateData() {
        if(mList.size() != 0){
            adapter.update(mList);
            adapter.notifyDataSetChanged();
        }
    }
}
