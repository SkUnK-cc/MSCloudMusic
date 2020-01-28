package com.example.hp.mycloudmusic.cache

import android.text.TextUtils
import com.example.hp.mycloudmusic.util.DevUtil
import com.example.hp.mycloudmusic.util.FileMusicUtils
import com.example.hp.mycloudmusic.util.LogUtils
import java.io.File
import java.io.FileOutputStream
import java.lang.Thread.sleep
import java.net.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.regex.Pattern
import kotlin.experimental.and



object MusicCacheManager {

    private var proxyIdle: Boolean = false
    private var localServer: ServerSocket? = null
    private var localHost = "127.0.0.1"
    private var localPort: Int = 8065
    private var HTTP_PORT = 80
    private var map = mutableMapOf<String,String>()

    private var remoteHostAndPort = ""//这个用来到时替换本地地址的
    var remoteAddress: SocketAddress? = null
    private var trueSocketRequestInfoStr: String = ""
    private var writeFile: Boolean = true   //是否缓存到文件
    private val socketTimeoutTime: Int = 5000
    private var remoteUrl: String? = null
    private var fileTotalLength: Long = 0L
    private val currProxyId: Int = 0
    private val lastProxyId: Int = 0

    init {
        proxyInit()
        startProxy()
    }

    private fun proxyInit(){
        proxyIdle = false
        try {
            if (localServer == null || localServer!!.isClosed) {
                localServer = ServerSocket()
                localServer?.reuseAddress = true
                val socketAddress = InetSocketAddress(localHost, localPort)
                localServer?.bind(socketAddress)
            }
        }catch (e: Exception){
            LogUtils.e("绑定端口失败")
            try {
                localPort--
                localServer = ServerSocket(localPort,0,InetAddress.getByName(localHost))
                localServer?.reuseAddress = true
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }



    fun getLocalURLAndSetRemoteSocketAddr(url: String): String{
        remoteUrl = url
        remoteHostAndPort = ""
        try {
            var localProxyUrl = ""
            val originURI = URI.create(url)
            val remoteHost = originURI.host
            if(!TextUtils.isEmpty(remoteHost)){
                if(originURI.port != -1){ // url 带有 port
                    Thread{
                        remoteAddress = InetSocketAddress(remoteHost,originURI.port)
                    }.start()
                    remoteHostAndPort = "$remoteHost:${originURI.port}"
                    localProxyUrl = url.replace(remoteHostAndPort,"$localHost:$localPort")
                    map[originURI.path] = remoteHostAndPort
                }else{ // url 不带 port
                    if(!TextUtils.isEmpty(remoteHost)){
                        Thread{
                            remoteAddress = InetSocketAddress(remoteHost, HTTP_PORT)
                        }.start()
                        remoteHostAndPort = remoteHost
                        localProxyUrl = url.replace(remoteHostAndPort,"$localHost:$localPort")
                        map[originURI.path] = "$remoteHost:$HTTP_PORT"
                    }
                }
                LogUtils.e("getLocalURLAndSetRemoteSocketAddr,path=${originURI.path}")
            }
            return localProxyUrl
        }catch (e: Exception){
            LogUtils.e("getLocalURLAndSetRemoteSocketAddr failed")
            e.printStackTrace()
            return ""
        }
    }




    fun getTrueSocketRequestInfo(localSocket: Socket){
        val inStream = localSocket.getInputStream()
        var trueSocketRequestInfoStr = ""
        val localRequest = ByteArray(1024)
        writeFile = true
        while(inStream.read(localRequest)!=-1){
            val str = String(localRequest)
            LogUtils.e("getTrueSocketRequestInfo,str=$str")
            trueSocketRequestInfoStr += str
            if(trueSocketRequestInfoStr.contains("GET") && trueSocketRequestInfoStr.contains("\r\n\r\n")){
                trueSocketRequestInfoStr = trueSocketRequestInfoStr.replace("$localHost:$localPort", remoteHostAndPort)
                this.trueSocketRequestInfoStr = trueSocketRequestInfoStr
                if(trueSocketRequestInfoStr.contains("Range")){
                    LogUtils.e("getTrueSocketRequestInfo,=Range=")
                    writeFile = false
                }
                break
            }
        }
    }



    private fun sendRemoteRequest(): Socket {
        val remoteSocket = Socket()
        remoteSocket.connect(remoteAddress,socketTimeoutTime)
        remoteSocket.getOutputStream().write(trueSocketRequestInfoStr.toByteArray())
        remoteSocket.getOutputStream().flush()
        return remoteSocket
    }

    fun startProxy(){
        Thread(Runnable{
            try {
                if (localServer == null) return@Runnable
                val localSocket: Socket = localServer!!.accept()
                getTrueSocketRequestInfo(localSocket)
                while (remoteAddress == null) {
                    sleep(25)
                }
                val remoteSocket = sendRemoteRequest()
                processTrueRequestInfo(remoteSocket, localSocket)
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                if(localServer!=null){
                    localServer!!.close()
                    localServer = null
                }
            }
        }).start()
    }

    private fun processTrueRequestInfo(remoteSocket: Socket, localSocket: Socket) {
        var fops: FileOutputStream? = null
        var theFile: File? = null
        try {
            val inRemoteSocket = remoteSocket.getInputStream() ?: return
            val outLocalSocket = localSocket.getOutputStream() ?: return
            if(writeFile && !TextUtils.isEmpty(remoteUrl)){
                val dirs = FileMusicUtils.getMusicDiskCacheDir()
                theFile = File(dirs,getFileNameByRemoteURL(remoteUrl!!)+".mp3")
                fops = FileOutputStream(theFile)
            }

            try {
                var readLength: Int
                val remote_reply = ByteArray(4096)
                var firstData = true

                while(inRemoteSocket.read(remote_reply,0,remote_reply.size).let { readLength=it;it!=-1 }){
                    DevUtil.e("MusicCacheManager","processFromNet")
                    try{
                        if(firstData){
                            firstData = false
                            val str = String(remote_reply,Charsets.UTF_8)
                            val pattern = Pattern.compile("Content-Length:\\s*(\\d+)")
                            val matcher = pattern.matcher(str)
                            if(matcher.find()){
                                fileTotalLength = matcher.group(1).toLong()
                            }
                        }
                    }catch (e: Exception){
                        e.printStackTrace()
                    }

                    try{
                        outLocalSocket.write(remote_reply,0,readLength)
                        outLocalSocket.flush()
                    }catch (e: Exception){
                        e.printStackTrace()
                    }

                    if(writeFile){
                        try {
                            if(fops!=null){
                                LogUtils.e("cache to file")
                                fops.write(remote_reply,0,readLength)
                                fops.flush()
                            }
                        }catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
                }
                if(currProxyId != lastProxyId){
                    theFile?.delete()
                }
            }catch (e: Exception){
                theFile?.delete()
                e.printStackTrace()
            }finally {
                inRemoteSocket.close()
                outLocalSocket.close()
                if(fops!=null){
                    fops.close()

                    if(theFile!=null && theFile.exists()){

                    }
                }
                localSocket.close()
                remoteSocket.close()
            }
        }catch (e: Exception){
            e.printStackTrace()
            theFile?.delete()
        }
    }

    fun getFileNameByRemoteURL(url: String): String{
        val md5: MessageDigest?
        try {
            md5 = MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            LogUtils.d("get md5 instance error")
            return ""
        }
        val hash = md5.digest(url.toByteArray())
        val hex = StringBuilder(hash.size*2)
        for( b in hash){
            if((b and 0xff.toByte()) < 0x10){
                hex.append("0")
            }
            hex.append(Integer.toHexString((b and 0xFF.toByte()).toInt()))
        }
        return hex.toString()
    }

    fun getCacheFilePath(url: String): String{
        val fileName = getFileNameByRemoteURL(url)
        var fp = File(FileMusicUtils.getMusicDiskCacheDir(), "$fileName.mp3")
        if(fp.exists()){
            return fp.absolutePath
        }else{
            return ""
        }
    }

}