package library.lanshifu.com.myapplication.net.api;

import io.reactivex.Observable;
import library.lanshifu.com.myapplication.model.SongDetailInfo;
import library.lanshifu.com.myapplication.model.SongListDetail;
import library.lanshifu.com.myapplication.model.WrapperSongListInfo;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by lanshifu on 2017/9/21.
 */

public interface MusicApi {



    //获取全部歌单
    @GET("ting")
    @Headers("user-agent:Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
    Observable<WrapperSongListInfo> getSongListAll(@Query("format") String format,
                                                   @Query("from") String from,
                                                   @Query("method") String method,
                                                   @Query("page_size") int page_size,
                                                   @Query("page_no") int page_no);

    //获取某个歌单中的信息
    @GET("ting")
    @Headers("user-agent:Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
    Observable<SongListDetail> getSongListDetail(@Query("format") String format,
                                                 @Query("from") String from,
                                                 @Query("method") String method,
                                                 @Query("listid") String listid);

    //获取某个歌曲的信息，返回播放链接
    @GET("ting")
    @Headers("user-agent:Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)")
    Observable<SongDetailInfo> getSongDetail(@Query("from") String from,
                                             @Query("version") String version,
                                             @Query("format") String format,
                                             @Query("method") String method,
                                             @Query("songid") String songid);
}
