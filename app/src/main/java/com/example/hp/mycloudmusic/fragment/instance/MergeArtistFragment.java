package com.example.hp.mycloudmusic.fragment.instance;

import android.net.Uri;
import android.os.Bundle;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.musicInfo.merge.Artist_info;

public class MergeArtistFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    private Artist_info artistInfo;

    public MergeArtistFragment() {
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

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_merge_artist;
    }

    public void setData(Artist_info artist_info) {
        this.artistInfo = artist_info;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
