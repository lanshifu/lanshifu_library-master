package library.lanshifu.com.myapplication.bluetooth.thread;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.bluetooth.BluetoothChatHelper;
import library.lanshifu.com.myapplication.bluetooth.ChatConstant;

/**
 * @Description: 连接后维护线程
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 2016-09-13 18:38
 */
public class ConnectedThread extends Thread {

    private final BluetoothChatHelper mHelper;
    private final BluetoothSocket mSocket;
    private final InputStream mInStream;
    private final OutputStream mOutStream;

    public ConnectedThread(BluetoothChatHelper bluetoothChatHelper, BluetoothSocket socket, String socketType) {
        L.d("创建守护线程: " + socketType);
        mHelper = bluetoothChatHelper;
        mSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            L.e("temp sockets not created", e);
        }

        mInStream = tmpIn;
        mOutStream = tmpOut;
    }

    public void run() {
        L.d("BEGIN mConnectedThread");
        int bytes;
        byte[] buffer = new byte[1024];

        // Keep listening to the InputStream while connected
        while (!mHelper.isStopThread()) {
            try {
                bytes = mInStream.read(buffer);
                byte[] data = new byte[bytes];
                L.e("消息："+data);
                System.arraycopy(buffer, 0, data, 0, data.length);
                mHelper.getHandler().obtainMessage(ChatConstant.MESSAGE_READ, bytes, -1, data).sendToTarget();
            } catch (IOException e) {
                L.e("读取出错了", e);
                mHelper.start(false);
                break;
            }
        }
    }

    public void write(byte[] buffer) {
        if(mSocket.isConnected()){
            try {
                mOutStream.write(buffer);
                mHelper.getHandler().obtainMessage(ChatConstant.MESSAGE_WRITE, -1, -1, buffer).sendToTarget();
            } catch (IOException e) {
                L.e("Exception during write", e);
            }
        }
    }

    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) {
            L.e("close() of connect socket failed", e);
        }
    }
}
