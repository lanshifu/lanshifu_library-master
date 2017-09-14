package library.lanshifu.com.myapplication.net.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * Created by lanshifu on 2017/9/14.
 */

public interface JsoupApi {

    @GET("forum.php")
    Observable<ResponseBody> getBasePager();
}
