package library.lanshifu.com.myapplication.wifi.wifitransfe;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.OnClick;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.myapplication.Constant;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.model.ClientScanResult;
import library.lanshifu.com.myapplication.net.MyObserver;
import library.lanshifu.com.myapplication.net.RxSchedulerHelper;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.receiver.WifiAPBroadcastReceiver;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.utils.ApMgr;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.utils.NetUtils;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.utils.TextUtils;
import library.lanshifu.com.myapplication.wifi.wifitransfe.core.utils.WifiMgr;
import library.lanshifu.com.myapplication.wifi.wifitransfe.micro_server.AndroidMicroServer;
import library.lanshifu.com.myapplication.wifi.wifitransfe.micro_server.DownloadResUriHandler;
import library.lanshifu.com.myapplication.wifi.wifitransfe.micro_server.ImageResUriHandler;
import library.lanshifu.com.myapplication.wifi.wifitransfe.micro_server.IndexResUriHandler;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class WifiTranserActivity extends BaseToolBarActivity {

    private static final String TAG = "WifiTranserActivity";
    /**
     * 主要的线程池
     */
    public static Executor mExcutor = Executors.newFixedThreadPool(5);
    @Bind(R.id.tv_tip_1)
    TextView tvTip1;
    @Bind(R.id.tv_tip_2)
    TextView tvTip2;
    @Bind(R.id.tv_log)
    TextView tvLog;

    private WifiAPBroadcastReceiver mWifiAPBroadcastReceiver;
    private boolean mIsInitialized = false;
    private AndroidMicroServer mAndroidMicroServer;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_wifi_transer;
    }

    @Override
    protected void onViewCreate() {

        setTBTitle("网页传输");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                init();
            } else {
                requestWriteSettings();
            }
        } else {
            init();
        }

        mRxManager.on("log", new Action1<String>() {
            @Override
            public void call(String s) {
                showLog(s);
            }
        });

    }

    private static final int REQUEST_CODE_WRITE_SETTINGS = 1;

    private void requestWriteSettings() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            if (Settings.System.canWrite(this)) {
                showShortToast("有权限");
                init();
            } else {
                showShortToast("无权限？");

            }
        }
    }


    private void init() {
        initUI();

        //1.初始化热点
        WifiMgr.getInstance(this).disableWifi();
        if (ApMgr.isApOn(this)) {
            //如果热点开启中，关闭热点
            ApMgr.disableAp(this);
        }

        mWifiAPBroadcastReceiver = new WifiAPBroadcastReceiver() {


            @Override
            public void onWifiApEnabled() {

                showLog(">>>热点打开咯 !!!");
                Log.i(TAG, "======>>>热点打开咯onWifiApEnabled !!!");
                if (!mIsInitialized) {
                    try {
                        showLog(">>>正在打开服务器...");
                        new Thread(createServer()).start();
                        mIsInitialized = true;
                    } catch (Exception e) {
                        mIsInitialized =false;
                        showLog(">>>出错了..."+e.toString());
                    }

                }

            }
        };

        IntentFilter filter = new IntentFilter(WifiAPBroadcastReceiver.ACTION_WIFI_AP_STATE_CHANGED);
        registerReceiver(mWifiAPBroadcastReceiver, filter);

        ApMgr.isApOn(this); // check Ap state :boolean
        String ssid = TextUtils.isNullOrBlank(Build.DEVICE) ? Constant.DEFAULT_SSID : Build.DEVICE;
        //初始化热点
        ApMgr.configApState(this, ssid); // change Ap state :boolean

    }

    private void initUI() {
        String normalColor = "#ff000000";
        String highlightColor = "#1467CD";
//        <font color=\'#ff0000\'>【题】</font>
        String ssid = TextUtils.isNullOrBlank(Build.DEVICE) ? Constant.DEFAULT_SSID : Build.DEVICE;
        String tip1 = getResources().getString(R.string.tip_web_transfer_first_tip).replace("{hotspot}", ssid);
        String[] tip1StringArray = tip1.split("\\n");
        Spanned tip1Spanned = Html.fromHtml("<font color='" + normalColor + "'>" + tip1StringArray[0].trim() + "</font><br>"
                + "<font color='" + normalColor + "'>" + tip1StringArray[1].trim() + "</font><br>"
                + "<font color='" + highlightColor + "'>" + tip1StringArray[2].trim() + "</font>");
        tvTip1.setText(tip1Spanned);

        String tip2 = getResources().getString(R.string.tip_web_transfer_second_tip);
        String[] tip2StringArray = tip2.split("\\n");
        Spanned tip2Spanned = Html.fromHtml("<font color='" + normalColor + "'>" + tip2StringArray[0].trim() + "</font><br>"
                + "<font color='" + normalColor + "'>" + tip2StringArray[1].trim() + "</font><br>"
                + "<font color='" + highlightColor + "'>" + tip2StringArray[2].trim() + "</font><br>"
                + "<font color='" + normalColor + "'>" + tip2StringArray[3].trim() + "</font><br>");
        tvTip2.setText(tip2Spanned);
    }

    /**
     * 创建一个AndroidMicroServer
     *
     * @return
     * @throws Exception
     */
    public Runnable createServer() throws Exception {
        return new Runnable() {
            @Override
            public void run() {
                mRxManager.post("log", "run");
                String hotspotIpAddr;
                try {
                    // 确保热点开启之后获取得到IP地址
                    hotspotIpAddr = WifiMgr.getInstance(WifiTranserActivity.this).getIpAddressFromHotspot();
                    mRxManager.post("log", "hotspotIpAddr = "+hotspotIpAddr);
                    int count = 0;
                    while (hotspotIpAddr.equals(Constant.DEFAULT_UNKOWN_IP) && count < Constant.DEFAULT_TRY_TIME) {
                        Thread.sleep(500);
                        hotspotIpAddr = WifiMgr.getInstance(WifiTranserActivity.this).getIpAddressFromHotspot();
                        mRxManager.post("log", "receiver serverIp ----->>>" + hotspotIpAddr);
                        Log.i(TAG, "receiver serverIp ----->>>" + hotspotIpAddr);
                        count++;
                    }

                    // 即使热点wifi的IP地址也是无法连接网络 所以采取此策略
                    count = 0;
                    while (!NetUtils.pingIpAddress(hotspotIpAddr) && count < Constant.DEFAULT_TRY_TIME) {
                        Thread.sleep(500);
                        Log.i(TAG, "try to ping ----->>>" + hotspotIpAddr + " - " + count);
                        mRxManager.post("log", "ping ----->>>" + hotspotIpAddr + " - " + count);
                        count++;
                    }
                } catch (Exception e) {
                    //maybe not get the hotspot ip
                    Log.i(TAG, "maybe not get the hotspot ip");
                    mRxManager.post("log", "maybe not get the hotspot ip");
                }

                mAndroidMicroServer = new AndroidMicroServer(Constant.DEFAULT_MICRO_SERVER_PORT);
                mAndroidMicroServer.resgisterResUriHandler(new IndexResUriHandler(WifiTranserActivity.this, AppContext.getAppContext().getFileInfoMap()));
                mAndroidMicroServer.resgisterResUriHandler(new ImageResUriHandler(WifiTranserActivity.this));
                mAndroidMicroServer.resgisterResUriHandler(new DownloadResUriHandler(WifiTranserActivity.this));
                mAndroidMicroServer.start();

                mRxManager.post("log", "服务器已经启动，可以开始传输数据");
            }
        };


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mWifiAPBroadcastReceiver != null) {
            unregisterReceiver(mWifiAPBroadcastReceiver);
            mWifiAPBroadcastReceiver = null;
        }

        closeServer();

        //关闭热点
        ApMgr.disableAp(this);

    }

    /**
     * 关闭Android微服务器
     */
    private void closeServer() {
        if (mAndroidMicroServer != null) {
            mAndroidMicroServer.stop();
            mAndroidMicroServer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWifiAPBroadcastReceiver != null) {
            unregisterReceiver(mWifiAPBroadcastReceiver);
            mWifiAPBroadcastReceiver = null;
        }

        closeServer();

        //关闭热点
        ApMgr.disableAp(this);
        WifiMgr.getInstance(this).openWifi();


    }


    private void showLog(String text) {
        tvLog.append("\r\n" + text);
    }

    private void getConnectCount() {
        Observable.create(new Observable.OnSubscribe<List<ClientScanResult>>() {
            @Override
            public void call(Subscriber<? super List<ClientScanResult>> subscriber) {
                List<ClientScanResult> clientScanResults = readFile();
                subscriber.onNext(clientScanResults);
                subscriber.onCompleted();
            }

        }).compose(RxSchedulerHelper.<List<ClientScanResult>>io_main())
                .subscribe(new MyObserver<List<ClientScanResult>>() {
                    @Override
                    public void _onNext(List<ClientScanResult> clientScanResult) {
                        showLog("数目：" + clientScanResult.size());
                        showLog("数目：" + clientScanResult.toString());
                    }

                    @Override
                    public void _onError(String e) {
                        showLog("读取出错" + e);
                    }
                });


    }

    private List<ClientScanResult> readFile() {
        BufferedReader br = null;
        ArrayList<ClientScanResult> result = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader("/proc/net/arp"));//读取这个文件
            String line;
            while ((line = br.readLine()) != null) {
                L.e("设备信息：" + line);
//                mRxManager.post("log", "设备信息：\r\n"+line);
                String[] splitted = line.split(" +");//将文件里面的字段分割开来
                if (splitted.length >= 4) {
                    // Basic sanity check
                    String mac = splitted[3];// 文件中分别是IP address  HW type Flags HW address mask Device

                    if (!splitted[0].contains("IP")) {
                        //最后如果能匹配 那就证明是连接了热点的手机  加到这个集合里 里面有所有需要的信息
                        result.add(new ClientScanResult(splitted[0], splitted[3], splitted[5], true));
                    }
                }
            }
        } catch (IOException e) {
            mRxManager.post("log", e.toString());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                L.e(e.getMessage());
                mRxManager.post("log", e.toString());
            }
        }
        return result;
    }


    @OnClick({R.id.button_restart, R.id.btn_clear, R.id.btn_showConnectCount})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_restart:
                break;
            case R.id.btn_clear:
                tvLog.setText("日志：");
                break;
            case R.id.btn_showConnectCount:
                getConnectCount();
                break;
        }
    }
}
