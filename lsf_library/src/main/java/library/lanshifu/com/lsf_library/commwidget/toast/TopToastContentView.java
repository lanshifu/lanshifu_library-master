package library.lanshifu.com.lsf_library.commwidget.toast;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 顶部toast布局，主要处理消失时候取消handler消息
 * Created by lanshifu on 2017/9/16.
 */

public class TopToastContentView extends LinearLayout {

    public static final int WHAT = 0;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (onHandlerCallBackListner != null) {
                    onHandlerCallBackListner.handlerMsg();
                }
            }
        }
    };

    public TopToastContentView(Context context) {
        super(context);
    }

    public TopToastContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TopToastContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeMessages(WHAT);
    }

    public void setOnHandlerCallBackListner(OnHandlerCallBackListner onHandlerCallBackListner) {
        this.onHandlerCallBackListner = onHandlerCallBackListner;
    }

    public OnHandlerCallBackListner onHandlerCallBackListner;

    public interface OnHandlerCallBackListner {
        void handlerMsg();
    }
}
