
package library.lanshifu.com.myapplication.net.progress.body;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import java.io.IOException;
import java.util.List;

import library.lanshifu.com.myapplication.net.progress.ProgressListener;
import library.lanshifu.com.myapplication.net.progress.ProgressManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * ================================================
 * 继承于 {@link RequestBody}, 通过此类获取 Okhttp 上传的二进制数据
 * ================================================
 */
public class ProgressRequestBody extends RequestBody {

    protected Handler mHandler;
    protected int mRefreshTime;
    protected final RequestBody mDelegate;
    protected final ProgressListener[] mListeners;
    protected final ProgressInfo mProgressInfo;
    private BufferedSink mBufferedSink;
    private String mUrl;


    public ProgressRequestBody(String url, Handler handler, RequestBody delegate, List<ProgressListener> listeners, int refreshTime) {
        this.mUrl = url;
        this.mDelegate = delegate;
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
        try {
            return mDelegate.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (mBufferedSink == null) {
            mBufferedSink = Okio.buffer(new CountingSink(sink));
        }
        try {
            mDelegate.writeTo(mBufferedSink);
            mBufferedSink.flush();
        } catch (IOException e) {
            e.printStackTrace();
            for (int i = 0; i < mListeners.length; i++) {
                mListeners[i].onError(mProgressInfo.getId(), e);
            }
            sendHandleMsg(ProgressManager.WHAT_REQUEST_ERROR);
            throw e;
        }
    }

    protected final class CountingSink extends ForwardingSink {
        private long totalBytesRead = 0L;
        private long lastRefreshTime = 0L;  //最后一次刷新的时间
        private long tempSize = 0L;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            try {
                super.write(source, byteCount);
            } catch (IOException e) {
                e.printStackTrace();
                for (int i = 0; i < mListeners.length; i++) {
                    mListeners[i].onError(mProgressInfo.getId(), e);
                }
                //通知去除监听
                sendHandleMsg(ProgressManager.WHAT_REQUEST_ERROR);
                throw e;
            }
            if (mProgressInfo.getContentLength() == 0) { //避免重复调用 contentLength()
                mProgressInfo.setContentLength(contentLength());
            }
            totalBytesRead += byteCount;
            tempSize += byteCount;
            if (mListeners != null) {
                long curTime = SystemClock.elapsedRealtime();
                if (curTime - lastRefreshTime >= mRefreshTime || totalBytesRead == mProgressInfo.getContentLength()) {
                    final long finalTempSize = tempSize;
                    final long finalTotalBytesRead = totalBytesRead;
                    final long finalIntervalTime = curTime - lastRefreshTime;
                    for (int i = 0; i < mListeners.length; i++) {
                        final ProgressListener listener = mListeners[i];
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // Runnable 里的代码是通过 Handler 执行在主线程的,外面代码可能执行在其他线程
                                // 所以我必须使用 final ,保证在 Runnable 执行前使用到的变量,在执行时不会被修改
                                mProgressInfo.setEachBytes(finalTempSize);
                                mProgressInfo.setCurrentbytes(finalTotalBytesRead);
                                mProgressInfo.setIntervalTime(finalIntervalTime);
                                mProgressInfo.setFinish(finalTotalBytesRead == mProgressInfo.getContentLength());
                                listener.onProgress(mProgressInfo);
                                if(mProgressInfo.isFinish()){
                                    sendHandleMsg(ProgressManager.WHAT_REQUEST_FINISH);
                                }
                            }
                        });
                    }
                    lastRefreshTime = curTime;
                    tempSize = 0;
                }
            }
        }
    }

    private void sendHandleMsg(int what){
        //通知去除监听
        Message message = Message.obtain();
        message.what = what;
        message.obj = mUrl;
        mHandler.sendMessage(message);
    }
}
