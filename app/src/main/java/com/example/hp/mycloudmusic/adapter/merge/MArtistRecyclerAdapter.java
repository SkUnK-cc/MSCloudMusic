package com.example.hp.mycloudmusic.adapter.merge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.adapter.BaseRecyclerHolder;
import com.example.hp.mycloudmusic.musicInfo.merge.Artist;

import java.util.ArrayList;
import java.util.List;

public class MArtistRecyclerAdapter extends RecyclerView.Adapter<BaseRecyclerHolder> {

    private Context context;
    private List<Artist> mList;

    public MArtistRecyclerAdapter(Context context){
        this.context = context;
        mList = new ArrayList<>();
    }

    @NonNull
    @Override
    public BaseRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.merge_artist_item,parent,false);
        BaseRecyclerHolder viewHolder = BaseRecyclerHolder.getRecyclerHolder(context,view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
