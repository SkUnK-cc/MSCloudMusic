package com.example.hp.mycloudmusic.provider

import com.example.hp.mycloudmusic.util.DevUtil
import com.example.hp.mycloudmusic.util.FileMusicUtils
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.File

object BufferMusicProvider {
    const val TAG = "BufferMusicProvider"
    val map = mutableMapOf<String,String>()

    fun scanBufferMusic(){
        Observable.create<Unit>{
            val path = FileMusicUtils.getMusicDiskCacheDir()
            val dir = File(path)
            if(dir.exists() && dir.isDirectory){
                val fileList = dir.listFiles()
                for(item in fileList){
                    DevUtil.e(TAG,"add buffer file:${item.name}")
                    map[item.name] = item.absolutePath
                }
            }
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun addBufferMusic(fileName: String,dir: String){
        map[fileName] = dir+fileName
    }

    fun isContainsFile(fileName: String): Boolean{
        return map.containsKey(fileName)
    }
}