package library.lanshifu.com.myapplication.net.api;

import java.util.List;

import library.lanshifu.com.myapplication.model.LiveCategory;
import library.lanshifu.com.myapplication.model.Recommend;
import retrofit2.http.GET;
import rx.Observable;

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

}
