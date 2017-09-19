package library.lanshifu.com.myapplication.network_into;

import android.content.Context;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.UnknownHostException;

import library.lanshifu.com.lsf_library.utils.NetworkUtils;
import library.lanshifu.com.myapplication.MyApp;
import library.lanshifu.com.myapplication.model.HostBean;

/**
 * Created by lanshifu on 2017/9/17.
 */

public class NetworkHelper {


    private static InetAddress mInetAddress;
    private static int int_gateway;
    private static int int_ip;
    private static int int_net_mask;
    private static String gatewayMac;
    public static boolean isHttpserverRunning = false;
    public static boolean isHijackRunning = false;
    public static boolean isTcpdumpRunning = false;
    public static boolean isInjectRunning = false;
    public static boolean isKillRunning = false;
    public static HostBean hostBean = null;


    private static String mStoragePath = null;
    public static String PORT = "10000";


    public static void initWifiInfo() {
        WifiManager wifiManager = (WifiManager) MyApp.getContext().getSystemService(Context.WIFI_SERVICE);
        int_ip = wifiManager.getDhcpInfo().ipAddress;
        int_net_mask = wifiManager.getDhcpInfo().netmask;
        /**获取不到子网掩码，nexus5实测，偶尔拿不到**/
        if (int_net_mask == 0) {
            int_net_mask = (0 << 24) + (0xff << 16) + (0xff << 8) + 0xff ;
        }
        int_gateway = wifiManager.getDhcpInfo().gateway;
        try {
            mInetAddress = InetAddress.getByName(NetworkUtils.netfromInt(int_ip));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        gatewayMac = wifiManager.getConnectionInfo().getBSSID().replace('-', ':');
    }

    public static String getGateway() {
        return NetworkUtils.netfromInt(int_gateway);
    }

    public static String getGatewayMac() {
        return gatewayMac;
    }

    public static InetAddress getInetAddress() {
        return mInetAddress;
    }

    public static String getIp() {
        return NetworkUtils.netfromInt(int_ip);
    }

    public static int getIntNetMask() {
        return int_net_mask;
    }

    public static int getIntGateway() {
        return int_gateway;
    }

    public static int getHostCount() {
        return NetworkUtils.countHost(int_net_mask);
    }


    public static void setTarget(HostBean h){
        hostBean = h;
    }

    public static HostBean getTarget() {
        return hostBean;
    }

}
