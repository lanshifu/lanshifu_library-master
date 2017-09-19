package library.lanshifu.com.myapplication.network_into.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import library.lanshifu.com.lsf_library.baserx.RxManager;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.network_into.NetworkHelper;
import library.lanshifu.com.myapplication.network_into.activity.TinyServiceActivity;
import library.lanshifu.com.myapplication.network_into.tinyserver.NanoHTTPD;
import library.lanshifu.com.myapplication.network_into.tinyserver.SimpleWebServer;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.utils.FileUtils;


public class HttpService extends Service {

    public static final int my_notice_id = 10;
    public static final String SERVER_LOG_CHANGE_INTENT = "server_log_change_intent";
    public static final String TAG = "httpserver";
    public static final int PORT = 10000;
//    	private static final String webRootPath = Environment.getExternalStorageDirectory().toString()+ "/lanshifu/www";
    private static final String webRootPath = FileUtils.getRootDirPath()+"www";
    private static final int NO_1 = 10;
    private NanoHTTPD httpd;

    RxManager mRxManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mRxManager = new RxManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        notice();

        if (httpd != null && httpd.isAlive()) {
            httpd.stop();
        }
        File webRoot = new File(webRootPath);
        if (!webRoot.exists()) {
            webRoot.mkdirs();
            copyIndexHtml();
        }
        httpd = new SimpleWebServer(this, NetworkHelper.getIp(), PORT, webRoot, new SimpleWebServer.OnRequestListener() {
            @Override
            public void requestLog(String log) {
                //请求的日志
                mRxManager.post("server_log",log);
                L.e(log);
            }
        });

        try {
            httpd.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        NetworkHelper.isHttpserverRunning = true;
        return super.onStartCommand(intent, flags, startId);
    }

    private void copyIndexHtml() {
        InputStream is = null;
        FileOutputStream fos = null;
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            fos = new FileOutputStream(webRootPath + "/index.html");
            is = this.getAssets().open("web_index.html");
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void onDestroy() {
        if (httpd != null && httpd.isAlive()) {
            httpd.stop();
        }
        NetworkHelper.isHttpserverRunning = false;
        clearNotice();
        super.onDestroy();
        mRxManager.clear();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    protected void notice() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("tiny服务器");
        builder.setContentText("tiny服务器真在后台运行，点击管理");
        //设置点击通知跳转页面后，通知消失
        builder.setAutoCancel(true);
        Intent intent = new Intent(this,TinyServiceActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(my_notice_id,notification);
    }



    protected void clearNotice() {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(my_notice_id);
    }
}
