package library.lanshifu.com.myapplication.net;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import library.lanshifu.com.lsf_library.utils.FileUtil;
import library.lanshifu.com.myapplication.net.progress.ProgressListener;
import library.lanshifu.com.myapplication.net.progress.ProgressManager;
import library.lanshifu.com.myapplication.net.progress.body.ProgressInfo;
import okhttp3.ResponseBody;

import static library.lanshifu.com.lsf_library.utils.FileUtil.closeQuietly;

/**
 * 封装了文件下载监听
 * Created by lanxiaobin on 2017/11/8.
 */

public abstract class DownLoadObserver<T> implements Observer<T> {

    private String mDownUrl; //保存路径
    private String mFilepath; //保存路径

    /**
     * 封装了下载
     *
     * @param url       下载地址
     * @param filePath  保存的地址
     */
    public DownLoadObserver(final String url, String filePath) {
       this.mFilepath = filePath;
       this.mDownUrl = url;
        ProgressManager.getInstance().addDownLoadListener(url, new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo progressInfo) {
                DownLoadObserver.this.onProgress(progressInfo);
            }

            @Override
            public void onError(long id, Exception e) {
                onDownFailed(e.getMessage());
                ProgressManager.getInstance().clearDownLoadListener(mDownUrl);
            }
        });
    }


    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        ProgressManager.getInstance().clearDownLoadListener(mDownUrl);

        ResponseBody responseBody = null;
        //保存到文件
        if(!(t instanceof ResponseBody)){
            onDownFailed("传入类型非ResponseBody");
            return;
        }
        responseBody = (ResponseBody) t;
//        if(responseBody.contentLength() >0){
//            onDownFailed("下载失败");
//            return;
//        }
        saveToSD(responseBody);

    }

    @Override
    public void onError(Throwable e) {
        onDownFailed(e.getMessage());

    }

    @Override
    public void onComplete() {

    }

    private void saveToSD(final ResponseBody responseBody) {
        final File file = new File(mFilepath);
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                boolean success = writeFile2Disk(responseBody, file);
                e.onNext(success);
                e.onComplete();
            }
        })
                .compose(RxSchedulerHelper.<Boolean>io_main())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean success) throws Exception {
                        if (success) {
                            onDownLoadSuccess();
                        } else {
                            onDownFailed("保存到sd卡出错，详情看日志");
                        }
                    }
                });


    }

    public static boolean writeFile2Disk(final okhttp3.ResponseBody response, final File file) {
        OutputStream os = null;
        InputStream is = response.byteStream();
        try {
            os = new FileOutputStream(file);
            int len;
            byte[] buff = new byte[1024];

            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
            }
            return true;

        } catch (Exception e) {
            return false;
        } finally {
            closeQuietly(os);
            closeQuietly(is);
        }

    }


    protected abstract void onProgress(ProgressInfo progressInfo);

    protected abstract void onDownLoadSuccess();

    protected abstract void onDownFailed(String error);

}
