package com.example.hp.mycloudmusic.musicInfo;

import android.os.Parcelable;

import java.io.Serializable;

public abstract class AbstractMusic implements Serializable,Parcelable,Parcelable.Creator<AbstractMusic> {
    public static Creator<AbstractMusic> CREATOR;

    public AbstractMusic(){
        CREATOR = this;
    }
}
