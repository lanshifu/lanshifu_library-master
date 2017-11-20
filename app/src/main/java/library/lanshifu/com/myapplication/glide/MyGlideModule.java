package library.lanshifu.com.myapplication.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

/**
 * 更改Glide和配置以及替换Glide组件
 * Created by lanshifu on 2017/11/15.
 */

public class MyGlideModule implements GlideModule {
    public static final int DISK_CACHE_SIZE = 500 * 1024 * 1024;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //Glide加载的图片都会缓存到SD卡上了,硬盘缓存的大小调整成了500M
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context,DISK_CACHE_SIZE));
        //Glide加载图片的默认格式是RGB_565
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        //调用了Glide的register()方法来注册组件
        glide.register(GlideUrl.class, InputStream.class, new OkHttpGlideUrlLoader.Factory());
    }
}
