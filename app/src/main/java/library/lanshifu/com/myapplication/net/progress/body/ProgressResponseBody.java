
package library.lanshifu.com.myapplication.net.progress.body;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import java.io.IOException;
import java.util.List;

import library.lanshifu.com.myapplication.net.progress.ProgressListener;
import library.lanshifu.com.myapplication.net.progress.ProgressManager;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * ================================================
 * 继承于 {@link ResponseBody}, 通过此类获取 Okhttp 下载的二进制数据
 * ================================================
 */
public class ProgressResponseBody extends ResponseBody {

    protected Handler mHandler;
    protected int mRefreshTime;
    protected final ResponseBody mDelegate;
    protected final ProgressListener[] mListeners;
    protected final ProgressInfo mProgressInfo;
    private BufferedSource mBufferedSource;
    private String mUrl;
    private boolean isDownLoadSuccess;

    public ProgressResponseBody(String url, Handler handler, ResponseBody responseBody, List<ProgressListener> listeners, int refreshTime) {
        this.mUrl = url;
        this.mDelegate = responseBody;
        this.mListeners = listeners.toArray(new ProgressListener[listeners.size()]);
        this.mHandler = handler;
        this.mRefreshTime = refreshTime;
        this.mProgressInfo = new ProgressInfo(System.currentTimeMillis());
    }

    @Override
    public MediaType contentType() {
        return mDelegate.contentType();
    }

    @Override
    public long contentLength() {
        return mDelegate.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mDelegate.source()));
        }
        return mBufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            private long totalBytesRead = 0L;
            private long lastRefreshTime = 0L;  //最后一次刷新的时间
            private long tempSize = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = 0L;
                try {
                    bytesRead = super.read(sink, byteCount);
                } catch (IOException e) {
                    e.printStackTrace();
                    for (int i = 0; i < mListeners.length; i++) {
                        mListeners[i].onError(mProgressInfo.getId(), e);
                    }
                    sendHandleMsg(ProgressManager.WHAT_RESP_ERROR);
                    throw e;
                }
                if (mProgressInfo.getContentLength() == 0) { //避免重复调用 contentLength()
                    mProgressInfo.setContentLength(contentLength());
                }
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                tempSize += bytesRead != -1 ? bytesRead : 0;
                if (mListeners != null && !mProgressInfo.isFinish()) {
                    long curTime = SystemClock.elapsedRealtime();
                    if (curTime - lastRefreshTime >= mRefreshTime || bytesRead == -1 || totalBytesRead == mProgressInfo.getContentLength()) {
                        final long finalBytesRead = bytesRead;
                        final long finalTempSize = tempSize;
                        final long finalTotalBytesRead = totalBytesRead;
                        final long finalIntervalTime = curTime - lastRefreshTime;
                        mProgressInfo.setEachBytes(finalBytesRead != -1 ? finalTempSize : -1);
                        mProgressInfo.setCurrentbytes(finalTotalBytesRead);
                        mProgressInfo.setIntervalTime(finalIntervalTime);
                        mProgressInfo.setFinish(finalBytesRead == -1 && finalTotalBytesRead == mProgressInfo.getContentLength());

                        for (int i = 0; i < mListeners.length; i++) {
                            final ProgressListener listener = mListeners[i];
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // Runnable 里的代码是通过 Handler 执行在主线程的,外面代码可能执行在其他线程
                                    // 所以我必须使用 final ,保证在 Runnable 执行前使用到的变量,在执行时不会被修改
                                    listener.onProgress(mProgressInfo);
                                }
                            });
                        }
                        if (mProgressInfo.isFinish()) {
                            sendHandleMsg(ProgressManager.WHAT_RESP_FINISH);
                        }

                        lastRefreshTime = curTime;
                        tempSize = 0;
                    }
                }
                return bytesRead;
            }
        };
    }

    private void sendHandleMsg(int what) {
        //通知去除监听
        Message message = Message.obtain();
        message.what = what;
        message.obj = mUrl;
        mHandler.sendMessage(message);
    }
}
