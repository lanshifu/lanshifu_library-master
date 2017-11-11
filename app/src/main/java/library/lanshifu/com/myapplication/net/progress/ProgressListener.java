package library.lanshifu.com.myapplication.net.progress;


import library.lanshifu.com.myapplication.net.progress.body.ProgressInfo;

/**
 * Created by lanxiaobin on 2017/10/25.
 */

public interface ProgressListener {

    /**
     * 进度监听
     *
     * @param progressInfo 关于进度的所有信息
     */
    void onProgress(ProgressInfo progressInfo);

    /**
     * 错误监听
     *
     * @param id 进度信息的 id
     * @param e 错误
     */
    void onError(long id, Exception e);
}
