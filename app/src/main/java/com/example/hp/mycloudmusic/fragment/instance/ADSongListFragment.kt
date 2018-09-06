package com.example.hp.mycloudmusic.fragment.instance

import android.os.Bundle
import com.example.hp.mycloudmusic.R
import com.example.hp.mycloudmusic.adapter.recyclerview.CommonViewHolder
import com.example.hp.mycloudmusic.api.baidu.BaiduMusicApi
import com.example.hp.mycloudmusic.fragment.factory.FragmentFactory
import com.example.hp.mycloudmusic.musicInfo.AbstractMusic
import com.example.hp.mycloudmusic.musicInfo.artistDetail.ArtistInfoResp
import com.example.hp.mycloudmusic.musicInfo.artistDetail.ArtistSongListResp
import com.example.hp.mycloudmusic.musicInfo.merge.Song
import com.example.hp.mycloudmusic.mvp.presenter.BasePresenter
import com.example.hp.mycloudmusic.service.PlayService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ADSongListFragment<T : BasePresenter<*>?>: ADetailListFragment<T?,Song>(),PlayMusicFragment.PlayMusicBackListener{

    companion object{
        fun newInstance(artist: ArtistInfoResp) : ADSongListFragment<*>{
            var fragment = ADSongListFragment<BasePresenter<*>>()
            var bundle = Bundle()
            bundle.putParcelable(ARTIST,artist)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onVisible() {
        artist = arguments!!.getParcelable(ARTIST)
        artist?.let { getListFromNet(it) }
    }

    override fun onInvisible() {

    }

    override fun getListFromNet(artist: ArtistInfoResp) {
        getBaiduApi().getArtistSongList(artist.ting_uid,artist.artist_id,mPagenum*BaiduMusicApi.pagenSize,BaiduMusicApi.pagenSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Observer<ArtistSongListResp>{
                    override fun onComplete() {
                    }
                    override fun onSubscribe(d: Disposable) {
                    }
                    override fun onNext(resp: ArtistSongListResp) {
                        if(resp.songlist != null && resp.isValid){
                            mPagenum++
                            mData.addAll(resp.songlist)
                            adapter!!.notifyDataSetChanged()
                        }
                    }
                    override fun onError(e: Throwable) {
                    }
                })
    }

    override fun getItemLayout(): Int {
        return R.layout.merge_song_item
    }

    override fun mConvert(holder: CommonViewHolder, data: Song) {
        holder.setText(R.id.merge_song_title, data.getTitle())
        holder.setText(R.id.merge_song_artist, data.getArtist())
    }

    override fun mOnItemClick(position: Int) {
        val service: PlayService = playService
        service.play(mData as List<AbstractMusic>?,position)
        showPlayMusicFragment()
    }

    fun showPlayMusicFragment(){
//        val transaction = activity!!.supportFragmentManager.beginTransaction()
//        transaction.setCustomAnimations(R.anim.fragment_slide_from_right, 0)
////        val playMusicFragment = PlayMusicFragment()
//        val playMusicFragment = FragmentFactory.getInstance(null).getmPlayMusicFragment()
//        playMusicFragment.setBackListener(this)
//        if (playMusicFragment != null && !playMusicFragment.isAdded) {
//            Log.e("click: ","add")
//            transaction.add(android.R.id.content, playMusicFragment)
//        } else if (playMusicFragment!!.isAdded) {
//            Log.e("click: ","show")
//            transaction.show(playMusicFragment)
//        }
//        transaction.commitAllowingStateLoss()
        val playMusicFragment = FragmentFactory.getInstance(null).getmPlayMusicFragment()
        addOrShowFragmentOnActivity(android.R.id.content,playMusicFragment,R.anim.fragment_slide_from_right)
    }

    override fun playMusicOnBack() {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.fragment_slide_from_left,R.anim.fragment_slide_out_right)
        val playMusicFragment = FragmentFactory.getInstance(null).getmPlayMusicFragment()
//        var detailFragment = FragmentFactory.getInstance(null).getArtistDetailFragment(null)
        if(playMusicFragment!!.isVisible){
            transaction.hide(playMusicFragment)
        }
        transaction.commitAllowingStateLoss()
    }
}
