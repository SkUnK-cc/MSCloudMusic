package com.example.hp.mycloudmusic.fragment.instance;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.musicInfo.merge.Album_info;


public class MergeAlbumFragment extends Fragment {

    private Album_info albumInfo;

    private OnFragmentInteractionListener mListener;


    public MergeAlbumFragment() {
        // Required empty public constructor
    }


    public static MergeAlbumFragment newInstance() {
        MergeAlbumFragment fragment = new MergeAlbumFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_merge_album, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setData(Album_info album_info) {
        this.albumInfo = album_info;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
