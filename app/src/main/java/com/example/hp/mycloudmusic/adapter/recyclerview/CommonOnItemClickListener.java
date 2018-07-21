package com.example.hp.mycloudmusic.adapter.recyclerview;

import android.view.View;
import android.view.ViewGroup;

public interface CommonOnItemClickListener<T> {
    void onItemClick(ViewGroup parent, View view, T t, int position);
    boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
}
