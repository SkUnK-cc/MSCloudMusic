package com.example.hp.mycloudmusic.api.kugou;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface KuGouLyricApi {

    //http://m.kugou.com/app/i/krc.php?cmd=100&hash=3B748967D4578FD04D5504234447208A&timelength=298000&d=0.12693735343635937
    @GET("app/i/krc.php?cmd=100&timelength=298000")
    Observable<ResponseBody> getKuGouLyric(@Query("keyword") String keyword);
}
