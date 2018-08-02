package com.example.hp.mycloudmusic.adapter.recyclerview.search_fragment;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

public class NoScrollGridLayoutManager extends GridLayoutManager {
    private boolean isScrollEnable = true;

    public NoScrollGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public void setScrollEnable(boolean scrollEnable) {
        isScrollEnable = scrollEnable;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnable && super.canScrollVertically();
    }
}
