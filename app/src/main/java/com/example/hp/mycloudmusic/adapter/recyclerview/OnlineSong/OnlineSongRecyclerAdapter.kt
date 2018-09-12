package com.example.hp.mycloudmusic.adapter.recyclerview.OnlineSong

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.hp.mycloudmusic.R
import com.example.hp.mycloudmusic.adapter.recyclerview.CommonAdapter
import com.example.hp.mycloudmusic.adapter.recyclerview.CommonViewHolder
import com.example.hp.mycloudmusic.musicInfo.merge.Song

class OnlineSongRecyclerAdapter(context: Context, datas: List<Song>) : CommonAdapter<Song>(context, R.layout.merge_song_item, datas) {

    var mListener: OnlineSongClickListener? = null

    override fun setListener(parent: ViewGroup, viewHolder: CommonViewHolder, viewType: Int) {
        super.setListener(parent, viewHolder, viewType)
        if (!isEnabled(viewType)) return
        viewHolder.getView<View>(R.id.merge_song_info).setOnClickListener {
            mListener?.onInfoClick(getPosition(viewHolder))
        }
        viewHolder.getView<View>(R.id.merge_song_more).setOnClickListener {
            mListener?.onMoreClick(getPosition(viewHolder))
        }
    }

    override fun convert(holder: CommonViewHolder, song: Song) {
        holder.getView<TextView>(R.id.merge_song_title).text = song.title
        holder.getView<TextView>(R.id.merge_song_artist).text = song.artist
    }

    fun setOnlineSongClickListener(listener: OnlineSongClickListener){
        mListener = listener
    }

    interface OnlineSongClickListener {
        fun onMoreClick(position: Int)
        fun onInfoClick(position: Int)
    }
}