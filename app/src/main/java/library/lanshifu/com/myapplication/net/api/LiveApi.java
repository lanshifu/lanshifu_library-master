package library.lanshifu.com.myapplication.net.api;

import java.util.List;

import io.reactivex.Observable;
import library.lanshifu.com.myapplication.model.LiveCategory;
import library.lanshifu.com.myapplication.model.Recommend;
import library.lanshifu.com.myapplication.model.Room;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/8/19.
 */

public interface LiveApi {

    /**
     * 获取分类列表
     * @return
     *
     * categories/list.json
     */
    @GET("json/app/index/category/info-android.json?v=3.0.1&os=1&ver=4")
    Observable<List<LiveCategory>> getAllCategories();

    /**
     * 获取推荐列表
     * @return
     */
    @GET("json/app/index/recommend/list-android.json?v=3.0.1&os=1&ver=4")
    Observable<Recommend> getRecommend();

    /**
     * 进入房间
     * @param uid
     * @return
     */
    @GET("json/rooms/{uid}/info.json?v=3.0.1&os=1&ver=4")
    Observable<Room> enterRoom(@Path("uid")String uid);

    @FormUrlEncoded
    Observable post(
            @Field("body") String body);

}
