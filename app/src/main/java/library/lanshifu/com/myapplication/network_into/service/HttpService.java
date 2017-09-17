package library.lanshifu.com.myapplication.network_into.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import library.lanshifu.com.myapplication.network_into.NetworkHelper;
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
    private NanoHTTPD httpd;

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
        httpd = new SimpleWebServer(this, NetworkHelper.getIp(), PORT, webRoot);
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
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    protected void notice() {
//        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent intent = new Intent(this, TinyServiceActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent contentIntent = PendingIntent.getActivity(this,
//                R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setContentText("服务器后台运行中");
//        builder.setContentTitle(getString(R.string.app_name));
//        builder.setContentIntent(contentIntent);
//        builder.setWhen(System.currentTimeMillis());
//        Notification notification = builder.getNotification();
//
//
//        nm.notify(my_notice_id, notification);
    }

    protected void clearNotice() {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(my_notice_id);
    }
}
