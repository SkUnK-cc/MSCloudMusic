package com.example.hp.mycloudmusic.download

import android.text.TextUtils
import android.util.Log
import com.example.hp.mycloudmusic.CMApplication
import com.example.hp.mycloudmusic.api.RetrofitFactory
import com.example.hp.mycloudmusic.api.RxSchedulers
import com.example.hp.mycloudmusic.base.BaseAppHelper
import com.example.hp.mycloudmusic.musicInfo.AudioBean
import com.example.hp.mycloudmusic.musicInfo.merge.Song
import com.example.hp.mycloudmusic.musicInfo.songPlay.SongPlayResp
import com.example.hp.mycloudmusic.provider.BufferMusicProvider
import com.example.hp.mycloudmusic.rx.BaseObserver
import com.example.hp.mycloudmusic.util.DevUtil
import com.example.hp.mycloudmusic.util.FileMusicUtils
import com.example.hp.mycloudmusic.util.LogUtils
import com.example.hp.mycloudmusic.util.SpUtils
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile
import java.util.concurrent.atomic.AtomicReference

//多文件下载https://www.jianshu.com/p/fe3607dddacc
class MusicDownloadManager() {

    companion object {
        const val TAG = "MusicDownloadManager"

        const val TYPE_DOWNLOAD = "download"
        const val TYPE_CACHE = "cache"

        private val INSTANCE:AtomicReference<MusicDownloadManager> = AtomicReference()
        fun getInstance(): MusicDownloadManager{
            while(true) {
                var current: MusicDownloadManager? = INSTANCE.get()//此处get方法可能返回null，current应为可空类型
                if (current != null) {
                    return current
                }
                current = MusicDownloadManager()
                //compareAndSet:将INSTANCE中的值与第一个参数比较，如果相同则返回true并用第二个参数更新INSTANCE中的值，
                //如果不相同，返回false
                if (INSTANCE.compareAndSet(null, current)) {
                    return current
                }
            }
        }
    }

    private var downCalls: HashMap<String,Call> = HashMap()
    var client: OkHttpClient? = null
    var listeners: HashMap<String,ArrayList<DownloadListener>> = HashMap()

    init {
        client = OkHttpClient.Builder().build()
    }

    fun downloadSong(song: Song, type: String){
        Log.e("MusicDownloadManager","downloadSong")
        getSongNetInfo(song,type)
    }

    private fun getSongNetInfo(song: Song, type: String){
        Log.e("MusicDownloadManager","getSongNetInfo")
        RetrofitFactory.provideBaiduApi()
                .querySong(song.getSong_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<SongPlayResp>() {
                    override fun onNext(resp: SongPlayResp) {
                        if (resp.isValid) {
                            song.bitrate = resp.bitrate
                            song.songInfo = resp.songinfo
                            //封装mp3和歌词的downloadInfo
                            val downloadInfo = createSongDownloadInfo(song,type)
                            if(downloadInfo==null)return
                            download(song,downloadInfo,type)
                        }
                    }
                })
    }

    private fun download(song: Song, downloadInfo: DownloadInfo?, type: String) {
        if(downloadInfo == null || downloadInfo.url == "")return
        Log.e("MusicDownloadManager","download")
        Observable.just(downloadInfo)
                .filter { !downCalls.containsKey(it.url) }
                //flatmap将一个Observable变换为一个或多个Observable
                //此处主要的作用是将一个downloadInfo用DownloadSubscribe包装起来
                //DownloadSubscribe主要起下载作用
                .flatMap { Observable.create(DownloadSubscribe(it)) }
                .compose(RxSchedulers.compose())
                .subscribe(object: DownloadObserver(downloadInfo){
                    // onNext 用于反馈进度
                    override fun onNext(t: DownloadInfo) {
                        super.onNext(t)
                        Log.e("MusicDownloadManager","正在下载..."+t.getDown())
                        val specListeners = listeners[t.url]
                        if(specListeners == null)return
                        for(l in specListeners){
                            l.onNext(t)
                        }
                    }
                    // onComplete 代表下载完成
                    override fun onComplete() {
                        super.onComplete()
                        val audioBean = AudioBean(song)
                        audioBean.fileName = mInfo!!.fileName
                        audioBean.path = mInfo!!.dir+mInfo!!.fileName
                        CMApplication.provideLiteOrm().insert(audioBean)
                        BaseAppHelper.get().localMusicChanged = true
                        DevUtil.d("MusicDownloadManager",audioBean.title+audioBean.artist+" 下载完成！")
                        BufferMusicProvider.addBufferMusic(mInfo!!.fileName,mInfo!!.dir)
                        if(type== TYPE_CACHE && mInfo?.total!!>0L){
                            var remaind = SpUtils.getTodayBufferRemaindFlow()
                            DevUtil.d(TAG,"remaind=$remaind")
                            remaind -= mInfo?.total?:0
                            DevUtil.d(TAG,"remaind after download=$remaind")
                            SpUtils.setTodayBufferRemaindFlow(if(remaind>=0)remaind else 0L)
                        }
                        if(mInfo == null)return
                        val specListeners: ArrayList<DownloadListener>? = listeners[mInfo!!.url]
                                ?: return
                        for(l in specListeners!!) {
                            l.onComplete(mInfo)
                        }
                    }
                })
    }

