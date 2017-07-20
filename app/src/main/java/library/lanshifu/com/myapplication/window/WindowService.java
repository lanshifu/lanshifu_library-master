package library.lanshifu.com.myapplication.window;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Random;

import library.lanshifu.com.lsf_library.baserx.RxManager;
import library.lanshifu.com.myapplication.R;
import rx.functions.Action1;

/**
 * Created by lWX385269 lanshifu on 2017/2/8.
 */

public class WindowService extends Service {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wmParams;
    private View mWindowView;
    private TextView mPercenTv;
    private int mStartX;
    private int mStartY;
    private int mEndX;
    private int mEndY;
    private RxManager mRxManager;

    @Override
    public void onCreate() {
        super.onCreate();

        initWindowParams();
        initView();
        addWindowView();
        initClick();
        mRxManager = new RxManager();
        mRxManager.on("packagename", new Action1<String>() {
            @Override
            public void call(String s) {
                if (mPercenTv != null) {
                    mPercenTv.setText(s);
                }
            }
        });

    }

    private void addWindowView() {
        mWindowManager.addView(mWindowView, wmParams);
    }

    private void initView() {

        mWindowView = LayoutInflater.from(this).inflate(R.layout.layout_windw, null);
        mPercenTv = (TextView) mWindowView.findViewById(R.id.percent);

    }

    private void initWindowParams() {
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
//        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.format = PixelFormat.TRANSLUCENT;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWindowView != null) {
            mWindowManager.removeView(mWindowView);
        }
        if (mRxManager != null) {
            mRxManager.clear();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    void initClick() {
        mWindowView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mStartX = (int) event.getRawX();
                        mStartY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mEndX = (int) event.getRawX();
                        mEndY = (int) event.getRawY();
                        if (needIntercept()) {
                            wmParams.x = (int) event.getRawX() - mWindowView.getMeasuredWidth() / 2;
                            wmParams.y = (int) event.getRawY() - mWindowView.getMeasuredHeight() / 2;
                            mWindowManager.updateViewLayout(mWindowView, wmParams);
                            return true;
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        if (needInterceptUp()) {
                            return true;
                        }
                        break;

                }

                return false;
            }
        });

        mWindowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int rgb = Color.rgb(random.nextInt(250), random.nextInt(250), random.nextInt(250));
                mPercenTv.setBackgroundColor(rgb);
            }
        });


    }


    private boolean needIntercept() {
        if (Math.abs(mStartX - mEndX) > 30 || Math.abs(mStartY - mEndY) > 30) {
            return true;
        }
        return false;
    }


    private boolean needInterceptUp() {
        if (Math.abs(mStartX - mEndX) > 100 || Math.abs(mStartY - mEndY) > 100) {
            return true;
        }
        return false;
    }


}
