package library.lanshifu.com.myapplication.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lanshifu.myapp3.BookManager;
import lanshifu.myapp3.IDemandListener;
import lanshifu.myapp3.MessageBean;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.myapplication.R;

/**
 * Created by lanshifu on 2017/10/29.
 */

public class AidlActivity extends BaseToolBarActivity {
    @Bind(R.id.btn_start)
    Button btnStart;

    boolean mBound;
    @Override
    protected int getLayoutid() {
        return R.layout.activity_aidl;
    }

    @Override
    protected void onViewCreate() {

    }



    @OnClick(R.id.btn_start)
    public void onViewClicked() {

        if (!mBound) {
            start();
        }else {
            try {
                String content = demandManager.getDemand().getContent();
                MessageBean bean = new MessageBean();
                bean.setLevel(2);
                bean.setContent("来自客户的的消息");
                demandManager.setDemandIn(bean);
                showShortToast(content);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    IDemandListener.Stub listener = new IDemandListener.Stub() {
        @Override
        public void onDemandReceiver(final MessageBean msg) throws RemoteException {
            //该方法运行在Binder线程池中，是非ui线程
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showShortToast(msg.getContent());
                }
            });
        }
    };

    private BookManager demandManager;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            L.d("onServiceConnected");
            mBound = true;
            //得到该对象之后，我们就可以用来进行进程间的方法调用和传输啦。
            demandManager = BookManager.Stub.asInterface(service);
            try {
                demandManager.registerListener(listener);
                MessageBean demand = demandManager.getDemand();
                String content = demand.getContent();
                int level = demand.getLevel();
                showShortToast(content);
                L.d("content "+content);
                L.d("level "+level);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
            try {
                demandManager.unregisterListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };
    private void start() {
        Intent intent = new Intent();
        intent.setAction("lanshifu.myapp3.aidl");//service的action
        intent.setPackage("lanshifu.myapp3");//aidl文件夹里面aidl文件的包名
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        L.d("start");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        if(demandManager !=null){
            Intent intent = new Intent();
            intent.setAction("lanshifu.myapp3");//service的action
            intent.setPackage("lanshifu.myapp3");//aidl文件夹里面aidl文件的包名
            stopService(intent);
        }
    }
}
