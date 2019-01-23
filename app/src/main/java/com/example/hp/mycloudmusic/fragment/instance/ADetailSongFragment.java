package com.example.hp.mycloudmusic.fragment.instance;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.adapter.recyclerview.CommonAdapter;
import com.example.hp.mycloudmusic.adapter.recyclerview.CommonOnItemClickListener;
import com.example.hp.mycloudmusic.adapter.recyclerview.CommonViewHolder;
import com.example.hp.mycloudmusic.api.RetrofitFactory;
import com.example.hp.mycloudmusic.api.baidu.BaiduMusicApi;
import com.example.hp.mycloudmusic.custom.ArtistDetailScrollView;
import com.example.hp.mycloudmusic.custom.FullyLinearLayoutManager;
import com.example.hp.mycloudmusic.fragment.factory.FragmentFactory;
import com.example.hp.mycloudmusic.musicInfo.artistDetail.ArtistSongListResp;
import com.example.hp.mycloudmusic.musicInfo.merge.Artist;
import com.example.hp.mycloudmusic.musicInfo.merge.Song;
import com.example.hp.mycloudmusic.ui.onLine.ArtistInfoActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("ValidFragment")
public class ADetailSongFragment extends BaseFragment {

//    @Bind(R.id.ll_detail_song)
//    LinearLayout llSong;
    @Bind(R.id.rv_detail_song)
    RecyclerView mRecyclerView;
    private int height=0;
    private List<Song> mDatas = new ArrayList<>();
    private CommonAdapter<Song> adapter;
    private Artist artist;
    private int mPageNum;

    private ArtistInfoActivity activity;
    private ArtistDetailScrollView parentScrollView;

    @Override
    protected int getContentView() {
        return R.layout.fragment_adetail_song;
    }

    @SuppressLint("ValidFragment")
    public ADetailSongFragment(){
    }

    public static ADetailSongFragment newInstance(Artist artist) {
        ADetailSongFragment fragment = new ADetailSongFragment();
        Bundle args = new Bundle();
        args.putParcelable(ArtistDetailFragment.ARTIST,artist);
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
        artist = getArguments().getParcelable(ArtistDetailFragment.ARTIST);
        getSongListFromNet();
    }

    private void getSongListFromNet() {
        RetrofitFactory.provideBaiduApi()
                .getArtistSongList(artist.ting_uid,artist.artist_id,mPageNum * BaiduMusicApi.pagenSize,BaiduMusicApi.pagenSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtistSongListResp>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArtistSongListResp resp) {
                        if(resp!= null && resp.isValid()){
                            mPageNum++;
                            mDatas.addAll(resp.getSonglist());
                            adapter.notifyDataSetChanged();
                        }else{
                            Log.e("songfragment", "onNext: resp is null!!!");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new FullyLinearLayoutManager(getContext()));
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mRecyclerView.setLayoutManager(new FullyLinearLayoutManager0(getContext()));
        adapter = new CommonAdapter<Song>(getActivity(), R.layout.merge_song_item,mDatas)
        {
            @Override
            public void convert(CommonViewHolder holder, Song song) {
                holder.setText(R.id.merge_song_title,song.getTitle());
                holder.setText(R.id.merge_song_artist,song.getArtist());
            }
        };
        adapter.setOnItemClickListener(new CommonOnItemClickListener<Song>(){
            @Override
            public void onItemClick(ViewGroup parent, View view, Song song, int position) {
                showPlayMusicFragment(song);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Song song, int position) {
                return false;
            }
        });
        mRecyclerView.setAdapter(adapter);
//        mRecyclerView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.e("recyclerview", "50dp = "+ DisplayUtil.dip2px(getContext(),50));
//                Log.e("recyclerview", "recyclerview height = "+mRecyclerView.getHeight());
//            }
//        },2000);
    }

    private void showPlayMusicFragment(Song song) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        PlayMusicFragment playMusicFragment = FragmentFactory.getInstance(null).getmPlayMusicFragment();
        if(playMusicFragment != null && !playMusicFragment.isAdded()){
            transaction.add(android.R.id.content,playMusicFragment);
        }else if(playMusicFragment.isAdded()){
            transaction.show(playMusicFragment);
        }
        transaction.commit();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
