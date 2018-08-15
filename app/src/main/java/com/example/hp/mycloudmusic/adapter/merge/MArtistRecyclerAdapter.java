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
    private IClickArtistListener listener;

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
    public void onBindViewHolder(@NonNull BaseRecyclerHolder holder, final int position) {
        Artist artist = mList.get(position);
        holder.setText(R.id.merge_artist_name,artist.getName());
        holder.setImageByUrl(R.id.merge_artist_image,artist.getAvatar_middle());
        holder.getView(R.id.merge_artist_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onClick(mList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(List<Artist> list) {
        this.mList = list;
    }

    /**
     * 接口
     */
    public interface IClickArtistListener{
        void onClick(Artist artist);
    }

    public void setOnClickListener(IClickArtistListener listener) {
        this.listener = listener;
    }
}
