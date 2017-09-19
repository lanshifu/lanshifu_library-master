package library.lanshifu.com.myapplication.network_into.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.utils.NetworkUtils;
import library.lanshifu.com.myapplication.MyApp;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.model.HostBean;
import library.lanshifu.com.myapplication.network_into.NetworkHelper;
import library.lanshifu.com.myapplication.widget.CommRecyclerView;

/**
 * Created by lanshifu on 2017/9/17.
 */

public class NetworkScanfActivity extends BaseToolBarActivity {
    private static final String TAG = "NetworkScanfActivity";
    @Bind(R.id.recyclerView)
    CommRecyclerView recyclerView;
    @Bind(R.id.tv_router)
    TextView tvRouter;
    @Bind(R.id.tv_my)
    TextView tvMy;
    private BaseQuickAdapter adapter;
    private NetworkInterface networkInterface;
    private boolean stop = true;

    private static final byte[] NETBIOS_REQUEST = {(byte) 0x82, (byte) 0x28, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x1, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
            (byte) 0x0, (byte) 0x0, (byte) 0x20, (byte) 0x43, (byte) 0x4B, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41,
            (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41,
            (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x41, (byte) 0x0,
            (byte) 0x0, (byte) 0x21, (byte) 0x0, (byte) 0x1};

    private static final short NETBIOS_UDP_PORT = 137;

    private static final Pattern ARP_TABLE_PARSER = Pattern
            .compile("^([\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,3})\\s+([0-9-a-fx]+)\\s+([0-9-a-fx]+)\\s+([a-f0-9]{2}:[a-f0-9]{2}:[a-f0-9]{2}:[a-f0-9]{2}:[a-f0-9]{2}:[a-f0-9]{2})\\s+([^\\s]+)\\s+(.+)$",
                    Pattern.CASE_INSENSITIVE);

    private static final int DATASET_CHANGED = 1;
    private static final int DATASET_HOST_ALIAS_CHANGED = 2;
    private static final int FINISH_REFRESH = 3;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DATASET_CHANGED) {
                HostBean hostBean = (HostBean) msg.obj;
                adapter.addData(hostBean);

            } else if (msg.what == DATASET_HOST_ALIAS_CHANGED) {
                int i = msg.arg1;
                HostBean host = (HostBean) adapter.getData().get(i);
                host.setAlias((String) msg.obj);
                adapter.notifyDataSetChanged();
            } else if (msg.what == FINISH_REFRESH) {
                recyclerView.finishRefresh();
            }
        }
    };
    private DiscoveryThread discoveryThread;
    private ArpReadThread arpReader;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_network_scanf;
    }

    @Override
    protected void onViewCreate() {
        setTBTitle("网络中的主机");

        if (!NetworkUtils.isWifiConnected()) {
            showErrorToast("wifi 没有连接");
            return;
        }

        initRecyclerView();

        try {
            networkInterface = NetworkInterface.getByInetAddress(NetworkHelper.getInetAddress());
        } catch (SocketException e) {
            e.printStackTrace();
        }

        WifiManager wifiManager = (WifiManager) MyApp.getContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        HostBean gateWay = new HostBean(NetworkHelper.getGatewayMac(), NetworkHelper.getGateway(),
                NetworkUtils.vendorFromMac(NetworkUtils.stringMacToByte(NetworkHelper
                        .getGatewayMac())), wifiInfo.getSSID().replace("\"", ""));
        HostBean myself = new HostBean(wifiManager.getConnectionInfo().getMacAddress(), NetworkHelper.getIp(),
                NetworkUtils.vendorFromMac(NetworkUtils.stringMacToByte(wifiInfo.getMacAddress())), Build.MODEL);
//        adapter.addData(gateWay);
//        adapter.addData(myself);
        tvRouter.setText("路由器信息："+gateWay.getIp() +"-"+gateWay.getMac());
        tvMy.setText("我的设备："+myself.getIp() +"-"+myself.getMac());

    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<HostBean, BaseViewHolder>(R.layout.host_list_item, new ArrayList<HostBean>()) {
            @Override
            protected void convert(BaseViewHolder holder, final HostBean host) {
                String alias = host.getAlias();
                holder.setText(R.id.host_ip, alias != null && !alias.isEmpty() ? alias : host.getIp());
                holder.setText(R.id.host_mac, host.getMac());
                holder.setText(R.id.host_vendor, host.getVendor());
                holder.getView(R.id.rl_root).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 功能
                        Intent intent = new Intent(NetworkScanfActivity.this, NetworkFunctionActivity.class);
                        intent.putExtra("host", (Serializable) host);
                        startActivity(intent);
                        NetworkHelper.setTarget(host);
                    }
                });

            }

        };
        recyclerView.setAdapter(adapter);
        recyclerView.setEnableLoadmore(false);
        recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                adapter.replaceData(new ArrayList());
                startDiscovery();
            }
        });
        recyclerView.autoRefresh();
    }

    private void startDiscovery() {
        stop = false;

        if (discoveryThread != null && !discoveryThread.isAlive()) {
            discoveryThread.interrupt();
            discoveryThread = null;
        }
        discoveryThread = new DiscoveryThread();
        discoveryThread.start();

        if (arpReader != null && !arpReader.isAlive()) {
            arpReader.interrupt();
            arpReader = null;
        }
        arpReader = new ArpReadThread();
        arpReader.start();

    }

    /**
     * 多线程按照IP地址递增扫描 使用线程池 固定大小10
     *
     * @author oinux
     */
    class DiscoveryThread extends Thread {

        ExecutorService executor;

        public void run() {
            if (executor != null && !executor.isShutdown()) {
                executor.shutdownNow();
                executor = null;
            }
            executor = Executors.newFixedThreadPool(10);

            int next_int_ip = 0;
            try {
                if (!stop) {
                    next_int_ip = NetworkHelper.getIntNetMask() & NetworkHelper.getIntGateway();
                    for (int i = 0; i < NetworkHelper.getHostCount() && !stop; i++) {
                        next_int_ip = NetworkUtils.nextIntIp(next_int_ip);
                        Log.e(TAG, "DiscoveryThread next_int_ip: " + next_int_ip);
                        if (next_int_ip != -1) {
                            String ip = NetworkUtils.netfromInt(next_int_ip);
                            try {
                                executor.execute(new UDPThread(ip));
                            } catch (RejectedExecutionException e) {
                                break;
                            } catch (OutOfMemoryError m) {
                                break;
                            }
                        }
                        Thread.sleep(10);
                    }
                    sleep(15000);
                    stop = true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (executor != null)
                    executor.shutdownNow();
            }
        }
    }

    /**
     * 发送NETBIOS数据包
     *
     * @author oinux
     */
    class UDPThread extends Thread {

        String target_ip;

        public UDPThread(String target_ip) {
            this.target_ip = target_ip;
        }

        public void run() {
            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket();
                // Log.d(TAG, target_ip);
                InetAddress address = InetAddress.getByName(target_ip);
                DatagramPacket packet = new DatagramPacket(NETBIOS_REQUEST, NETBIOS_REQUEST.length, address, NETBIOS_UDP_PORT);
                socket.setSoTimeout(200);
                socket.send(packet);
                socket.close();
            } catch (UnknownHostException e) {
            } catch (IOException e) {
            } finally {
                if (socket != null)
                    socket.close();
            }
        }
    }

    /**
     * 读取arp缓存文件
     *
     * @author oinux
     */
    class ArpReadThread extends Thread {

        ExecutorService executor;

        public void run() {
            if (executor != null && !executor.isShutdown()) {
                executor.shutdownNow();
                executor = null;
            }
            executor = Executors.newFixedThreadPool(5);

            RandomAccessFile fileReader = null;
            try {
                fileReader = new RandomAccessFile("/proc/net/arp", "r");
                StringBuilder sb = new StringBuilder();
                int len = -1;
                String line = null;
                Matcher matcher = null;

                while (!stop) {
                    sleep(3000);
                    fileReader.seek(0);
                    List<HostBean> mCheckHosts = adapter.getData();
                    List<HostBean> hostBeanList = new ArrayList<>();
                    while (!stop && (len = fileReader.read()) >= 0) {
                        sb.append((char) len);
                        if (len != '\n')
                            continue;
                        line = sb.toString();
                        sb.setLength(0);
                        matcher = ARP_TABLE_PARSER.matcher(line);
                        if (matcher.find()) {
                            String address = matcher.group(1), flags = matcher.group(3), hwaddr = matcher.group(4), device = matcher.group(6);
                            if (device.equals(networkInterface.getDisplayName()) && !hwaddr.equals("00:00:00:00:00:00") && flags.contains("2")) {

                                synchronized (NetworkScanfActivity.class) {

                                    boolean contains = false;

                                    for (HostBean h : mCheckHosts) {
                                        if (h.getMac().equals(hwaddr) || h.getIp().equals(address)) {
                                            contains = true;
                                            break;
                                        }
                                    }
                                    if (!contains) {
                                        byte[] mac_bytes = NetworkUtils.stringMacToByte(hwaddr);
                                        String vendor = NetworkUtils.vendorFromMac(mac_bytes);
                                        HostBean host = new HostBean(hwaddr, address, vendor);
                                        hostBeanList.add(host);

                                        mHandler.obtainMessage(DATASET_CHANGED, host).sendToTarget();
//                                        executor.execute(new RecvThread(address));
                                    }
                                }
                            }
                        }
                    }
//                    mHandler.obtainMessage(DATASET_CHANGED, hostBeanList).sendToTarget();
                }
                mHandler.sendEmptyMessage(FINISH_REFRESH);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileReader != null)
                        fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (executor != null)
                    executor.shutdownNow();
            }
        }
    }


    class RecvThread extends Thread {

        String target_ip;

        public RecvThread(String target_ip) {
            this.target_ip = target_ip;
        }

        public void run() {
            byte[] buffer = new byte[128];
            DatagramSocket socket = null;
            String name;
            try {
                InetAddress inetAddress = InetAddress.getByName(target_ip);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetAddress, NETBIOS_UDP_PORT), query = new DatagramPacket(NETBIOS_REQUEST,
                        NETBIOS_REQUEST.length, inetAddress, NETBIOS_UDP_PORT);
                socket = new DatagramSocket();
                socket.setSoTimeout(200);

                for (int i = 0; i < 3; i++) {
                    socket.send(query);
                    socket.receive(packet);

                    byte[] data = packet.getData();
                    if (data != null && data.length >= 74) {
                        String response = new String(data, "ASCII");
                        name = response.substring(57, 73).trim();

                        for (int k = 0; k < adapter.getData().size(); k++) {
                            HostBean h = (HostBean) adapter.getData().get(k);
                            if (h.getIp().equals(target_ip)) {
                                mHandler.obtainMessage(DATASET_HOST_ALIAS_CHANGED, k, 0, name).sendToTarget();
                                break;
                            }
                        }
                        break;
                    }
                }
            } catch (SocketTimeoutException ste) {
            } catch (IOException e) {
            } finally {
                if (socket != null)
                    socket.close();
            }

        }
    }


    private void stopDiscovery() {
        stop = true;
        if (arpReader != null && arpReader.isAlive()) {
            arpReader.interrupt();
            arpReader = null;
        }
        if (discoveryThread != null && !discoveryThread.isAlive()) {
            discoveryThread.interrupt();
            discoveryThread = null;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopDiscovery();
        recyclerView.finishRefresh();

    }
}
