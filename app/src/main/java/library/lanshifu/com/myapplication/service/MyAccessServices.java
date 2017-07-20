package library.lanshifu.com.myapplication.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;

import library.lanshifu.com.lsf_library.baserx.RxManager;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.window.WindowService;

/**
 * Created by lWX385269 lanshifu on 2017/2/9.
 */

public class MyAccessServices extends AccessibilityService{


    private RxManager mRxManager;
    private Intent service;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        String pkgName = event.getPackageName().toString();
        String className = event.getClassName().toString();

//        T.showShort(className);
//        L.e("111","pkgName:"+pkgName);

        if(mRxManager ==null){
            mRxManager = new RxManager();
        }
        mRxManager.post("packagename","类名："+className);

    }

    //此方法不走
    @Override
    public void onInterrupt() {
        if(mRxManager!=null){
            mRxManager.clear();
        }
        if(service!=null){
            stopService(service);
        }
        T.showShort("onInterrupt");

    }

    @Override
    public boolean onUnbind(Intent intent) {
        T.showShort("已关闭");
        if(mRxManager!=null){
            mRxManager.clear();
        }
        if(service!=null){
            stopService(service);
        }
        return super.onUnbind(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onServiceConnected() {
        AccessibilityServiceInfo info = getServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.notificationTimeout = 500;
        setServiceInfo(info);
        setServiceInfo(info);
        super.onServiceConnected();
        T.showShort("已连接");

        service = new Intent(this,WindowService.class);
        startService(service);

    }



}
