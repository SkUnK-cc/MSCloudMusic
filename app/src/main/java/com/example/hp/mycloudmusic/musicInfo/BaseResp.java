package com.example.hp.mycloudmusic.musicInfo;

public class BaseResp {
    public static final int ERROR_CODE_OK = 22000;
    public int error_code = 0;

    public boolean isValid(){
        return error_code==0 || error_code==ERROR_CODE_OK;
    }
}
