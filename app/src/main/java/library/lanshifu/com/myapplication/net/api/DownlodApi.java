package library.lanshifu.com.myapplication.net.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;

/**
 * Created by lanxiaobin on 2017/11/11.
 */

public interface DownlodApi {


    @Streaming
    @GET("http://121.29.10.1/f3.market.xiaomi.com/download/AppStore/0ff0604fd770f481927d1edfad35675a3568ba656/com.tencent.mobileqq.apk")
    Observable<ResponseBody> downloadQQ();
}
