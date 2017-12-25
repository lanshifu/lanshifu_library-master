package library.lanshifu.com.myapplication.bluetooth.thread;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.myapplication.bluetooth.BluetoothChatHelper;

import static library.lanshifu.com.myapplication.bluetooth.ChatConstant.NAME_INSECURE;
import static library.lanshifu.com.myapplication.bluetooth.ChatConstant.UUID_INSECURE;
import static library.lanshifu.com.myapplication.bluetooth.State.STATE_CONNECTED;
import static library.lanshifu.com.myapplication.bluetooth.State.STATE_CONNECTING;
import static library.lanshifu.com.myapplication.bluetooth.State.STATE_LISTEN;
import static library.lanshifu.com.myapplication.bluetooth.State.STATE_NONE;

/**
 * @Description: 监听线程
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 2016-09-13 17:57
 */
public class AcceptThread extends Thread {

    private static BluetoothServerSocket mServerSocket;
    private String mSocketType;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mRemoveDevice;


    public AcceptThread(BluetoothDevice device, boolean secure) {
        mRemoveDevice = device;
        BluetoothServerSocket tmp = null;
        mSocketType = secure ? "Secure" : "Insecure";


        try {
//            if (secure) {
//                mServerSocket = mHelper.getAdapter().listenUsingRfcommWithServiceRecord(ChatConstant.NAME_SECURE, ChatConstant.UUID_SECURE);
//            } else {
            mServerSocket = BluetoothChatHelper.getInstance().getAdapter().listenUsingInsecureRfcommWithServiceRecord(NAME_INSECURE, UUID_INSECURE);
//            }

        } catch (IOException e) {
            L.e("初始化mServerSocket报错" + e.getMessage());
        }

//        mServerSocket = getServerSocket();

    }

    private BluetoothServerSocket getServerSocket() {

        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter
                    .getDefaultAdapter();
        }

        Method listenMethod = null;
        try {
            listenMethod = mBluetoothAdapter.getClass().getMethod("listenUsingRfcommOn", int.class);
        } catch (SecurityException e) {
            L.e("SecurityException :" + e.getMessage());
        } catch (NoSuchMethodException e) {
            L.e("NoSuchMethodException :" + e.getMessage());
        }
        try {
            return (BluetoothServerSocket) listenMethod.invoke(mBluetoothAdapter, 29);
        } catch (IllegalArgumentException e) {
            L.e("IllegalArgumentException :" + e.getMessage());
        } catch (IllegalAccessException e) {
            L.e("IllegalAccessException :" + e.getMessage());
        } catch (InvocationTargetException e) {
            L.e("InvocationTargetException :" + e.getMessage());
        }
        return null;
    }


    public void run() {
        L.d("AcceptThread run 执行");
        setName("AcceptThread" + mSocketType);

        BluetoothSocket socket = null;


        while (BluetoothChatHelper.getInstance().getState() != STATE_CONNECTED && !BluetoothChatHelper.getInstance().isStopThread()) {
            try {
                if (mServerSocket != null) {
                    L.d("调用 mServerSocket.accept()，阻塞中................");
                    socket = mServerSocket.accept();
                } else {
                    L.e("mServerSocket == null");
                    break;
                }
            } catch (IOException e) {
                L.e(" mServerSocket.accept() 异常：" + e.getMessage());
                BluetoothChatHelper.getInstance().connectionFailed(e.getMessage());
                break;
            }
            if (socket != null) {
                synchronized (this) {
                    if (BluetoothChatHelper.getInstance().getState() == STATE_LISTEN
                            || BluetoothChatHelper.getInstance().getState() == STATE_CONNECTING) {
                        L.d("连接...");
                        BluetoothChatHelper.getInstance().connected(socket, socket.getRemoteDevice(), mSocketType);
                    } else if (BluetoothChatHelper.getInstance().getState() == STATE_NONE
                            || BluetoothChatHelper.getInstance().getState() == STATE_CONNECTED) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            L.d("关闭sonket出错" + e.getMessage());
                        }
                    }
                }
            }
        }
        L.d("结束 AcceptThread");
    }

    public void cancel() {
        L.e("Socket Type" + mSocketType + "cancel " + this);
        close();
    }

    private void close() {
        try {
            if (mServerSocket != null) {
                L.e("关闭 ServerSocket");
                mServerSocket.close();
            }
        } catch (IOException e) {
            L.e("关闭 ServerSocket failed" + e);
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, UUID_INSECURE);
        } catch (Exception e) {
            L.e("createBluetoothSocket 出错:" + e.getMessage());
            return null;
        }
    }
}
