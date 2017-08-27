package library.lanshifu.com.myapplication.bluetooth.thread;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;

import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.myapplication.bluetooth.BluetoothChatHelper;
import library.lanshifu.com.myapplication.bluetooth.ChatConstant;

/**
 * @Description: 连接线程
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 2016-09-13 17:58
 */
public class ConnectThread extends Thread {

    private BluetoothChatHelper mHelper;
    private  BluetoothSocket mSocket;
    private final BluetoothDevice mDevice;
    private String mSocketType;

    public ConnectThread(BluetoothChatHelper bluetoothChatHelper, BluetoothDevice device, boolean secure) {
        mHelper = bluetoothChatHelper;
        mDevice = device;
        BluetoothSocket tmp = null;
        mSocketType = secure ? "Secure" : "Insecure";

        try {
            if (secure) {
                mSocket = device.createRfcommSocketToServiceRecord(ChatConstant.UUID_SECURE);
            } else {
                mSocket = device.createInsecureRfcommSocketToServiceRecord(ChatConstant.UUID_INSECURE);
            }
        } catch (IOException e) {
            L.e("Socket Type: " + mSocketType + "create() failed", e);
        }
    }

    public void run() {
        L.d("BEGIN mConnectThread SocketType:" + mSocketType);
        setName("ConnectThread" + mSocketType);

        if(mHelper.getAdapter().isDiscovering()){
            mHelper.getAdapter().cancelDiscovery();
        }

        try {
            mSocket.connect();
        } catch (IOException e) {
            L.e("连接报错");
            mHelper.connectionFailed();
            try {
                mSocket.close();
            } catch (IOException e2) {
                L.e("关闭连接报错 " +e2.getMessage());
            }
            return;
        }

        synchronized (this) {
            mHelper.setConnectThread(null);
        }

        mHelper.connected(mSocket, mDevice, mSocketType);
    }

    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) {
            L.e("close() of connect " + mSocketType
                    + " socket failed", e);
        }
    }
}
