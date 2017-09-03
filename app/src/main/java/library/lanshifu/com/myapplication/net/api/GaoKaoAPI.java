package library.lanshifu.com.myapplication.net.api;

import library.lanshifu.com.myapplication.model.GaoKaoBean;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/9/3.
 */

public interface GaoKaoAPI {

    /**
     * 获取分类列表
     * @return
     *
     * categories/list.json
     */
    @GET("ksy/?c=core&a=call&_m=gklq.search")
    Observable<GaoKaoBean> search(
            @Query("zkzh") String zkzh,
            @Query("csrq") String location,
            @Query("code") String code
    );

}
