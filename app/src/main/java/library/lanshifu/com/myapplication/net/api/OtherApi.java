package library.lanshifu.com.myapplication.net.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * Created by lanshifu on 2017/10/26.
 */

public interface OtherApi {

    @GET("70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3689295147,509373218&fm=26&gp=0.jpg")
    Observable<ResponseBody> downloadPicture();
}
