package library.lanshifu.com.myapplication.net;


import android.util.Config;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import library.lanshifu.com.lsf_library.utils.SystemManage;
import library.lanshifu.com.myapplication.MyApp;
import library.lanshifu.com.myapplication.net.api.APIException;
import library.lanshifu.com.myapplication.net.api.ApiConstant;
import library.lanshifu.com.myapplication.net.api.WeatherApi;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lWX385269 lanshifu on 2017/4/10.
 */

public class RetrofitHelper {

    private static final String BASE_URL = "https://api.douban.com/v2/movie/";

    private static OkHttpClient okHttpClient;

    static {
        initOkHttp();
    }


    private static void initOkHttp() {
        if (okHttpClient == null) {

            HttpLoggingInterceptor loggingInterceptor = null;
            //打印请求log日志
            if (Config.DEBUG) {
                loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            }

            //设置Http缓存
            Cache cache = new Cache(new File(MyApp.getInstance()
                    .getCacheDir(), "HttpCache"), 1024 * 1024 * 10);

            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .cache(cache)
                    .addNetworkInterceptor(new CacheInterceptor())
//                    .addNetworkInterceptor(new StethoInterceptor())
                    .retryOnConnectionFailure(true) //错误重连
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
//                   .addInterceptor(new UserAgentInterceptor())
                    .build();
        }

    }


    public static WeatherApi getWeatherService() {
        return createApi(WeatherApi.class, ApiConstant.BASE_URL_WEATHER);

    }

    /**
     * 根据传入的baseUrl，和api创建retrofit
     */
    private static <T> T createApi(Class<T> clazz, String baseUrl) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(clazz);
    }


    /**
     * 添加UA拦截器，B站请求API需要加上UA才能正常使用
     */
    private static class UserAgentInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            Request originalRequest = chain.request();
            Request requestWithUserAgent = originalRequest.newBuilder()
                    .removeHeader("User-Agent")
                    .addHeader("User-Agent", ApiConstant.COMMON_UA_STR)
                    .build();
            return chain.proceed(requestWithUserAgent);
        }
    }

    /**
     * 为okhttp添加缓存，这里是考虑到服务器不支持缓存时，从而让okhttp支持缓存
     */
    private static class CacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            // 有网络时 设置缓存超时时间1个小时
            int maxAge = 60 * 60;
            // 无网络时，设置超时为1天
            int maxStale = 60 * 60 * 24;
            Request request = chain.request();
            if (SystemManage.checkNetWorkStatue(MyApp.getInstance())) {
                //有网络时只从网络获取
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
            } else {
                //无网络时只从缓存中读取
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            if (SystemManage.checkNetWorkStatue(MyApp.getInstance())) {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    }


    public <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }


    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    public class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (!httpResult.isSuccess()) {
                throw new APIException(httpResult.code, httpResult.msg);
            }
            return httpResult.data;
        }
    }

}