    private fun getDownloadInfo(downloadInfo: DownloadInfo?){
        val contentLength:Long = getContentLength(downloadInfo!!.url)
        downloadInfo.total = contentLength
        val file = File(downloadInfo.dir,downloadInfo.fileName)
        if(file.exists()){
            downloadInfo.progress = file.length()
        }
        if(downloadInfo.internal!=null)getDownloadInfo(downloadInfo.internal!!)
    }

    private fun getContentLength(url: String): Long {
        val request: Request = Request.Builder()
                .url(url)
                .build()
        try {
            Log.e("getContentLength","执行")
            if(client == null)Log.e("getContentLength","client is null!")

            val response: Response = client!!.newCall(request).execute()
            Log.e("getContentLength","执行完成")
            if (response.isSuccessful) {
                val contentLength: Long = response.body()!!.contentLength()
                response.close()
                if (contentLength == 0L) return DownloadInfo.TOTAL_ERROR
                return contentLength
            }
        }catch (e:IOException){
            e.printStackTrace()
        }
        return DownloadInfo.TOTAL_ERROR
    }

    private fun createSongDownloadInfo(song: Song, type: String): DownloadInfo? {
        var downloadInfo: DownloadInfo? = null
        if(song.bitrate!=null && song.bitrate.file_link != ""){
            downloadInfo = DownloadInfo(song.bitrate.file_link,null)
            downloadInfo.fileName = FileMusicUtils.getLocalMusicName(song.title,song.artist)
            downloadInfo.dir = getDirPath(type)
            val dir = File(downloadInfo.dir)
            if(dir.exists() && dir.isDirectory &&  dir.listFiles().contains(File(downloadInfo.fileName))){
                DevUtil.e(TAG,"该歌曲已经存在")
                return null
            }
            if(!TextUtils.isEmpty(song.lrclink)){
                val inter = DownloadInfo(song.lrclink,null)
                inter.fileName = FileMusicUtils.getLrcFileName(song.title, song.artist)
                inter.dir = FileMusicUtils.getLrcDir()
                downloadInfo.internal = inter
            }
        }
        return downloadInfo
    }

    private fun getDirPath(type: String): String {
        when(type){
            TYPE_DOWNLOAD -> {
                return FileMusicUtils.getLocalMusicDir()
            }
            TYPE_CACHE -> {
                return FileMusicUtils.getMusicDiskCacheDir()
            }
        }
        return ""
    }

    fun addListener(urlString: String,listener: DownloadListener?){
        if(listener==null)return
        if(listeners.containsKey(urlString)){
            listeners.get(urlString)?.add(listener)
        }else{
            val array = ArrayList<DownloadListener>()
            array.add(listener)
            listeners[urlString] = array
        }
    }

    fun removeListener(urlString: String,listener: DownloadListener?){
        if(listener==null)return
        if(listeners.containsKey(urlString)){
            listeners[urlString]?.remove(listener)
        }
    }


    private inner class DownloadSubscribe(downloadInfo: DownloadInfo) : ObservableOnSubscribe<DownloadInfo> {
        private var downloadInfo: DownloadInfo? = downloadInfo

        init {
            getDownloadInfo(downloadInfo)
        }

        override fun subscribe(e: ObservableEmitter<DownloadInfo>) {
            Log.e("DownloadSubscribe","subscribe")
            download(downloadInfo,e)
        }

        private fun download(info: DownloadInfo?,e: ObservableEmitter<DownloadInfo>) {
            LogUtils.e("DownloadSubscribe")
            if(info == null){
                DevUtil.e(TAG,"info is null")
                return
            }
            DevUtil.e(TAG,"info.url= ${info.url}")
            val link = info.url
            DevUtil.e(TAG, "url= $link")
            val downloadedLength = info.progress
            val request: Request = Request.Builder()
                    .addHeader("RANGE","bytes=" + downloadedLength + "-"+info.total)
                    .url(link)
                    .build()
            val call = client!!.newCall(request)
            downCalls[link] = call  //在hashmap中添加一个键值对
            val response = call.execute()

            val file = File(info.dir,info.fileName)
            var input: InputStream? = null
            var saveFile: RandomAccessFile? = null
            try {
                saveFile = RandomAccessFile(file, "rw")
                saveFile.seek(info.progress)
                if (response == null || !response.isSuccessful) return
                input = response.body()!!.byteStream()
                val buffer = ByteArray(2048)
                var len: Int
                //kotlin 中等式(赋值)不是一个表达式
                //当没有更多数据的时候，read方法会返回-1,
                // let 操作符返回值为函数块的最后一行或指定return表达式
                while ( input.read(buffer).let { len=it;it!=-1 }) {
                    LogUtils.e("while循环")
                    info.progress += len
                    saveFile.write(buffer, 0, len)
                    //这里传入整个downloadInfo链

                    e.onNext(downloadInfo!!)    //反馈进度
                }
                downCalls.remove(link)
            }finally{
                input?.close()
                saveFile?.close()
                response.body()?.close()
            }
            if(info.internal!=null)download(info.internal,e)
            Log.e("下载完成","total="+info.total+"\nprogress="+info.progress)
            e.onComplete()
        }
    }

    interface DownloadListener{
        fun onNext(downloadInfo: DownloadInfo)
        fun onComplete(downloadInfo: DownloadInfo?)
    }
}


