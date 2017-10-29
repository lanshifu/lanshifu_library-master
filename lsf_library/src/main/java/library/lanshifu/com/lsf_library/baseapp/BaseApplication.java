package library.lanshifu.com.lsf_library.baseapp;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;

import library.lanshifu.com.lsf_library.BuildConfig;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.lsf_library.utils.T;

/**
 * Created by 蓝师傅 on 2016/12/7.
 */

public class BaseApplication extends Application {

    private static BaseApplication instance;
    protected static Context context;

    public static Context getContext() {
        return context;

    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();

        T.init(context);
        L.init(true,"lanshifu");
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
//        Logger.addLogAdapter(new DiskLogAdapter());
        FormatStrategy formatStrategy = CsvFormatStrategy.newBuilder()
                .tag("lanshifu")
                .build();

        Logger.addLogAdapter(new DiskLogAdapter(formatStrategy));





//        File cacheDir =StorageUtils.getOwnCacheDirectory(this, "imageloader/Cache");
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration
//                .Builder(getApplicationContext())
//                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
//                .threadPoolSize(3)//线程池内加载的数量
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
//                .memoryCacheSize(2 * 1024 * 1024)
//                .discCacheSize(50 * 1024 * 1024)
//                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .discCacheFileCount(100) //缓存的文件数量
//                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
//                .discCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
//                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
//                .writeDebugLogs() // Remove for release app
//                .build();//开始构建
//
//        ImageLoader.getInstance().init(config);//全局初始化此配置


    }


    public static BaseApplication getInstance(){
        return instance;

    }
}
