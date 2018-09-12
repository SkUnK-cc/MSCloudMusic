package com.example.hp.mycloudmusic.download

import android.text.TextUtils
import com.example.hp.mycloudmusic.api.RetrofitFactory
import com.example.hp.mycloudmusic.musicInfo.AudioBean
import com.example.hp.mycloudmusic.musicInfo.merge.Song
import com.example.hp.mycloudmusic.musicInfo.songPlay.SongPlayResp
import com.example.hp.mycloudmusic.util.FileMusicUtils
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile
import java.util.concurrent.atomic.AtomicReference
//多文件下载
class DownloadManager {


    companion object {
        private final val INSTANCE:AtomicReference<DownloadManager> = AtomicReference()
        fun getInstance(): DownloadManager{
            while(true) {
                var current: DownloadManager = INSTANCE.get()
                if (current != null) {
                    return current
                }
                current = DownloadManager()
                //compareAndSet:将INSTANCE中的值与第一个参数比较，如果相同则返回true并用第二个参数更新INSTANCE中的值，
                //如果不相同，返回false
                if (INSTANCE.compareAndSet(null, current)) {
                    return current
                }
            }
        }
    }

    private var downCalls: HashMap<String,Call> = HashMap<String,Call>()
    var client: OkHttpClient? = null
    var ArrayList<>

    private var downloadObserver: DownloadObserver = object: DownloadObserver(){

        override fun onSubscribe(d: Disposable) {
            super.onSubscribe(d)
        }

        override fun onNext(t: DownloadInfo) {
            super.onNext(t)
        }

        override fun onComplete() {
            super.onComplete()
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }
    }

    private fun DownloadManager(){
        client = OkHttpClient.Builder().build()
    }

    fun downloadSong(song: Song){
        var audioBean: AudioBean = AudioBean(song)
        var lrcFileName = FileMusicUtils.getLrcFileName(song.title, song.artist)
        var lrcFile: File = File(FileMusicUtils.getLrcDir() +lrcFileName)
        getSongNetInfo(song)
        if(!TextUtils.isEmpty(song.lrclink) && !lrcFile.exists()){
            downloadLrc(FileMusicUtils.getLrcDir(),lrcFileName,song.lrclink)
        }
    }

    fun downloadLrc(lrcDir: String, lrcFileName: String, lrclink: String) {

    }

    fun getSongNetInfo(song: Song){
        RetrofitFactory.provideBaiduApi()
                .querySong(song.getSong_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<SongPlayResp> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(resp: SongPlayResp) {
                        if (resp != null && resp.isValid) {
                            song.bitrate = resp.bitrate
                            song.songInfo = resp.songinfo
                            var downloadInfo = createSongDownloadInfo(song)
                            download(song,downloadInfo)
                        }
                    }
                    override fun onError(e: Throwable) {}
                    override fun onComplete() {}
                })
    }

    private fun download(song: Song, downloadInfo: DownloadInfo?) {
        if(downloadInfo == null || downloadInfo.url.equals(""))return
        Observable.just(downloadInfo)
                .filter { !downCalls.containsKey(downloadInfo.url) }
                .flatMap { Observable.just(getDownloadInfo(it)) }
                .flatMap { Observable.create(DownloadSubscribe(downloadInfo)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    private fun getDownloadInfo(downloadInfo: DownloadInfo){
        var contentLength:Long = getContentLength(downloadInfo.url)
        downloadInfo.total = contentLength
        var file:File = File(downloadInfo.dir,downloadInfo.fileName)
        if(file.exists()){
            downloadInfo.progress = file.length()
        }
        if(downloadInfo.internal!=null)getDownloadInfo(downloadInfo.internal!!)
    }

    private fun getContentLength(url: String): Long {
        var request: Request = Request.Builder()
                .url(url)
                .build()
        var response: Response = client!!.newCall(request).execute()
        if(response!=null && response.isSuccessful){
            var contentLength: Long= response.body()!!.contentLength()
            response.close()
            if(contentLength==0L)return DownloadInfo.TOTAL_ERROR
            return contentLength
        }
        return DownloadInfo.TOTAL_ERROR
    }

    private fun createSongDownloadInfo(song: Song): DownloadInfo? {
        var downloadInfo: DownloadInfo? = null
        if(song.bitrate!=null && song.bitrate.file_link!=null && !song.bitrate.file_link.equals("")){
            downloadInfo = DownloadInfo(song.bitrate.file_link,null)
            downloadInfo.fileName = FileMusicUtils.getLocalMusicName(song.title,song.artist)
            downloadInfo.dir = FileMusicUtils.getLocalMusicDir()
            if(!TextUtils.isEmpty(song.lrclink)){
                var inter = DownloadInfo(song.lrclink,null)
                inter.fileName = FileMusicUtils.getLrcFileName(song.title, song.artist)
                inter.dir = FileMusicUtils.getLrcDir()
                downloadInfo.internal = inter
            }
        }
        return downloadInfo

    }

    private inner class DownloadSubscribe : ObservableOnSubscribe<DownloadInfo> {
        private var downloadInfo: DownloadInfo? = null

        constructor(downloadInfo:DownloadInfo){
            this.downloadInfo = downloadInfo
        }

        override fun subscribe(e: ObservableEmitter<DownloadInfo>) {
            download(downloadInfo,e)
        }

        private fun download(info: DownloadInfo?,e: ObservableEmitter<DownloadInfo>) {
            if(info == null)return
            var link = info.url
            var downloadedLength = info.progress
            var request: Request = Request.Builder()
                    .addHeader("RANGE","bytes=" + downloadedLength + "-")
                    .url(link)
                    .build()
            var call = client!!.newCall(request)
            downCalls.put(link,call)
            var response = call.execute()

            var file = File(info.dir,info.fileName)
            var input: InputStream? = null
            var saveFile: RandomAccessFile? = null
            try {
                saveFile = RandomAccessFile(file, "rw")
                saveFile.seek(info.progress)
                if (response == null || !response.isSuccessful) return
                input = response.body()!!.byteStream()
                var buffer: ByteArray = ByteArray(2048)
                //kotlin 中等式(赋值)不是一个表达式
                while ((input.read(buffer)) != -1) {
                    info.progress += buffer.size
                    saveFile.write(buffer, 0, buffer.size)
                    //这里传入整个downloadInfo链
                    e.onNext(downloadInfo!!)
                }
            }finally{
                if(input!=null) {
                    input.close()
                }
                if(saveFile!=null){
                    saveFile.close()
                }
                response.body()?.close()
            }
            if(info.internal!=null)download(info.internal,e)
            e.onComplete()
        }
    }

    interface DownloadListener{
        fun onNext(downloadInfo: DownloadInfo)
        fun onComplete()
    }
}

