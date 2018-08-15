package com.example.hp.mycloudmusic.musicInfo.songPlay

data class Bitrate(val show_link:String,
                   val free: Int,
                   val song_file_id: Long,
                   val file_size: Long,
                   val file_extension: String,
                   val file_duration: Int,
                   val file_bitrate: Int,
                   val file_link: String,
                   val hash: String) {
    /*
        "show_link":"http://zhangmenshiting.qianqian.com/data2/music/fb3757824df30e8b2d7cae2f4891e8d9/594807550/594807550.mp3?xcode=b38a71356f501cb643e6e1d8249fc792",
        "free":1,
        "song_file_id":594807550,
        "file_size":4304843,
        "file_extension":"mp3",
        "file_duration":268,
        "file_bitrate":128,
        "file_link":"http://zhangmenshiting.qianqian.com/data2/music/fb3757824df30e8b2d7cae2f4891e8d9/594807550/594807550.mp3?xcode=b38a71356f501cb643e6e1d8249fc792",
        "hash":"d99595e44e81ce05bae08f2f8aabe4d95c71dc4f"
     */
}