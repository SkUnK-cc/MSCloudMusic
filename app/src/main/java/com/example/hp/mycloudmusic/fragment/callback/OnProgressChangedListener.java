package com.example.hp.mycloudmusic.fragment.callback;

import android.text.SpannableStringBuilder;

public interface OnProgressChangedListener {
    void onProgressChanged(SpannableStringBuilder builder, int position, boolean refresh);
}
